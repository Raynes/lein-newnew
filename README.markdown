# lein-newnew

This plugin provides the 'new' task for creating fresh project skeletons from Leiningen 2.x, but is usable from Leiningen 1.x as well.

It is extensible via templates and has a simple API for creating them. With this new task, you can create templates for any sort of project scaffolding you can imagine, as simple or complex as you like.

By default, it includes four templates: default, app, plugin, and template. 'default' is for libraries, the same as what Leiningen's old 'new' task spits out. 'app' is for applications, while 'plugin' generates a skeleton Leiningen plugin project. 'template' is a very meta template for creating new templates.

TEMPLATES! WOOT!

## Writing Templates

Templates are very simple. If you wanted to create a template called 'blah', you'd just need to have a file on the classpath at `leiningen/new/blah.clj`. Any mustache templates you intend to use would go in `leiningen/new/blah/`. There isn't really much to talk about. Check out `leiningen.new.templates` for the user-level template-writing API, and check out the simple included templates for examples. When you're ready to write your template, use `lein new template <name>` and hack away!

While developing a template, if you're in the template project lein-newnew will pick it up and you'll be able to test it. However, if you want to use it on your system without putting it on clojars, just `lein install` your template. If you're using Leiningen 1.x, do `lein plugin install mytemplate 0.1.0`; on Leiningen 2.x it will be available automatically.

### Distributing your template

Templates are just maven artifacts. Particularly, they need only be on the classpath when 'lein new' is called. So, as an awesome side-effect, you can just put your templates in a jar and toss them on clojars and have people install them like normal Leiningen plugins.

In Leiningen 2.x, templates get dynamically fetched if they're not found. So for instance `lein new heroku myproject` will find the latest version of the `heroku/lein-template` project from Clojars and use that.

## Usage in Leiningen 1.x

    $ lein plugin install lein-newnew 0.2.6
    $ lein new foo
    $ lein new plugin lein-foo
    
This plugin **requires** Leiningen 1.6.2 or later. Please confirm that you have this version by running `lein version` before installing the plugin. If you install the plugin on an older version (not sure how old it has to be to cause problems), you'll probably need to uninstall the plugin and then reinstall it after upgrading.

## Usage in Leiningen 2.x

You can pull in a newer version of this plugin than the one that comes with Leiningen 2 if you like; just add it to the `:plugins` section of your `:user` profile in ~/.lein/profiles.clj:

```clj
{:user {:plugins [[lein-newnew "0.3.4"]]}}
```

## License

Copyright © 2011-2012 Anthony Grimes and contributors

Distributed under the Eclipse Public License, the same as Clojure.
