(ns leiningen.new
  "Generate project scaffolding based on a template."
  (:refer-clojure :exclude [new list])
  (:require [bultitude.core :as bultitude])
  (:import java.io.FileNotFoundException))

(defn resolve-template [name]
  (let [sym (symbol (str "leiningen.new." name))]
    (if (try (require sym)
             (catch FileNotFoundException _ true))
      (println "Could not find template" name "on the classpath.")
      (resolve (symbol (str sym "/" name))))))

;; A lein-newnew template is actually just a function that generates files and
;; directories. We have a bit of convention: we expect that each template is on
;; the classpath and is based in a .clj file at `leiningen/new/`. Making this
;; assumption, a user can simply give us the name of the template he wishes to
;; use and we can `require` it without searching the classpath for it or doing
;; other time consuming things.
;;
;; Since our templates are just function calls just like Leiningen tasks, we can
;; also expect that a template generation function also be named the same as the
;; last segment of its namespace. This is what we call to generate the project.
;; If the template's namespace is not on the classpath, we can just catch the
;; FileNotFoundException and print a nice safe message.

(defn create
  ([name]
     (create "default" name))
  ([template name & args]
     (if (and (re-find #"(?i)(?<!(clo|compo))jure" name)
              (not (System/getenv "LEIN_IRONIC_JURE")))
       (println "Sorry, names based on non-ironic *jure puns are not allowed.\n"
                "If you intend to use this name ironically, please set the\n"
                "LEIN_IRONIC_JURE environment variable and try again.")
       (when-let [f (resolve-template template)]
         (apply f name args)))))

;; Since we have our convention of templates always being at
;; `leiningen.new.<template>`, we can easily search the classpath
;; to find templates in the same way that Leiningen can search to
;; find tasks. Furthermore, since our templates will always have a
;; function named after the template that is the entry-point, we can
;; also expect that it has the documentation for the template. We can
;; just look up these templates on the classpath, require them, and then
;; get the metadata off of that function to list the names and docs
;; for all of the available templates.

(defn list []
  (println "List of 'lein new' templates on the classpath:")
  (doseq [n (bultitude/namespaces-on-classpath :prefix "leiningen.new.")
          ;; There are things on the classpath at `leiningen.new` that we
          ;; don't care about here. We could use a regex here, but meh.
          :when (not= n 'leiningen.new.templates)]
    (require n)
    (let [n-meta (-> (the-ns n)
                     (ns-resolve (symbol (last (.split (str n) "\\."))))
                     (meta))]
      (println " "(:name n-meta) "-"
               (:doc n-meta "No documentation available.")))))

(defn show [name]
  (let [resolved (meta (resolve-template name))]
    (println (:doc resolved "No documentation available."))
    (println)
    (println "Argument list:" (or (:help-arglists resolved)
                                  (:arglists resolved)))))

;; TODO: document subcommands
(defn ^{:no-project-needed true
        :help-arglists '[[project project-name]
                         [project template project-name & args]]}
  new
  "Generate scaffolding for a new project based on a template.

If only one argument is passed, the default template is used and the
argument is treated as if it were the name of the project."
  [& args]
  (let [args (if (or (map? (first args)) (nil? (first args)))
               (rest args)
               args)]
    (cond (= ":list" (second args)) (list)
          (= ":show" (second args)) (show (nth args 2))
          :else (apply create args))))
