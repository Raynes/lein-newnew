(ns leiningen.newnew.generate
  (:use [clojure.string :only [split join]])
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [stencil.core :as stencil]
            [leiningen.newnew.files :as f]
            [leiningen.newnew.templates :as t] :reload)
  (:import java.util.jar.JarFile
           java.io.File))

(def ^:dynamic *dir* nil)

(defn render-text [txt & [data]]
  (stencil/render-string txt (or data {})))

(defn render-path [base path & [data]]
  (if (or (= base "") (= base File/separator)) path
      (str base File/separator
           (if data
             (render-text path data)
             path))))

(defn make-dir [base [path _] & [data]]
  ;;(println base path data)
  (let [dirname (render-path base path data)
        dir     (io/file dirname)]
    (if (not (.exists dir))
      (.mkdirs dir))))

(defn create-file [base [input file-path] & [data]]
  (let [filename   (render-path base file-path data)
        file       (File. filename)]
    ;;(println "CREATING:" filename)
    (if (not (.exists file))
      (let [dir-path   (join File/separator (butlast (split filename #"[/\\]" )))
            _          (make-dir "" [dir-path] data)
            _          (.createNewFile file)]))
    file))

(defn make-file [base [input file-path] & [data]]
  (let [file  (create-file base [input file-path] data)]
    (io/copy input file)))


(defn render-file [tmpl-name tmpl-jar base [tmpl-path file-path] & [data]]
  (let [file      (create-file base [tmpl-path file-path] data)
        tmpl-file (str "leiningen/new/" (t/sanitize tmpl-name) "/" tmpl-path)]
    (cond (nil? data) (f/transfer-resource tmpl-jar tmpl-file file)
          :else
          (f/transfer-resource tmpl-jar tmpl-file file
                               #(render-text % data)))))

(defn render-dir [tmpl-name tmpl-jar base [tmpl-path file-path & {:keys [except]}] & [data]]
  (let [tmpl-dir    (f/format-jar-path
                       (str "leiningen/new/" (t/sanitize tmpl-name) "/" tmpl-path))
        tmpl-files  (f/list-jar tmpl-jar tmpl-dir)
        tmpl-files#  (if except
                       (filter #(not (some (fn [x] (= 0 (.indexOf % (f/add-ending-slash x)))) except)) tmpl-files)
                           tmpl-files)]
    (doseq [file tmpl-files#]
      (let [fname_  (apply str (concat file-path File/separator file))
            jname  (f/trim-beginning-slash (render-path tmpl-path file nil))
            fname  (f/trim-beginning-slash (render-path "" fname_ data))]
        (render-file tmpl-name tmpl-jar base
                     [jname fname] data)))))

(defn- gen-input-vec [inp]
  (cond (or (list? inp) (vector? inp))
        (let [[& [i1 & [i2 & args]]] inp]
          (cond (nil? i1) (throw (Exception. "There should be at least one argument."))
                (nil? i2) [i1 i1]
                (= i2 :except) (apply vector i1 i1 :except args)
                (nil? args) [i1 i2]
                :else (apply vector i1 i2 args)))
        (instance? String inp) [inp inp]))

(defn- do-directive [stage tmpl-name tmpl-jar base [& params] & [data]]
  ;;(println "DO-DIrective**: " stage base params data)
  (cond (= stage :make-dirs)   (make-dir base params data)
        (= stage :make-files)  (make-file base params data)
        (= stage :copy-dirs)   (render-dir tmpl-name tmpl-jar base params)
        (= stage :copy-files)  (render-file tmpl-name tmpl-jar base params)
        (= stage :render-dirs)   (render-dir tmpl-name tmpl-jar base params data)
        (= stage :render-files)  (render-file tmpl-name tmpl-jar base params data)))

(defn render-project
  [tmpl-name {:keys [data directives] :as template}]
  ;;(println "RENDERING PROJECT")
  (println data directives)
  (let [dir      (or *dir* (:name data))
        stages   [:copy-dirs  :render-dirs  :make-dirs
                  :copy-files :render-files :make-files]]
    (if-let [jar-path (f/find-template-jar tmpl-name)]
      (with-open [tmpl-jar (JarFile. jar-path)]
        (cond (or *dir* (.mkdir (io/file dir)))
              (doseq [stg stages]
                (if-let [ds (stg directives)]
                  (doseq [d ds]
                    (do-directive stg
                                  tmpl-name
                                  tmpl-jar
                                  dir
                                  (gen-input-vec d)
                                  data)))))))))
(comment

  (binding [*dir* "dearfly"]
    (render-project "newnew-test-template" (newnew-test-template "org.e/dearfly")))

  (defn newnew-test-template
    "An example template for websites"
    [name]
    {:template true
     :data
     {:raw-name name
      :name (t/project-name name)
      :namespace (t/sanitize-ns name)
      :nested-dirs (t/name-to-path name)
      :year (t/year)}

     :directives ;; key, input/output or [["input" "output"]]
     {:render-dirs [["" :except ["example" ".gitignore"]]]}})

)
