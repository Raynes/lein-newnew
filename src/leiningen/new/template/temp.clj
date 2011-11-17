(ns leiningen.new.{{name}}
    (:use leiningen.new.templates))

(def render (renderer "{{name}}"))

(defn {{name}}
  "FIXME: write documentation"
  [name]
  (let [data {:name name}]
    (->files name data
             ["foo.clj" (render "foo.clj" data)])))