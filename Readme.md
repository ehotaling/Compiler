# BASIC Language Interpreter

## Project Overview

This project is a comprehensive interpreter for a subset of the BASIC (Beginner's All-Purpose Symbolic Instruction Code) programming language. It includes a lexer, a parser, and an interpreter, capable of processing BASIC code from a file or in an interactive mode. The interpreter supports various BASIC language features including variable assignments, control flow (FOR/NEXT, IF/THEN, WHILE, GOSUB/RETURN, GOTO), input/output operations (PRINT, READ, INPUT, DATA), and a range of built-in functions.

This project was developed incrementally, following a series of assignments that built upon each other:
* **Assignment 1: The Lexer**: Focused on tokenizing the BASIC source code.
* **Assignment 2: Enhance the Lexer**: Added more token types, including keywords and symbols, using HashMaps for efficient lookup.
* **Assignment 3: Start the Parser**: Began the process of transforming the token list into an Abstract Syntax Tree (AST), focusing on order of operations for mathematical expressions (Expression-Term-Factor pattern).
* **Assignment 4: Expand the Parser**: Introduced more AST nodes for statements like PRINT, assignment, and variable handling.
* **Assignment 5: Even More in the Parser**: Added support for string literals and statements like READ, DATA, and INPUT.
* **Assignment 6: Finish the Parser**: Completed the parser by adding support for labels, GOSUB/RETURN, FOR/NEXT, IF, WHILE, END, and function invocations.
* **Assignment 7: Start the Interpreter**: Began building the interpreter, focusing on pre-processing optimizations like handling DATA statements and labels, and implementing built-in functions.
* **Assignment 8: Continue the Interpreter**: Implemented the evaluation logic for expressions (including mathematical operations and function calls) and started interpreting statement nodes like READ, ASSIGN, INPUT, and PRINT.
* **Assignment 9: Finish the Interpreter**: Completed the interpreter by adding a 'Next' member to StatementNode for proper execution flow, implementing the remaining statement interpretations (IF, GOSUB, RETURN, FOR, NEXT, END), and setting up the main interpreter loop.

---
## Features

* **Lexical Analysis**: Converts BASIC source code into a stream of tokens.
    * Recognizes keywords, identifiers (variables), numbers (integer and float), string literals, and symbols.
    * Handles comments and whitespace.
    * Tracks line numbers and character positions for tokens.
* **Parsing**: Builds an Abstract Syntax Tree (AST) from the token stream.
    * Implements Expression-Term-Factor pattern for correct order of operations.
    * Supports a variety of statement types including assignments, print statements, control flow, and data manipulation.
    * Creates heterogeneous n-ary tree nodes.
* **Interpretation**: Executes the BASIC program represented by the AST.
    * Manages variable storage (integers, floats, strings).
    * Handles control flow statements like `IF-THEN`, `FOR-NEXT`, `WHILE`, `GOSUB-RETURN`, and `GOTO`.
    * Supports `READ`/`DATA` for program data.
    * Implements `INPUT` for user interaction.
    * Provides `PRINT` for outputting values.
    * Includes built-in functions: `RANDOM`, `LEFT$`, `RIGHT$`, `MID$`, `NUM$`, `VAL`, `VAL%`.
* **Error Handling**: Provides error messages for syntax errors and runtime issues.
* **Interactive Mode**: Allows users to type and run BASIC commands directly in the interpreter.
    * Commands: `RUN`, `HELP`, `EXIT`, `NEW`, `OUTPUT`, `SAVE [file]`, `LOAD [file]`.
* **Debug Mode**: Offers a debug flag (`-d` or `-debug`) to print tokens and the AST for inspection.

---
## BASIC Language Dialect Supported

### Data Types
* Variables are typed by their ending character:
    * `$` for **string** (e.g., `myString$`)
    * `%` for **float** (e.g., `percentOfPeople%`)
    * Any other ending for **integer** (e.g., `count`)
* Simple variables do not need to be pre-defined.
* All variables are global.

### Structure
* Programs are a list of commands.
* **Labels** are used for line referencing (e.g., `myLabel:`). A label must be the first item on a line.

### Statements and Keywords
* **PRINT**: Outputs values to the console. Can print multiple comma-separated values.
    * `PRINT expression1, expression2, "string_literal", ...`
* **READ**: Reads values from `DATA` statements into variables.
    * `READ var1, var2$, var3%`
* **INPUT**: Prompts the user for input and assigns it to variables.
    * `INPUT "Prompt string", var1$, var2`
* **DATA**: Defines a list of constant data (strings, integers, floats) to be used by `READ`.
    * `DATA "string", 123, 45.67`
* **GOSUB**: Transfers control to a subroutine identified by a label.
    * `GOSUB subroutineLabel`
* **RETURN**: Returns control from a subroutine to the statement after the `GOSUB` call.
* **GOTO**: Unconditionally transfers control to a line identified by a label.
    * `GOTO targetLabel`
* **FOR...TO...STEP...NEXT**: Creates a loop.
    * `FOR variable = initialValue TO limit [STEP increment]`
    * `...statements...`
    * `NEXT variable`
    * The `STEP` part is optional; if omitted, the increment is 1.
* **IF...THEN**: Conditional execution. If the expression is true, control jumps to the specified label.
    * `IF boolean_expression THEN label`
    * Boolean expressions are of the form: `Expression operator Expression`, where operator can be `>`, `>=`, `<`, `<=`, `<>`, `=`.
* **WHILE...label**: Loops through statements until an expression is false. The loop body is defined up to the specified end label.
    * `WHILE boolean_expression endLabelName`
    * `...statements...`
    * `endLabelName:`
* **END**: Terminates program execution.
* **Assignment**: Assigns a value to a variable.
    * `variable = expression` (e.g., `C = 5*(F-32)/9`)

### Built-in Functions
* **RANDOM()**: Returns a random integer.
* **LEFT$(string, int)**: Returns the leftmost N characters from the string.
* **RIGHT$(string, int)**: Returns the rightmost N characters from the string.
* **MID$(string, int, int)**: Returns a substring starting from the second argument's position for a length specified by the third argument (e.g., `MID$("Albany",2,3)` results in "ban").
* **NUM$(int or float)**: Converts a number to its string representation.
* **VAL(string)**: Converts a string to an integer.
* **VAL%(string)**: Converts a string to a float.

---
## Prerequisites

* Java 1.8 or higher (Project `pom.xml` specifies Java 11, but `README.md` mentions Java 1.8). It's best to use Java 11 as per `pom.xml` configuration.
    * The `pom.xml` has `<maven.compiler.source>1.8</maven.compiler.source>` and `<maven.compiler.target>1.8</maven.compiler.target>` under properties, but the `maven-compiler-plugin` configuration specifies source and target as 11.
* Maven 3.8.x or later.

---
## Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd <repository-directory>
    ```
2.  **Build the project using Maven:**
    ```bash
    mvn clean install -U
    mvn package
    ```
    This will compile the source code, run tests, and create a JAR file in the `target/` directory (e.g., `basic-app-1.0.jar`).

### IntelliJ IDEA Setup (Recommended)
1.  Open IntelliJ IDEA.
2.  Click "Open", navigate to the project root directory, select the `pom.xml` file, then click "Open as Project".
3.  Ensure IntelliJ uses the correct Maven version:
    * Click "View" -> "Tool Windows" -> "Maven".
    * Open Maven Settings for your project.
    * Ensure you are using "Bundled (Maven 3)" under "Maven home path" or point to your local Maven 3.8.x+ installation. This will help use the correct Maven version if your system-level Maven is different.

---
## Usage

### Running a BASIC file:
Execute the compiled JAR file, passing the BASIC file name as an argument:
```bash
java -jar target/basic-app-1.0.jar "your_basic_file.bas"
```

### Interactive Mode:
Run the interpreter in interactive mode:
```bash
java -jar target/basic-app-1.0.jar -interactive
```
or
``` bash
java -jar target/basic-app-1.0.jar -i
```
This will start the SimpleBASIC interpreter. You can type BASIC commands directly.

#### Interactive Mode Commands:
* **HELP**: Print help options.
* **RUN**: Compile and run the current program entered in the interactive session.
* **NEW**: Start a new program, discarding the current one.
* **OUTPUT**: Output the current program.
* **SAVE**: Save the current program to output.txt in the current directory.
* **SAVE [file]**: Save the current program to the specified file in the current directory.
* **LOAD [file]**: Load a BASIC program from the specified file.
* **EXIT**: Quit the interpreter.

### Debug Mode:

To see the token stream and the Abstract Syntax Tree (AST) during execution, use the debug flag:
``` bash
java -jar target/basic-app-1.0.jar "your_basic_file.bas" -debug
```
or
``` bash
java -jar target/basic-app-1.0.jar "your_basic_file.bas" -d
```

## Project Structure

### `lexer/`: Lexical Analysis
Contains classes responsible for lexical analysis.

- **`CodeHandler.java`**: Manages reading and navigating the source code character by character.
- **`Lexer.java`**: The main lexer class that tokenizes the input source code.
- **`Token.java`**: Represents a token with its type, value (optional), line number, and position.
- **`TokenManager.java`**: Manages the stream of tokens produced by the Lexer, providing peek and match-and-remove functionalities for the Parser.

### `parser/`: Parsing
Contains classes responsible for parsing the token stream into an AST.

- **`Parser.java`**: The main parser class. Implements recursive descent parsing, including the Expression-Term-Factor pattern.

### `node/`: Abstract Syntax Tree (AST) Nodes
Contains classes defining the nodes of the Abstract Syntax Tree.

- **`Node.java`**: Abstract base class for all AST nodes.
- **`ProgramNode.java`**: Root node of the AST, holding a list of statements or expressions.
- **`StatementNode.java`**: Abstract base class for all statement nodes (e.g., `PrintNode`, `AssignmentNode`). Has a `next` pointer to link statements for sequential execution.
- **`ExpressionNode.java`, `TermNode.java`, `FactorNode.java`**: Nodes representing parts of mathematical expressions.
- Specific nodes for each language construct:
  - `IntegerNode`, `FloatNode`, `StringNode`, `VariableNode`, `MathOpNode`
  - `PrintNode`, `AssignmentNode`, `IfNode`, `ForNode`, `WhileNode`, `GosubNode`
  - `ReturnNode`, `DataNode`, `ReadNode`, `InputNode`, `FunctionNode`
  - `LabeledStatementNode`, `EndNode`, etc.
- **`InterpreterDataType.java`**: Enum for variable types (`STRING`, `INTEGER`, `FLOAT`).
- **`StatementVisitor.java`**: Interface for the visitor pattern used by the Interpreter for pre-processing and statement execution.
- **`BuiltInFunctions.java`**: Static methods for BASIC built-in functions.

### `interpreter/`: AST Execution
Contains the class responsible for executing the AST.

- **`Interpreter.java`**: Walks the AST and executes the BASIC program. Manages variable state, control flow, and I/O. Implements `StatementVisitor`.

### `main/java/Basic.java`
The main class with the `main` method, serving as the entry point for the application. Handles command-line arguments and orchestrates the lexing, parsing, and interpretation processes.

### `test/java/`: Unit Tests
Contains JUnit tests for various components.

- `LexerTest.java`, `ParserTest.java`, `InterpreterTest.java`
- `CodeHandlerTest.java`, `BuiltInFunctionsTest.java`, `TokenTest.java`

### `test/resources/`
Contains sample BASIC files and other resources used for testing.

### `pom.xml`
Maven project configuration file, defining dependencies (JUnit Jupiter for testing), build plugins, and project metadata.

### `src/instructions/`
Contains documents detailing the requirements for each assignment, which collectively define the features and evolution of the interpreter.

## Testing

The project includes a suite of JUnit tests to ensure the correctness of the lexer, parser, and interpreter components.

### Lexer Tests (`LexerTest.java`)
- Verify tokenization of various inputs, including:
  - Keywords
  - Identifiers
  - Numbers
  - String literals (with escapes and multi-lines)
  - Symbols
  - Labels
  - Comments

### CodeHandler Tests (`CodeHandlerTest.java`)
- Test the functionality of the `CodeHandler` for reading and navigating source code.

### Parser Tests (`ParserTest.java`)
- Validate AST construction for different expressions and statements.
- Ensure proper order of operations is respected in expression parsing.

### Interpreter Tests (`InterpreterTest.java`)
- Test data and label processing.
- Validate variable storage and retrieval across data types.
- Test mathematical operations and function calls.
- Evaluate `INPUT` and `PRINT` statements (including a test mode for automated I/O).
- Test control flow statements: `IF`, `FOR`, `WHILE`, `GOSUB`, `GOTO`.
- Test `READ`/`DATA` functionality.

### Built-In Function Tests (`BuiltInFunctionsTest.java`)
- Test each of the built-in BASIC functions for correctness.

### Token Tests (`TokenTest.java`)
- Validate `Token` class methods, including `toString()`.

### Running the Tests

Use the following Maven command:

```bash
mvn test
``` 

## Future Enhancements

While the project fulfills the specified requirements, potential future enhancements could include:

- More extensive error recovery in the parser.
- Support for user-defined functions and subroutines with parameters and local variables.
- Additional data structures like arrays.
- File I/O operations beyond `INPUT` and `PRINT`.
- A richer set of built-in functions.
- Graphical User Interface (GUI).
- More sophisticated debugging capabilities.
