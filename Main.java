import java.util.List;

public class Main { 
    public static void main(String[] args) {
        // Initialize a Scanner
        Scanner scanner =  new Scanner();

        // Retrieve the list of token tables from the scanner
        List <List<Token>> tokenTables = scanner.getTokens();

        // Retrieve an arbitrary token table from the tokens
        List <Token> tokens = tokenTables.get(1);

        // Initialize a Parser
        Parser parser = new Parser();

        // Parse the tokens
        Node root = parser.parseSentence(tokens);

        // Print the tree
        TreePrinter.printTree(root);
    }
}