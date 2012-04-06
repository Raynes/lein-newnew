(ns leiningen.new.default
  "Generate a basic project."
  (:use [leiningen.new.templates :only [renderer sanitize year ->files sanitize-ns]]))

(defn default
  "A basic and general project layout."
  [name]
  (let [render (renderer "default")
        data {:name (sanitize-ns name)
              :sanitized (sanitize name)
              :year (year)}]
    (println "Generating a project called" name "based on the 'default' template.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             [".gitignore" (render "gitignore" data)]
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["test/{{sanitized}}/core_test.clj" (render "test.clj" data)])))
