# C-Hack-Panel-Control
Control Software for the C-Hack Panels

This new software was made to replace the Sequencer.
To build it run `mvn package`. 
To execute run `java -jar target/C-Hack-Panel-Control-X.X.X.jar <name of snippet> <arguments for snippet>`.

To contribute animations just copy the `GenericSnippet` in the package `animationSnippets` and start animating.
To register the snippet with the application just add the respective line into `LoadedSnippets`.
