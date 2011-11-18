# lein-newnew

This plugin will replace Leiningen's 'new' task in Leiningen 2.0. It is currently in the experimental stage, but is absolutely usable if you want to give it a go. Be sure to send me feedback.

It is extensible via templates and has a simple API for creating them. With this new task, you can create templates for any sort of project scaffolding you can imagine, as simple or complex as you like.

By default, it includes three templates: default, plugin, and template. 'default' is the same as what Leiningen's old 'new' task spits out, while 'plugin' generates a skeleton Leiningen plugin project. 'template' is a very meta template for creating new templates!

TEMPLATES! WOOT!

## Writing Templates

Templates are very simple. If you wanted to create a template called 'blah', you'd just need to have a file on the classpath at `leiningen/new/blah.clj`. Any mustache templates you intend to use would go in `leiningen/new/blah/`. There isn't really much to talk about. Check out `leiningen.new.templates` for the user-level template-writing API, and check out the simple included templates for examples. When you're ready to write your template, use `lein new template <name>` and hack away!

While developing a template, if you're in the template project lein-newnew will pick it up and you'll be able to test it. However, if you want to use it on your system without putting it on clojars, just `lein install` your template and then `lein plugin install mytemplate 0.1.0`. This will install the template for you.

### Distributing your template

Templates are just maven artifacts. Particularly, they need only be on the classpath when 'lein new' is called. So, as an awesome side-effect, you can just put your templates in a jar and toss them on clojars and have people install them like normal Leiningen plugins.

## Usage

    lein plugin install lein-newnew 0.1.2
    lein new foo
    lein new plugin lein-foo
    
This plugin **requires** Leiningen 1.6.2 or later. Please confirm that you have this version by running `lein version` before installing the plugin. If you install the plugin on an older version (not sure how old it has to be to cause problems), you'll probably need to uninstall the plugin and then reinstall it after upgrading. Otherwise, `lein templates` throws exceptions.

## License

Copyright (C) 2011 Anthony Grimes

Distributed under the Eclipse Public License, the same as Clojure.
