(ns leiningen.new
  (:import java.io.FileNotFoundException))

(defn new
  "Generate scaffolding for a new project based on a template.

If only one argument is passed, the default template is used and the
argument is treated as if it were the name of the project."
  ([project-name] (leiningen.new/new "default" project-name))
  ([template & args]
     (let [sym (symbol (str "leiningen.new." template))]
       (try (require sym)
            (apply (resolve (symbol (str sym "/" template))) args)
            (catch FileNotFoundException _
              (println "Could not find template" template "on the classpath."))))))