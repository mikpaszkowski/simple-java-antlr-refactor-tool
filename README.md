# Simple Java Refactoring Tool

Project relates to simplify the refactoring of *.java source files with simple transformations. User is able to run the application in a CLI mode or by running a JAR file with a proper set of arguments. 
## Features

- Renaming methods
- Renaming classes
- Moving methods between classes

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later

### Installation

1. Clone the repository:
   git clone https://github.com/your-username/project-name.git

2. Build the project:
```agsl
cd simple-java-antlr-refactor-tool
./gradlew build
```

### Usage

To run the project, use the following command:


#### Options

- `--help`: Check the details of how to use the application through running a JAR file.
- `--CLI`: Run the application in CLI (Command-Line Interface) mode.
- `--transformType="<type>"`: Specify the transformation type. Available types: `renameMethod`, `renameClass`, `moveMethod`.
- `--fileName="<path>"`: Specify the input Java file path.
- `--sourceClass="<name>"`: Specify the source class name.
- `--targetClass="<name>"`: Specify the target class name (for `moveMethod` transformation).
- `--methodName="<name>"`: Specify the method name.
- `--newMethodName="<name>"`: Specify the new method name (for `renameMethod` transformation).
- `--newClassName="<name>"`: Specify the new class name (for `renameClass` transformation).

Example usage:

```agsl
java -jar <target_jar_name>-jar-with-dependencies.jar --transformType=renameMethod --fileName=path/to/file.java --sourceClass=ClassName --methodName=methodName --newMethodName=newMethodName
```

```agsl
java -jar <target_jar_name>-jar-with-dependencies.jar --CLI
```


### Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please create a new issue or submit a pull request.

### License

This project is licensed under the [MIT License](LICENSE).
