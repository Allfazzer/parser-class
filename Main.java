import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create a Scanner for user input
        Scanner inputScanner = new Scanner(System.in); 

        // Get user input (use 'sentence.pl' or enter expression)
        String useFile;
        do {
            System.out.println("Do you want to use 'sentence.pl' (y/n)?");
            useFile = inputScanner.nextLine();
        } while (!useFile.equalsIgnoreCase("y") && !useFile.equalsIgnoreCase("n"));

        if (useFile.equalsIgnoreCase("y")) {
            // Read expressions from 'sentence.pl'
            try (BufferedReader br = new BufferedReader(new FileReader("sentence.pl"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    processExpression(line); 
                }
            } catch (IOException e) {
                System.err.println("Error reading from file: " + e.getMessage());
            }
        } else {
            // Get expression from user input
            System.out.print("Enter the expression: ");
            String expression = inputScanner.nextLine();
            processExpression(expression); 
        }
    }

    private static void processExpression(String line) {
        // Convert the expression to uppercase for case-insensitive processing
        String uppercaseExpression = line.toUpperCase(); 

        System.out.println("Expression: " + uppercaseExpression); 

        // Initialize a Lexer to tokenize the expression
        Lexer scanner = new Lexer(uppercaseExpression); 

        // Tokenize the input expression
        List<Token> tokens = scanner.tokenize(); 

        // Initialize a Parser to parse the tokens into an abstract syntax tree (AST)
        Parser parser = new Parser();

        try {
            // Parse the tokens and create the AST
            Node root = parser.parseSentence(tokens);

            // Initialize an Evaluator to evaluate the truth values of the expression
            Evaluator evaluator = new Evaluator();

            // Generate and print the truth table for the expression
            evaluator.generateTruthTable(root);

        } catch (ParserError e) {
            // Print parsing error messages
            System.out.println("Error parsing: " + uppercaseExpression + " - " + e.getMessage()); 
        }
    }
}