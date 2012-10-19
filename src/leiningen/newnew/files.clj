(ns leiningen.newnew.files
  (:use leiningen.newnew.templates)
  (:require [clojure.java.io :as io])
  (:import java.util.jar.JarFile))


(def SP java.io.File/separator)

(defn trim-beginning-slash [st]
  (if (re-find #"^[\\/].+" st)
    (subs st 1) st))

(defn add-ending-slash [st]
  (if (re-find #".*[^\\/]$" st)
    (str st "/") st))

(defn format-jar-path [st]
  (-> st
      add-ending-slash
      trim-beginning-slash))


(defn find-jars [pattern]
  (let [cl     (.getContextClassLoader (Thread/currentThread))
        all   (map (fn [x] (.getPath x)) (.getURLs cl))
        jars   (filter  #(re-find #".jar$" %) all)]
    (cond (instance? String pattern)
          (filter (fn [x] (< 0 (.indexOf x pattern))) jars)

          (instance? java.util.regex.Pattern pattern)
          (filter #(re-find pattern %) jars))))

(defn find-template-jar
  "Grabs the jar associated with the template-name. This function will work only if
   the template has already been resolved using leiningen/new/resolve-template.
   Templates downloaded templates in the .m2 directory always have the form
   <name>/lein-template and so we look for that pattern in the list of current loaded jars."
  [name]
  (let [defaults   ["app" "default" "template" "plugin"]
        default?   (some #(= name %) defaults)
        search-str (cond default? (str "lein-newnew" SP "lein-newnew")
                         :else (str name SP "lein-template"))]
    (first (find-jars search-str))))

(defn list-jar
  "This function lists all file entries in a jar file, specified by its path on disk.
    Usage: (list-jar jarfile 'path/in/jar'))
  "
  ([jar] (list-jar jar ""))
  ([jar jar-path]
     (let [jar-path   (format-jar-path jar-path)
           entries    (enumeration-seq (.entries jar))
           filenames  (mapv (fn [x] (.getName x)) entries)
           filenames  (filter (fn [x] (= 0 (.indexOf x jar-path))) filenames)]
       (map #(subs % (count jar-path)) filenames))))

(defn template-files [name]
  "Lists all the resource files associated with the template. This function returns a list.
   Each element of the list is a string. The file resource associated with the string
   can be accessed using (clojure.java.io/resource (str 'leiningen/new/' <file-elem>)."
  (if-let [jar (find-template-jar name)]
    (list-jar (JarFile. jar) (str "leiningen/new/" (sanitize name)))))


;;; Methods for transforming resources from a JarFile object

(defn jar-resource
  "Accesses the inputstream for an file within the jar."
  [jar jar-path]
  (if-let [entry (.getJarEntry jar jar-path)]
    (.getInputStream jar entry)))

(defn transfer-resource [jar jar-path out-stream & [func]]
  (if-let [res-stream (jar-resource jar jar-path)]
    (let [input  (cond func (func (slurp res-stream))
                       :else res-stream)]
      (io/copy input out-stream)
      (.close res-stream)
      true)))


;;
(comment

  (format-jar-path "leiningen/new/newnew_test_template")

  (println (list-jar  (JarFile. "/Users/Chris/.m2/repository/newnew-test-template/lein-template/0.1.0/lein-template-0.1.0.jar") "leiningen/new/newnew_test_template"))

  (find-template-jar "default")
  (find-template-jar "newnew-test-template")
  (println (template-files "default"))


  (find-template-jar "newnew-test-template")
  (println
   (template-files "default"))
  (println
   (template-files  "newnew-test-template"))

  (io/copy "project.clj" (io/file "test.clj"))

  (list-jar (find-template-jar "newnew-test-template"))
  (transfer-resource
   (JarFile. (find-template-jar "newnew-test-template"))
   "leiningen/new/newnew_test_template/project.clj"
   (io/file "test.clj"))

  (io/resource "project.clj"))
