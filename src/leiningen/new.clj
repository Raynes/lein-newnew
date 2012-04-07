(ns leiningen.new
  "Generate project scaffolding based on a template."
  (:refer-clojure :exclude [new list])
  (:require [bultitude.core :as bultitude])
  (:import java.io.FileNotFoundException))

(defn- fake-project [name]
  {:templates [[(symbol name "lein-template") "(0.0.0,)"]]
   :repositories {"clojars" {:url "http://clojars.org/repo/"}
                  "central" {:url "http://repo1.maven.org/maven2"}}})

(defn resolve-remote-template [name sym]
  (if-let [get-dep (resolve 'leiningen.core.classpath/resolve-dependencies)]
    (try (get-dep :templates (fake-project name) :add-classpath? true)
         (require sym)
         true
         (catch Exception _))))

(defn abort [name]
  (let [abort (or (resolve 'leiningen.core.main/abort)
                  (resolve 'leiningen.core/abort))]
    (abort "Could not find template" name "on the classpath.")))

(defn resolve-template [name]
  (let [sym (symbol (str "leiningen.new." name))]
    (if (try (require sym)
             true
             (catch FileNotFoundException _
               (resolve-remote-template name sym)))
      (resolve (symbol (str sym "/" name)))
      (abort name))))

;; A lein-newnew template is actually just a function that generates files and
;; directories. We have a bit of convention: we expect that each template is on
;; the classpath and is based in a .clj file at `leiningen/new/`. Making this
;; assumption, a user can simply give us the name of the template he wishes to
;; use and we can `require` it without searching the classpath for it or doing
;; other time consuming things. If this namespace isn't found and we are
;; running Leiningen 2, we can resolve it via pomegranate first.
;;
;; Since our templates are just function calls just like Leiningen tasks, we can
;; also expect that a template generation function also be named the same as the
;; last segment of its namespace. This is what we call to generate the project.

(defn create
  ([name]
     (create "default" name))
  ([template name & args]
     (if (and (re-find #"(?i)(?<!(clo|compo))jure" name)
              (not (System/getenv "LEIN_IRONIC_JURE")))
       (println "Sorry, names based on non-ironic *jure puns are not allowed.\n"
                "If you intend to use this name ironically, please set the\n"
                "LEIN_IRONIC_JURE environment variable and try again.")
       (apply (resolve-template template) name args))))

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
  (for [n (bultitude/namespaces-on-classpath :prefix "leiningen.new.")
        ;; There are things on the classpath at `leiningen.new` that we
        ;; don't care about here. We could use a regex here, but meh.
        :when (not= n 'leiningen.new.templates)]
    (-> (doto n require)
        (the-ns)
        (ns-resolve (symbol (last (.split (str n) "\\.")))))))

(defn show
  "Show details for a given template."
  [name]
  (let [resolved (meta (resolve-template name))]
    (println (:doc resolved "No documentation available."))
    (println)
    (println "Argument list:" (or (:help-arglists resolved)
                                  (:arglists resolved)))))

(defn ^{:no-project-needed true
        :help-arglists '[[project project-name]
                         [project template project-name & args]]
        :subtasks (list)}
  new
  "Generate scaffolding for a new project based on a template.

If only one argument is passed, the default template is used and the
argument is treated as if it were the name of the project.

Use \":show\" instead of a project name to show template details." 
  [& args]
  (let [args (if (or (map? (first args)) (nil? (first args)))
               (rest args)
               args)]
    (if (= ":show" (second args))
      (show (first args))
      (apply create args))))
