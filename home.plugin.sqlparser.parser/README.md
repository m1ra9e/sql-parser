[![Apache 2.0](https://img.shields.io/github/license/m1ra9e/sql-parser.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# sql-parser

The part of Eclipse plugin to parse sql-file with simple SELECT-query.

Includes:

- parser core

- simple cli (temporary)

## Build

Build requires Java (JDK) 17+ and Apache Maven 3.8+.

```sh
git clone https://github.com/m1ra9e/sql-parser.git sql-parser
cd sql-parser
mvn clean package
```

## Run

For run the application

- output to console:

```sh
java -jar sql-parser.jar file_path_for_parse.sql
```

- output to file:

```sh
java -jar sql-parser.jar file_path_for_parse.sql > result.txt
```

## Configure auto-generation of parser classes

In pom.xml already configured source directory with parser rules

``` 
<sourceDirectory>${project.basedir}/antlr-src</sourceDirectory>
```

and directory for generated parser classes

```
<outputDirectory>${project.build.directory}/generated-sources/antlr4/home/plugin/sqlparser/parser/antlr/generated</outputDirectory>
```

For auto-generation of parser classes need to enable the auto-build in Eclipse:
`Eclipse menu -> Project -> Build Automatically`

For ease of development, need to add directory `target/generated-sources/antlr4` as a resource folder:
`Project menu -> Build Path -> Configure Build Path... -> Tab 'Source'`

<details>

  <summary>Alternative way 1: generate parser classes via ant</summary>

  Run antlr-src/alternative-parser-class-generator/generator.xml

  ```sh
  # in antlr-src/alternative-parser-class-generator
  ant -buildfile generator.xml
  ```

</details>

<details>

  <summary>Alternative way 2: generate parser classes via Antlr-Tool plugin (only for old Eclipse versions)</summary>

  If used the Antlr-Tool plugin, you need to configure auto-generation of parser classes in g4-file run args:

  1. right click on antlr-src/SQL.g4 -> Generate ANTLR Recognizer

  2. right click on antlr-src/SQL.g4 -> Run as -> External Tools Configurations

  3. select ANTLR->SQL.g4

  4. Tab 'Tools' -> fill Arguments:

  ```
  -listener -no-visitor -encoding UTF-8 -o src/main/java/home/plugin/sqlparser/parser/antlr/generated
  ```

</details>
