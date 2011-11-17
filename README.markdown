# lein-newnew

This plugin is the future replacement of Leiningen's 'new' task. It is extensible via templates, and has a simple API for creating them. With this new task, you can create templates for and generate any sort of project scaffolding you can imagine, as simple or complex as you like.

By default, it includes two templates: 'default' and 'plugin'. 'default' is the same as what Leiningen's old 'new' task spits out, while 'plugin' generates a skeleton Leiningen plugin project.

## Usage

    lein plugin install lein-newnew 0.1.0
    lein new foo
    lein new plugin lein-foo

## License

Copyright (C) 2011 Anthony Grimes

Distributed under the Eclipse Public License, the same as Clojure.
