(ns leiningen.new.test-new-templates
  (:require [leiningen.new.templates :as tp]
            [leiningen.new :as n]
            [clojure.java.io :as io]))

(def as (tp/slurp-resource "/Users/Chris/.m2/repository/lein-newnew/lein-newnew/0.3.5/lein-newnew-0.3.5.jar"))

(all-methods (io/resource "leiningen/new/noir.clj"))
(.getFile (io/resource "leiningen/new/noir.clj"))
(enumeration-seq (.getResources cl "leiningen/new/noir.clj"))

(enumeration-seq (.getResources cl "leiningen/new/noir/"))
(enumeration-seq (.findResources cl "leiningen/new/noir"))
(.getResource cl "leiningen")

(tp/slurp-resource "leiningen/new/website/intro.md")
(def a (slurp "project.clj"))

(println a)
(println as)
(println (n/list ))
(def a (resolve 'leiningen.core.classpath/resolve-dependencies))
(a :templates {:templates [[(symbol name "lein-template") "(0.0.0,)"]]
               :repositories {"clojars" {:url "http://clojars.org/repo/"}
                              "central" {:url "http://repo1.maven.org/maven2"}}})

(println (io/resource "resources/lein-template/hello.txt"))
(tp/render-text "leiningen/new/app/src/{{sanitized}}/core.clj" {:sanitized "hello"})
(io/resource "leiningen/new/noir")



(def fa (leiningen.core.classpath/resolve-dependencies
         :templates
         {:templates [[(symbol "noir" "lein-template") "(0.0.0,)"]]
          :repositories {"clojars" {:url "http://clojars.org/repo/"}
                         "central" {:url "http://repo1.maven.org/maven2"}}}
         :add-classpath? true))

(import java.util.jar.JarFile)
(defn list-jar [jar-path inner-dir]
  (if-let [jar          (JarFile. jar-path)]
    (let [inner-dir    (if (and (not= "" inner-dir) (not= "/" (last inner-dir)))
                         (str inner-dir "/")
                         inner-dir)
          entries      (enumeration-seq (.entries jar))
          filenames    (map (fn [x] (.getName x)) entries)
          filenames    (filter (fn [x] (= 0 (.indexOf x inner-dir))) filenames)]
      (map #(subs % (count inner-dir)) filenames))))

(defn read-from-jar [jar-path inner-path]
  (if-let [jar   (JarFile. jar-path)]
    (if-let [entry (.getJarEntry jar inner-path)]
      (slurp (.getInputStream jar entry)))))

(println (read-from-jar "/Users/Chris/.m2/repository/lein-newnew/lein-newnew/0.3.5/lein-newnew-0.3.5.jar"
                        "leiningen/new.clj"))

(.getJarEntry (JarFile. (first fa)) "META-INF/maven/noir/lein-template/pom.x")

(println (list-jar  "/Users/Chris/.m2/repository/lein-newnew/lein-newnew/0.3.5/lein-newnew-0.3.5.jar" "leiningen"))
(println strings)
(.indexOf (first strings) "")

(use 'clojure.reflect)
(println (reflect (JarFile. (first fa))))(use 'clojure.reflect)

(defn all-methods [x]
    (->> x reflect
           :members
           (filter :return-type)
           (map :name)
           sort
           (map #(str "." %) )
           distinct
           println))
(slurp
 (.getInputStream (JarFile. (first fa)) (.getJarEntry (JarFile. (first fa)) "META-INF/maven/noir/lein-template/pom.xml")))

()

(println (.getName (second (enumeration-seq (.entries (JarFile. (first fa)))))))


(import java.util.jar.JarFile)
(defn list-jar [jar-path inner-dir]
  (if-let [jar          (JarFile. jar-path)]
    (let [inner-dir    (cond (and (not= "" inner-dir) (not= \/ (last inner-dir)))
                             (str inner-dir "/")

                             (= "/" inner-dir) ""

                             :else inner-dir)
          entries      (enumeration-seq (.entries jar))
          filenames    (map (fn [x] (.getName x)) entries)
          filenames    (filter (fn [x] (= 0 (.indexOf x inner-dir))) filenames)]
      (map #(subs % (count inner-dir)) filenames))))

(defn read-from-jar [jar-path inner-path]
  (if-let [jar   (JarFile. jar-path)]
    (if-let [entry (.getJarEntry jar inner-path)]
      (slurp (.getInputStream jar entry)))))

(defn get-jar-path [template]
  (let [cl     (.getContextClassLoader (Thread/currentThread))
        jars   (seq (.getURLs cl))
        name   (str template "/lein-template")]
    (->> jars
         (filter (fn [x] (< 0 (.indexOf (.getPath x) name))))
         first
         ((fn [x] (.getPath x))))))

(defn list-resources
  ([template] (list-resources template ""))
  ([template dir]
      (list-jar
       (get-jar-path template)
       (clojure.string/join "/" ["leiningen" "new" template]))))

(list-resources  "noir")

(.getPath (first (.getURLs cl)))
(filter (fn [x] (< 0 (.indexOf (.getPath x) "noir/lein-template"))) (seq (.getURLs cl)))
