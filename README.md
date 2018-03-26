# Auto Snapshotor

Figure out which apps need to snapshot automatically


### Prerequisites

Maven 3.0+ is required for maven dependency resolve.

## Usage

For now, the only way to make use of the project is to run with the main method. Other ways will be added shortly. 
Contribution are welcomed.

Clone the project to the same folder of your ssp-core. By default, it will try to search for a folder named "ssp-core",
 change it to your local setup. Please check the error message if failed.

Run the main will report apps need to snapshot between your current branch and master. 


## Running the tests

```
mvn test
```


### Coding Style

This project is following the Kotlin official coding style. 
https://kotlinlang.org/docs/reference/coding-conventions.html

## Tasks

### Changes Collection
- [x] Modification from Git branches
- [ ] Add, Remove and Rename from Git branches
- [ ] Changes from Git working dir.
- [ ] Other ways to define changes? 
- [ ] More Git Unit Testing

### Dependency Resolving with Maven
- [x] Recognize Maven Project/Library
- [x] Resolve Maven dependencies
- [x] Offline Mode. All dependencies need to be installed locally
- [ ] Online Mode

### Dependency Resolving with other ways
- [ ] JSON config?
- [ ] Dependency resolving plugins?  

### Reporter
- [x] Apps to snapshot
- [ ] Detailed reason why an app needs to snapshot. File changes/Lib changes

### Methods to run
- [x] Main method
- [ ] Command line
- [ ] Jenkins

## Built With

* [Kotlin](https://kotlinlang.org/)
* [Maven](https://maven.apache.org/)
* [Kotlintest](https://github.com/kotlintest/kotlintest)
