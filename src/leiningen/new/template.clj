(ns leiningen.new.template
  (:use [leiningen.newnew.templates :only [sanitize year]]))

(defn template
  "A meta-template for 'lein new' templates."
  [name]
  {:template true
   :data 
     {:name name
      :sanitized (sanitize name)
      :placeholder "{{sanitized}}"
      :year (year)}
   :directives
     {:render-dirs [["" :except ["src" "resources"]]
                    ["src" "src/leiningen/new"]]
      :copy-dirs ["resources"]}})
