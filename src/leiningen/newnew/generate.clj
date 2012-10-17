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
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [stencil.core :as stencil])
  (:import java.util.jar.JarFile))

;; It'd be silly to expect people to pull in stencil just to render
;; a mustache string. We can just provide this function instead. In
;; doing so, it is much less likely that a template author will have
;; to pull in any external libraries. Though he is welcome to if he
;; needs.
(def render-text stencil/render-string)


(defn renderer
  "Create a renderer function that looks for mustache templates in the
   right place given the name of your template. If no data is passed, the
   file is simply slurped and the content returned unchanged."
  [name]
  (fn [template & [data]]
    (let [path (string/join "/" ["leiningen" "new" (sanitize name) template])]
      (if data
        (render-text (slurp-resource path) data)
        (io/reader (io/resource path))))))

(defn- template-path [name path data]
  (io/file name (render-text path data)))




(def ^{:dynamic true} *dir* nil)

(defn <-generate
  "Generate a file with content. path can be a java.io.File or string.
   It will be turned into a File regardless. Any parent directories will
   be created automatically. Data should include a key for :name so that
   the project is created in the correct directory"
  [{:keys [name] :as data} & paths]
  (let [dir (or *dir* name)]
    (if (or *dir* (.mkdir (io/file dir)))
      (doseq [path paths]
        (cond (string? path)
              (.mkdirs (template-path dir path data))
          
          :else
          (let [[path content] path
                path (template-path dir path data)]
            (.mkdirs (.getParentFile path))
            (io/copy content (io/file path)))))
      (println "Could not create directory " dir ". Maybe it already exists?"))))


;; Directory Rendering:

(defn list-jar [jar-path inner-dir]
  (if-let [jar          (JarFile. jar-path)]
    (let [inner-dir    (if (and (not= "" inner-dir) (not= "/" (last inner-dir)))
                         (str inner-dir "/")
                         inner-dir)
          entries      (enumeration-seq (.entries jar))
          filenames    (map (fn [x] (.getName x)) entries)
          filenames    (filter (fn [x] (= 0 (.indexOf x inner-dir))) filenames)]
      (map #(subs % (count inner-dir)) filenames))))

(defn get-jar-path [template-name]
  (let [cl     (.getContextClassLoader (Thread/currentThread))
        jars   (seq (.getURLs cl))
        t-name (str template-name "/lein-template")]
    (->> jars 
         (filter (fn [x] (< 0 (.indexOf (.getPath x) t-name))))
         first
         ((fn [x] (.getPath x))))))

(defn list-resources [template-name]
  (list-jar 
   (get-jar-path template-name)
   (str "leiningen/new/" template-name)))


(defn read-from-jar [jar-path inner-path]
  (if-let [jar   (JarFile. jar-path)]
    (if-let [entry (.getJarEntry jar inner-path)]
      (slurp (.getInputStream jar entry)))))
