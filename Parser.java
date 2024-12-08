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

    // Utility method to check whether the entire tokens inside an Array List are enclosed in parenthesis
    public boolean areTokensEnclosed() {
        // Get the first token
        Token firstToken = tokens.get(0);
        
        // First token should be an open parenthesis
        if (firstToken.getValue() == "(") {
            // Ensure that the corresponding closed parenthesis is at the end of the tokens array

            // Start with the token after the open parenthesis
            int currentTokenIndex = 1; 

            // Variable to track whether all encountered left parenthesis have their corresponding closed parenthesis
            int numberOfClosedParenthesisToFind = 1;

            // Start traversing the tokens array
            while(currentTokenIndex != tokens.size()) {
                // Get curent token
                Token currentToken = tokens.get(currentTokenIndex);

                // Another open parenthesis was found
                if (currentToken.getValue() == "(") {
                    numberOfClosedParenthesisToFind++;
                } // An enclosing parenthesis was found
                else if (tokens.get(currentTokenIndex).getValue() == ")") {
                    numberOfClosedParenthesisToFind--;
                }

                // End loop if the outermost closed parenthesis is located
                if (numberOfClosedParenthesisToFind == 0) {
                    return true;
                } 

                // Move to the next token
                currentTokenIndex++;
            } 
        } 

        return false;
    }

    // Utility method to find the index of the main connective of the sentence
    public int getIndexOfMainConnective() {
        // Checker whether current token is inside a parenthesis
        boolean isOutsideParenthesis = true;

        // Find the connective that is not inside a parenthesis
        for (int index = 0; index < tokens.size(); index++) {
            // Get current token value
            String currentTokenValue = tokens.get(index).getValue();

            // Parenthesis encountered
            if (currentTokenValue == "(") {
                isOutsideParenthesis = false;
            } 
            // Parenthesis exited
            else if (currentTokenValue == ")"){
                isOutsideParenthesis = true;
            }
            // Tokens that are not parenthesis
            else if (this.isConnective(currentTokenValue) && isOutsideParenthesis) {
                return index;
            }
        }

        // Return -1 index if no main connective is found
        return -1;
    }

    // Utility method to check whether the given string is a connective
    public boolean isConnective(String value) {
        return value == "AND" || value == "OR" || value == "IMPLIES" || value == "EQUIVALENT";
    }
}
