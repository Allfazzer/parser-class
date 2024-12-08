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

        // Return an Atomic Sentence subtree if the tokens are the RHS of the production rule: 
        // <Sentence> := <AtomicSentence> 
        Node atomicSentenceNode = this.parseAtomicSentence();

        // Return the root
        return root;
    }

    // Return a Node containing the token if its the RHS of the production rule: 
    // <AtomicSentence> := “TRUE” | “FALSE” | “P” | “Q’ | “S”
    public Node parseAtomicSentence() {
        // Atomic Sentence should be composed of 1 token only
        if (tokens.size() == 1) {
            // Create the Atomic Sentence Node
            Node atomicSentenceNode = new Node("<Atomic Sentence>");

            // Create the child node of the Atomic Sentence
            String currentTokenValue = tokens.getFirst().getValue();
            Node currentNode = new Node(currentTokenValue);
            atomicSentenceNode.insertChild(currentNode);

            // Remove the current token from the list of Tokens
            consume();

            return atomicSentenceNode;
        } else {
            return null;
        }
    }

    // Return a <Complex Sentence> node if the tokens matches either one of the RHS of the production rule: 
    // <Complex Sentence> := “(“ <Sentence> “)” | <Sentence> <Connective> <Sentence> | “NOT” <Sentence>
    public Node parseComplexSentence() {
        // Create the Complex Sentence Node
        Node complexSentenceNode = new Node("<Complex Sentence>");

        return complexSentenceNode;
    }
    

    // Utility method to  the current token and advance to the next
    private void consume() {
        this.tokens = new ArrayList<>(tokens).subList(1, tokens.size());
    }
}
