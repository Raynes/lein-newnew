(ns leiningen.new.{{name}}
  (:use [leiningen.newnew.templates :only [name-to-path]]))

(defn {{name}}
  "FIXME: write documentation"
  [name]
  {:template true
   :data 
     {:name name
      :sanitized (name-to-path name)}
   :directives
     {:render-dirs [["" :except ["resources"]]]
      :copy-dirs ["resources"]}})