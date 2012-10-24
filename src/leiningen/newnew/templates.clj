(ns leiningen.newnew.templates
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn project-name
  "Returns project name from (possibly group-qualified) name:

   mygroup/myproj => myproj
   myproj         => myproj"
  [s]
  (last (string/split s #"/")))

(defn slurp-resource
  "Reads the contents of a file on the classpath."
  [resource-path]
  (-> resource-path io/resource io/reader slurp))

(defn sanitize
  "Replace hyphens with underscores."
  [s]
  (string/replace s #"-" "_"))

(defn name-to-path
  "Constructs directory structure from fully qualified artifact name:

   myproject         creates src/myproject/* directory
   mygroup.myproject creates src/mygroup/myproject/* directory

   and so on. Uses platform-specific file separators."
  [s]
  (-> s sanitize (string/replace #"\." java.io.File/separator)))

(defn sanitize-ns
  "Returns project namespace name from (possibly group-qualified) project name:

   mygroup/myproj  => mygroup.myproj
   myproj          => myproj
   mygroup/my_proj => mygroup.my-proj"
  [s]
  (-> s
      (string/replace #"/" ".")
      (string/replace #"_" "-")))

(defn year
  "Get the current year. Useful for setting copyright years and such."
  [] (+ (.getYear (java.util.Date.)) 1900))
