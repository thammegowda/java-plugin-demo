## Plugin System Demo
This project demonstrate how a simple plugin based apps can be built using
java packaging, maven build system and runtime class loading.

## Structure
This is a multi module maven project
+ `sdk` : defines interfaces for plugin
+  `plugin-src` : contains demo plugin sources
+ `plugin-app` : an example app built using plugins

## How to test
+ Step 1 : build and install sdk locally. Goto `sdk` directory and run `mvn clean install`
+ Step 2 : Package the plugins : Goto `plugin-src` directory and run `mvn clean package`
+ Step 3 : Copy plugin jars to plugin repository at `plugin-app/plugins`. You can run command `cp -v plugin-src/*/target/*.jar plugin-app/plugins/`
+ Step 4 : Run the Plugin app. goto `plugin-app` and run ` mvn compile assembly:single` followed by ` java -jar target/plugin-app-1.0-SNAPSHOT-jar-with-dependencies.jar `
