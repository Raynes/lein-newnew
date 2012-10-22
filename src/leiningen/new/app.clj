(ns leiningen.new.app
  "Generate a basic application project."
  (:use [leiningen.newnew.templates :only [year project-name
                                        sanitize-ns name-to-path]]))
(defn app
  "An application project template."
  [name]  
  (println "Generating a project called" name "based on the 'app' template.")
  {:template true
   :data 
     {:raw-name name
      :name (project-name name)
      :namespace (sanitize-ns name)
      :nested-dirs (name-to-path name)
      :year (year)}
   :directives
     {:render-dirs [[""]]}})
