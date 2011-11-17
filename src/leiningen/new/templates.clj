(ns leiningen.new.templates
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clostache.parser :as parser]))

(defn slurp-resource
  "Reads the contents of a file on the classpath."
  [resource-name]
  (-> resource-name .getPath io/resource io/reader slurp))

(defn sanitize
  "Replace hyphens with underscores."
  [s]
  (string/replace s #"-" "_"))

(def render-text parser/render)

(defn renderer
  "Create a renderer function that looks for mustache templates in the
   right place given the name of your template. If no data is passed, the
   file is simply slurped."
  [name]
  (fn [template & [data]]
    (let [text (slurp-resource (io/file "leiningen" "new" name template))]
      (if data
        (render-text text data)
        text))))

(defn- template-path [name path data]
  (io/file name (render-text path data)))

(defn ->files
  "Generate a file with content. path can be a java.io.File or string.
   It will be turned into a File regardless. Any parent directories will
   be created automatically."
  [name data & paths]
  (.mkdir (io/file name))
  (doseq [path paths]
    (if (string? path)
      (.mkdirs (template-path name path data))
      (let [[path content] path
            path (template-path name path data)]
        (.mkdirs (.getParentFile path))
        (spit path content)))))