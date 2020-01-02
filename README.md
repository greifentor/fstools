# fstools
A tool box for file system operations.


## Requirements

Java 8, Maven 3.


## Build

Build the project via `mvn clean install`.


## Components

### FileSystemTreeTraversal

This is a class which is able to traverse a file system tree starting from a root folder. Via using the
`FileFoundListener` or the `DirectoryFoundListener` information about the files and sub directories below the
root could be gained.
