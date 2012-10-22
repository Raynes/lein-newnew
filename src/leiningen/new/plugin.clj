(ns leiningen.new.plugin
  (:use [leiningen.newnew.templates :only [sanitize year]]))

(defn plugin
  "A leiningen plugin project template."
  [name]
  (let [unprefixed (if (.startsWith name "lein-")
                     (subs name 5)
                     name)]
  {:template true
   :data 
     {:name name
      :unprefixed-name unprefixed
      :sanitized (sanitize unprefixed)
      :year (year)}
   :directives
     {:render-dirs [["" :except ["src"]]]
      :render-files [["src/name.clj" "src/leiningen/{{sanitized}}.clj"]] }}))
  