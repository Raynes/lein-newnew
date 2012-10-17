(ns leiningen.new.website
  (:use [leiningen.newnew.utils :only [year project-name sanitize-ns name-to-path]]
        [leiningen.newnew.gen :only [<-generate]]))

(defn website
  "An example template for websites"
  [name]
  (let [data {:raw-name name
              :name (project-name name)
              :namespace (sanitize-ns name)
              :nested-dirs (name-to-path name)
              :year (year)}]
    (println "Generating fresh website project in " name)
    (<-generate data
              [:file-render  "README.md" "README.md"]
              [:file-render  "project.clj" "project.clj"]
              [:file-copy    ".gitignore" "gitignore"]
              [:dir-make     "scratch"]
              [:dir-copy     "resources" "resources" :except []]
              [:dir-render   "src"  "src"  :except ["{{nested-dirs}}/foo.clj"]]
              [:dir-render   "test" "test")))
