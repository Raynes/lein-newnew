(ns leiningen.new.default
  "Generate a basic project."
  (:use [leiningen.new.templates :only [renderer sanitize year
                                        ->files sanitize-ns qualified-name-to-path]]))

(defn default
  "A general project template."
  [name]
  (let [render (renderer "default")
        data {:name (sanitize-ns name)
              :sanitized (sanitize name)
              :nested-dirs (qualified-name-to-path name)
              :year (year)}]
    (println "Generating a project called" name "based on the 'default' template.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             [".gitignore" (render "gitignore" data)]
             ["src/{{nested-dirs}}/core.clj" (render "core.clj" data)]
             ["test/{{nested-dirs}}/core_test.clj" (render "test.clj" data)])))
