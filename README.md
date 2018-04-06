# Auto Snapshotor

Figure out which apps need to snapshot.


### Prerequisites

Maven 3.0+ is required for maven dependency resolve.

### Installation 
Open a new terminal and run the following command: 
```
curl -sL https://raw.githubusercontent.com/mbrtargeting/auto-snapshotor/master/tools/install.sh | bash
```

### Usage
Run command `as` from maven project's directory. Example usage: 
```
work && as
``` 

### Test/Lint

See [JUnit](https://junit.org/junit5), [AssertJ](http://joel-costigliola.github.io/assertj/index.html) and [Ktlint](https://github.com/shyiko/ktlint)

```
mvn verify
```

## Tasks

### Changes Collection
- [x] Modification from Git branches
- [x] Add, Remove and Rename from Git branches
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
- [x] List of Apps to snapshot
- [x] Detailed reason why an app needs to snapshot. File changes/Lib changes

### Methods to run
- [x] Main method
- [ ] Command line
- [ ] Jenkins

### Technical
- [x] Lint
- [ ] EditorConfig

## Built With

* [Kotlin](https://kotlinlang.org/)
* [Maven](https://maven.apache.org/)
* [Ktlint](https://github.com/shyiko/ktlint)
* [JUnit](https://junit.org/junit5) 
* [AssertJ](http://joel-costigliola.github.io/assertj/index.html)
