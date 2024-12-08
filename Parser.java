import java.util.List;

public class Parser {
    // The tokens to be processed by the parser
    private List <Token> tokens;

    // Constructor
    public Parser(List <Token> tokens) {
        this.tokens = tokens;
    }

    // Parse sentence
    public Node parseSentence() {
        // Create Node for the <Sentence> which will be the root of the tree
        Node root = new Node("<Sentence>");

        // Return the 
        return root;
    }
}
