(ns leiningen.template
  "Get information about a lein-newnew template."
  (:use [leiningen.new :only [resolve-template]]))

(defn ^{:no-project-needed true
        :help-arglists '([name])} template
  "Get detailed info about a template."
  ([name] (template nil name))
  ([project name]
   (let [resolved (meta (resolve-template name))]
     (println (:doc resolved))
     (println)
     (println "Argument list: " (or (:help-arglists resolved)
                                    (:arglists resolved))))))
