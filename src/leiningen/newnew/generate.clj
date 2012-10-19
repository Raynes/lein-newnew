;; You can write a 'new' task yourself without any extra plugins like
;; lein-newnew. What makes newnew so useful is the `templates` task for
;; listing templates and this file. The primary problem with writing your
;; own project scaffolding tools that are domain-specific is tht you
;; generally have to reimplement the same things every single time. With
;; lein-newnew, you have this little library that your templates can use.
;; It has all the things a template is likely to need:
;; * an easy way to generate files and namespaces
;; * a way to render files written with a flexible template language
;; * a way to get those files off of the classpath transparently
(ns leiningen.newnew.generate
  (:use [clojure.string :only [split join]])
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [stencil.core :as stencil]
            [leiningen.newnew.files :as f]
            [leiningen.newnew.templates :as t] :reload))



(import java.io.File)
(def ^:dynamic *dir* nil)

(defn render-text [txt & [data]]
  (stencil/render-string txt (or data {})))

(defn render-path [base path & [data]]
  (str base File/separator (render-text path data)))

(defn make-dir [base path & [data]]
  (let [dirname (render-path base path data)
        dir     (io/file dirname)]
    (if (not (.exists dir))
      (.mkdirs dir))))

(defn create-file [base [input file-path] & [data]]
  (let [filename   (render-path base file-path data)
        file       (File. filename)]
    (if (not (.exists file))
      (let [dir-path   (join File/separator (butlast (split file-path #"[/\\]" )))
            ___dir     (make-dir base dir-path data)
            ___file    (.createNewFile file)]))
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

(defn render-dir [tmpl-name tmpl-jar base [tmpl-path file-path] & [data]]
  (let [tmpl-dir (f/format-jar-path
                  (str "leiningen/new/" (t/sanitize tmpl-name) "/" tmpl-path))
        tmpl-files    (f/list-jar tmpl-jar tmpl-dir)]
    (doseq [file tmpl-files]
      (let [fname_  (apply str (concat file-path File/separator file))
            fname  (f/format-jar-path (render-path "" fname_ data))]
        (render-file tmpl-name tmpl-jar base
                     [file fname] data)))))

(let [[i1 & [i2 & args]] [1 2 3]]
  (println i1 i2 args))

(defn gen-input-vec [inp]
  (cond (or (list? inp) (vector? inp))
        (let [[& [i1 & [i2 & args]]] inp]
          (cond (nil? i1) (throw (Exception. "There should be at least one argument."))
                (nil? i2) [i1 i1]
                ;;except (vector i1 i2)
                :else [i1 i2]))
        (instance? String inp) [inp inp]))

(gen-input-vec "1")

(defn render-project
  [{:keys [data directives] :as template}]
  (let [dir    (or *dir* name)
        stages [:copy-dirs  :render-dirs  :make-dirs
                :copy-files :render-files :make-files]]
    (cond (or *dir* (.mkdir (io/file dir)))
          (doseq [stg stages]
                )
          )
  (println "Rendering template using:" )
  (println template)))

;;(create "newnew-test-template" "tester")




;;

;; Directory Rendering:
(comment

  (import java.util.jar.JarFile)
  (def name1 "org.zcaudate/pressi")
(render-dir  "newnew-test-template"
             (JarFile. "/Users/Chris/.m2/repository/newnew-test-template/lein-template/0.1.0/lein-template-0.1.0.jar")
             "new-template"
             ["" "template"]
             {:raw-name name1
              :name (t/project-name name1)
              :namespace (t/sanitize-ns name1)
              :nested-dirs (t/name-to-path name1)
              :year (t/year)})


(render-file "newnew-test-template"
             (JarFile. "/Users/Chris/.m2/repository/newnew-test-template/lein-template/0.1.0/lein-template-0.1.0.jar")
             "new-template"
             ["project.clj" "project.clj"]
             {:raw-name "hello2"})


(def a (File. "hello-there.txt"))
(.createNewFile a)

(string/split
 (tpl-path "template" "teot/{{hello}}/tn.txt" {:hello "there"})
  #"[/\\]")

(render-text  "teot/{{hello}}/tn.txt" {:hello "there"})

(render-path "template" "teot/{{hello}}/tn.txt" {:hello "there"})

(make-file "template" [(slurp "http://www.google.com") "teot/{{hello}}/tn.html"] {:hello "there"})


  )
