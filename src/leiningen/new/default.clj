(ns leiningen.new.default
  "Generate a basic application project."
  (:use [leiningen.newnew.templates :only [year project-name
                                        sanitize-ns name-to-path]]))
(defn default
  "A general project template for libraries.
  Accepts a group id in the project name: `lein new foo.bar/baz`"
  [name]  
  (println "Generating a project called" name "based on the 'default' template.")
  (println "To see other templates (app, lein plugin, etc), try `lein help new`.")
  {:template true
   :data 
     {:raw-name name
      :name (project-name name)
      :namespace (sanitize-ns name)
      :nested-dirs (name-to-path name)
      :year (year)}
   :directives
     {:render-dirs [[""]]}})

