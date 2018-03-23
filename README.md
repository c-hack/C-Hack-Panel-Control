# C-Hack-Panel-Control
Control Software for the C-Hack Panels

The C-Hack Panels are a set of LED-Panels. See [c-hack](http://c-hack.de/projekte/c-hack-panel/).  
The aim of the software is to send the right bytes over serial to the panels at ther right time, 
so the panels dispaly the intended animation.  
This new software was made to replace the old Sequencer.

## Requirements
To run releases: Java 1.8 JRE  
To compile and run from source:  Java 1.8 JDK, [Maven](https://maven.apache.org/index.html) and [TimLib](https://github.com/neumantm/TimLib)

## Building and running
To build it run `mvn package`. This will create a jar in `target`.  
To execute run `java -jar path/to/C-Hack-Panel-Control-X.X.X-jar-with-dependencies.jar <name of snippet> <arguments for snippet>`.  

## How to use
### Testing locally:
If you want to test locally, you can see what the panels would show, by just opening the file `sim/index.html` with a browser.(Tested in Firefox 59)

## Contributing
This project was created with eclipse, therefore you can just import the whole repo as a eclipse project.

To contribute animations just copy the `GenericSnippet` in the package `animationSnippets` and start animating.  
To register the snippet with the application just add the respective line into `LoadedSnippets`.

But also contributions to the API, assets or documentation on how to create Snippets are welcome.
