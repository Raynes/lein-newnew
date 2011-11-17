(ns leiningen.templates
  "List templates on the classpath."
  (:use [leiningen.util.ns :only [namespaces-matching]]))

(defn ^{:help-arglists '([])} templates
  "List available 'lein new' templates"
  []
  (println "List of 'lein new' templates on the classpath:")
  (doseq [n (remove '#{leiningen.new.templates leiningen.new}
                    (namespaces-matching "leiningen.new"))]
    (require n)
    (let [n-meta (meta
                  (ns-resolve (the-ns n)
                              (symbol (last (.split (str n) "\\.")))))]
      (println (str (:name n-meta) ":")
               (or (:doc n-meta) "No documentation available.")))))