(ns leiningen.new.website
  (:use [leiningen.new.templates :only [dir->copy dir->render]]))

(defn template
  "A meta-template for 'lein new' templates."
  [name]
  (let [data {:name name
              :sanitized (sanitize name)
              :placeholder "{{sanitized}}"
              :year (year)}]
    (println "Generating fresh 'lein new' template project.")
    (dir->copy   "resources" "resources")
    (dir->render data "src" "src")))
