import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("sentence.pl"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Expression: " + line); 

                // Initialize a Scanner
                Scanner scanner = new Scanner(line); 

                // Tokenize the input 
                List<Token> tokens = scanner.tokenize(); 

                // Initialize a Parser
                Parser parser = new Parser();

                try {
                    // Parse the tokens
                    Node root = parser.parseSentence(tokens);

                    // Initialize an Evaluator
                    Evaluator evaluator = new Evaluator();

                    // Generate and print the truth table
                    evaluator.generateTruthTable(root);

                } catch (ParserError e) {
                    // Print parsing related error messages
                    System.out.println("Error parsing: " + line + " - " + e.getMessage()); 
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }
}