import java.util.List;

public class Main { 
    public static void main(String[] args) {
        // Initialize a Scanner
        Scanner scanner =  new Scanner();

        // Retrieve the list of token tables from the scanner
        List <List<Token>> tokenTables = scanner.getTokens();

        // Retrieve an arbitrary token table from the tokens
        List <Token> tokens = tokenTables.get(5);

        // Initialize a Parser
        Parser parser = new Parser();

        try {
            // Parse the tokens
            Node root;

            // Get the root of the parse tree
            root = parser.parseSentence(tokens);

            // Print the tree
            TreePrinter.printTree(root);

        } catch (ParserError e) {
            // Print parsing related error messages
            System.out.println(e.getMessage());
        }
    }
}