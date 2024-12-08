import java.util.ArrayList;
import java.util.List;

public class Parser {
    // The tokens to be processed by the parser
    private List <Token> tokens;

    // Constructor
    public Parser(List <Token> tokens) {
        this.tokens = tokens;
    }

    // Parse the tokenized sentence
    public Node parseSentence() {
        // Create Node for the <Sentence> which will be the root of the tree
        Node root = new Node("<Sentence>");

        // Return the root
        return root;
    }

    // Utility method to  the current token and advance to the next
    private void consume() {
        this.tokens = new ArrayList<>(tokens).subList(1, tokens.size());
    }
}
