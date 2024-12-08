# Logical Expression Parser Repository Documentation

## Overview
This repository contains an implementation of a parser for logical expressions. The key components are:

- **Scanner**: Tokenizes logical expressions.
- **Parser**: Constructs a parse tree from tokens based on defined grammar.
- **Node**: Represents tree nodes for logical expressions.
- **Token**: Encapsulates tokens with their types and values.
- **Main**: Entry point for demonstrating parsing functionality.
- **TreePrinter**: Utility for printing the parse tree.

---

## Project Structure

```
.
├── Main.java
├── Scanner.java
├── Parser.java
├── Node.java
├── Token.java
└── TreePrinter.java
```

---

## Components

### 1. **Scanner**
**File**: `Scanner.java`

#### Description
The `Scanner` class tokenizes logical expressions into manageable components for parsing.

#### Methods
- **`getTokens()`**: Returns a list of tokenized logical expressions. Example expressions include:
  - `~P ∨ Q`
  - `P ∧ Q`
  - `(P ∨ Q) ∧ ~R`

#### Example Usage
```java
Scanner scanner = new Scanner();
List<List<Token>> tokens = scanner.getTokens();
```

---

### 2. **Parser**
**File**: `Parser.java`

#### Description
The `Parser` class builds a parse tree for logical expressions based on grammar rules.

#### Methods
- **`parseSentence(List<Token> tokens)`**: Constructs a parse tree for a given sentence.
- **`parseAtomicSentence(List<Token> tokens)`**: Parses atomic sentences (e.g., `TRUE`, `FALSE`, `P`).
- **`parseComplexSentence(List<Token> tokens)`**: Handles parentheses, negations, and connectives.
- **`parseConnective(Token token)`**: Parses connectives like `AND`, `OR`, `IMPLIES`, and `EQUIVALENT`.

#### Example Grammar Rules
- `<Sentence> ::= <AtomicSentence> | <ComplexSentence>`
- `<AtomicSentence> ::= "TRUE" | "FALSE" | "P" | "Q" | "S"`
- `<ComplexSentence> ::= "(" <Sentence> ")" | NOT <Sentence> | <Sentence> <Connective> <Sentence>`
- `<Connective> ::= AND | OR | IMPLIES | EQUIVALENT`

#### Example Usage
```java
Parser parser = new Parser();
Node root = parser.parseSentence(tokens.get(0));
```

---

### 3. **Node**
**File**: `Node.java`

#### Description
The `Node` class represents individual elements of the parse tree.

#### Fields
- **`value`**: The value of the node (e.g., "P", "NOT").
- **`children`**: A list of child nodes.

#### Methods
- **`getValue()`**: Returns the node's value.
- **`insertChild(Node child)`**: Adds a child node to the current node.

#### Example Usage
```java
Node root = new Node("<Sentence>");
Node child = new Node("P");
root.insertChild(child);
```

---

### 4. **Token**
**File**: `Token.java`

#### Description
The `Token` class encapsulates tokens with their types and values.

#### Fields
- **`tokenType`**: Type of the token (e.g., "Connective", "Atomic").
- **`value`**: Value of the token (e.g., "AND", "P").

#### Methods
- **`getTokenType()`**: Returns the token type.
- **`getValue()`**: Returns the token value.
- **`toString()`**: Provides a string representation of the token.

#### Example Usage
```java
Token token = new Token("Atomic", "P");
System.out.println(token.getValue());
```

---

### 5. **Main**
**File**: `Main.java`

#### Description
The `Main` class demonstrates the process of tokenizing and parsing logical expressions.

#### Workflow
1. Instantiate a `Scanner` and retrieve tokens.
2. Parse a selected logical expression using the `Parser`.
3. Print the resulting parse tree using `TreePrinter`.

#### Example Code
```java
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner();
        List<List<Token>> tokens = scanner.getTokens();
        List<Token> expressionTokens = tokens.get(1); // Second logical expression

        Parser parser = new Parser();
        Node root = parser.parseSentence(expressionTokens);

        TreePrinter.printTree(root);
    }
}
```

---

### 6. **TreePrinter**
**File**: `TreePrinter.java`

#### Description
The `TreePrinter` class provides functionality to visually display the parse tree.

#### Method
- **`printTree(Node root)`**: Prints the parse tree with horizontal lines.

#### Example Output
For the logical expression `(P ∨ Q) ∧ ~R`, the parse tree might look like:
```
<Sentence>
|-- <Complex Sentence>
    |-- (
    |-- <Sentence>
        |-- <Atomic Sentence>
            |-- P
        |-- <Connective>
            |-- OR
        |-- <Atomic Sentence>
            |-- Q
    |-- )
|-- <Connective>
    |-- AND
|-- <Sentence>
    |-- <Complex Sentence>
        |-- NOT
        |-- <Atomic Sentence>
            |-- R
```

#### Example Usage
```java
TreePrinter.printTree(root);
```

---

## Running the Project

1. **Compile** all `.java` files:
   ```sh
   javac *.java
   ```

2. **Run** the `Main` class:
   ```sh
   java Main
   ```

---

## Notes
- Ensure that all dependencies (e.g., `java.util.List`, `java.util.ArrayList`) are properly imported.
- The grammar and tokenization rules should be consistent with the logic implemented in `Scanner` and `Parser`.

---