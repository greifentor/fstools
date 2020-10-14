# fstools
A tool box for file system operations.


## Requirements

Java 8, Maven 3.


## Build

Build the project via `mvn clean install`.


## Components

### Shell

The shell component allows to call some methods of the fstools but is rather created to start a mirror process. The 
mirror process is able to mirror a source directory to a target directory.

To start the shell component type

`java -jar shell/target/fstools-shell-1.1.0.jar`

in a operating system shell (in the project folder). Take care to type the correct version number of the fstools.

In the fstools shell type `help` to get an overview of the commands. Type `help` followed by a specific command (e. g.
`help mirror`) to get a specific help for the command.

### FileSystemTreeTraversal

This is a class which is able to traverse a file system tree starting from a root folder. Via using the
`FileFoundListener` or the `DirectoryFoundListener` information about the files and sub directories below the
root could be gained.
