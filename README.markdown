# NOTICE

As of Leiningen 2.0.0, this task is included inside of leiningen's codebase. This plugin will no longer be developed
and all issues should be opened on [leiningen](https://github.com/technomancy/leiningen)'s issue tracker.

# lein-newnew

## NOTE: This plugin has been merged into Leiningen as of v2.0.0


This plugin provides the 'new' task for creating fresh project skeletons from Leiningen 2.x, but is usable from Leiningen 1.x as well.

It is extensible via templates and has a simple API for creating them. With this new task, you can create templates for any sort of project scaffolding you can imagine, as simple or complex as you like.

By default, it includes four templates: default, app, plugin, and template. 'default' is for libraries, the same as what Leiningen's old 'new' task spits out. 'app' is for applications, while 'plugin' generates a skeleton Leiningen plugin project. 'template' is a very meta template for creating new templates.

TEMPLATES! WOOT!

## Writing Templates

Suppose you've written a fabulously popular library, used the world
over by adoring fans. For the purposes of this document, let's say
this library is called "liquid-cool". If using liquid-cool takes a bit
of setup, or if you'd just like to give your users a little guidance
on how one might best create a new project which uses liquid-cool, you
might want to provide a template for it (just like how `lein` (via
lein-newnew) already provides built-in templates for "app", "plugin",
and so on).

Let's assume your library's project dir is ~/dev/liquid-cool. Create a
template for it like so:

    cd ~/dev
    lein new template liquid-cool --to-dir liquid-cool-template

Note that you'll now have a new and separate project named
"liquid-cool-template". It will have a group-id of "liquid-cool", and
an artifact-id of "lein-template".

> All lein templates have an artifact-id of "lein-template", and are
> differentiated by their group-id, which always should match the
> project for which they provide a template.

The files that your template will provide to users are in
src/leiningen/new/liquid_cool. lein-newnew starts you off with just
one, named "foo.clj". You can see it referenced in
src/leiningen/new/liquid_cool.clj, right underneath the "`->files
data`" line.

You can delete foo.clj if you like (and it's corresponding line in
liquid-cool.clj), and start populating that
src/leiningen/new/liquid_cool directory with the files you wish to be
part of your template. For everything you add, make sure the
liquid-cool.clj file receives corresponding entries in that `->files`
call. For examples to follow, have a look inside [the \*.clj files for
the built-in
templates](https://github.com/Raynes/lein-newnew/tree/master/src/leiningen/new).

While developing a template, if you're in the template project
lein-newnew will pick it up and you'll be able to test it. However, if
you want to use it on your system without putting it on clojars, just
`lein install` your template. If you're using Leiningen 1.x, do `lein
plugin install mytemplate 0.1.0`; on Leiningen 2.x it will be
available automatically.

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
