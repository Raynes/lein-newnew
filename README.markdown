# lein-newnew

This plugin will replace Leiningen's 'new' task in Leiningen 2.0. It is currently in the experimental stage, but is absolutely usable if you want to give it a go. Be sure to send me feedback.

It is extensible via templates, and has a simple API for creating them. With this new task, you can create templates for and generate any sort of project scaffolding you can imagine, as simple or complex as you like.

By default, it includes three templates: default, plugin, and template. 'default' is the same as what Leiningen's old 'new' task spits out, while 'plugin' generates a skeleton Leiningen plugin project. 'template' is a very meta template for creating new templates!

TEMPLATES! WOOT!

## Writing Templates

Templates are very simple. If you wanted to create a template called 'blah', you'd just need to have a file on the classpath at `leiningen/new/blah.clj`. Any mustache templates you intend to use would go in `leiningen/new/blah/`. There isn't really much to talk about. Check out `leiningen.new.templates` for the user-level template-writing API, and check out the simple included templates for examples. When you're ready to write your template, use `lein new template <name>` and hack away!

## Usage

    lein plugin install lein-newnew 0.1.0
    lein new foo
    lein new plugin lein-foo

## License

Copyright (C) 2011 Anthony Grimes

Distributed under the Eclipse Public License, the same as Clojure.
