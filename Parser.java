import java.util.ArrayList;
import java.util.List;

public class Parser {
    // Constructor
    public Parser() {
    }

    // Parse the tokenized sentence
    public Node parseSentence(List <Token> tokens) {
        // Create Node for the <Sentence> which will be the root of the tree
        Node root = new Node("<Sentence>");

        // Return an Atomic Sentence subtree if the tokens are the RHS of the production rule: 
        // <Sentence> := <AtomicSentence> 
        Node atomicSentenceNode = this.parseAtomicSentence(tokens);

        // Try the <Complex Sentence> if no <Atomic Sentence> node was returned
        if (atomicSentenceNode == null) {
            // Return a Complex Sentence subtree if the tokens are the RHS of the production rule: 
            // <Sentence> := <Complex Sentence> 
            Node complexSentenceNode = this.parseComplexSentence(tokens);
            // Insert the <Complex Sentence> node into the root
            root.insertChild(complexSentenceNode);
        }   
        // An <Atomic Sentence> node was returned
        else {
            // Insert the <Atomic Sentence> node into the root
            root.insertChild(atomicSentenceNode);
        }

        // Return the root
        return root;
    }

    // Return a Node containing the token if its the RHS of the production rule: 
    // <AtomicSentence> := “TRUE” | “FALSE” | “P” | “Q’ | “S”
    public Node parseAtomicSentence(List <Token> tokens) {
        // Atomic Sentence should be composed of 1 token only
        if (tokens.size() == 1) {
            // Create the Atomic Sentence Node
            Node atomicSentenceNode = new Node("<Atomic Sentence>");

            // Create the child node of the Atomic Sentence
            String currentTokenValue = tokens.getFirst().getValue();
            Node currentNode = new Node(currentTokenValue);
            atomicSentenceNode.insertChild(currentNode);

            return atomicSentenceNode;
        } else {
            return null;
        }
    }

    // Return a <Complex Sentence> node if the tokens matches either one of the RHS of the production rule: 
    // <Complex Sentence> := “(“ <Sentence> “)” | <Sentence> <Connective> <Sentence> | “NOT” <Sentence>
    public Node parseComplexSentence(List <Token> tokens) {
        // Create the Complex Sentence Node
        Node complexSentenceNode = new Node("<Complex Sentence>");

        // RHS is in the form “(“ <Sentence> “)”
        if (areTokensEnclosed(tokens)) {
            // Create node for the open parenthesis
            Node openParenthesisNode = new Node("(");

            // Create node for the closed parenthesis
            Node closedParenthesisNode = new Node(")");
            
            // Remove the open parenthesis and closed parenthesis token from the tokens array
            List <Token> tokensWithoutTheParenthesis = new ArrayList<>(tokens).subList(1, tokens.size() - 1);

            // Create node for the <Sentence>
            Node sentenceNode = this.parseSentence(tokensWithoutTheParenthesis);

            // Insert the <Complex Sentence> subnodes
            complexSentenceNode.insertChild(openParenthesisNode);
            complexSentenceNode.insertChild(sentenceNode);
            complexSentenceNode.insertChild(closedParenthesisNode);
        }
        // RHS is in the form: NOT <Sentence>
        else if (this.isEntireExpressionNegated(tokens)) {
            // Create the NOT node
            Node notNode = new Node("NOT");

            // Create the <Sentence> node
            Node sentenceNode = this.parseSentence(this.consume(tokens));

            // Insert the <Complex Sentence> subnodes
            complexSentenceNode.insertChild(notNode);
            complexSentenceNode.insertChild(sentenceNode);
        }
        // RHS is in the form: <Sentence><Connective><Sentence>
        else if (this.getIndexOfMainConnective(tokens) != -1) {
            // Get the index of the main connective
            int indexOfMainConnective = this.getIndexOfMainConnective(tokens);

            // Create the left sentence node
            List <Token> tokensToTheLeftOfMainConnective = new ArrayList<>(tokens).subList(0, indexOfMainConnective);
            Node leftSentenceNode = this.parseSentence(tokensToTheLeftOfMainConnective);

            // Create the main connetive node
            Node connectiveNode = this.parseConnective(tokens.get(indexOfMainConnective));

            // Create the right sentence node
            List <Token> tokensToTheRightOfMainConnective = new ArrayList<>(tokens).subList(indexOfMainConnective + 1, tokens.size());
            Node rightSentenceNode = this.parseSentence(tokensToTheRightOfMainConnective);

            // Insert the <Complex Sentence> subnodes
            complexSentenceNode.insertChild(leftSentenceNode);
            complexSentenceNode.insertChild(connectiveNode);
            complexSentenceNode.insertChild(rightSentenceNode);
        }

        return complexSentenceNode;
    }
    
    // Return a Connective Node containing the token if its the RHS of the production rule: 
    // <Connective> := “AND” | “OR” | “IMPLIES” | “EQUIVALENT”
    public Node parseConnective(Token token) {
        // Get the value of the token
        String tokenValue = token.getValue();

        // Ensure that the current token is a connective
        if (this.isConnective(tokenValue)) {
            // Create the Connective Node
            Node connectiveNode = new Node("<Connective>");

            // Create the child node of the Connective
            Node currentNode = new Node(tokenValue);
            connectiveNode.insertChild(currentNode);

            return connectiveNode;
        } else {
            return null;
        }
    }

    // Utility method to  the current token and advance to the next
    private List <Token> consume(List <Token> tokens) {
        return new ArrayList<>(tokens).subList(1, tokens.size());
    }

    // Utility method to check whether the entire tokens inside an Array List are enclosed in parenthesis
    public boolean areTokensEnclosed(List <Token> tokens) {
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

                // Corresponding closed parenthesis of the first token is located at the middle of the expression
                boolean closedParenthesisFoundAtTheMiddle = numberOfClosedParenthesisToFind == 0 && 
                currentTokenIndex != tokens.size() - 1;

                // Corresponding closed parenthesis of the first token is located at the end of the expression
                boolean closedParenthesisFoundAtTheEnd = numberOfClosedParenthesisToFind == 0 && 
                currentTokenIndex == tokens.size() - 1;

                // An expression is enclosed only if the first token is "(" and the corresponding ")" is the last token
                if (closedParenthesisFoundAtTheEnd) {
                    return true;
                } else if (closedParenthesisFoundAtTheMiddle) {
                    return false;
                }

                // Move to the next token
                currentTokenIndex++;
            } 
        } 

        return false;
    }

    // Utility method to find the index of the main connective of the sentence
    public int getIndexOfMainConnective(List <Token> tokens) {
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

    // Utility method to check whether the entire expression is negated
    public boolean isEntireExpressionNegated(List <Token> tokens) {
        // Get the first token 
        Token firstToken = tokens.get(0);

        // Ensure that the first token is a "NOT"
        if (firstToken.getValue() == "NOT") {

            // Get the tokenized subexpression without the "NOT"
            List <Token> subexpressionWithoutNOT =  new ArrayList<>(tokens).subList(1, tokens.size());

            // NOT is applied to the remaining tokens either:
            // (1) The expression is enclosed or 
            // (2) because the remaining subexpression is a single atomic sentence
            if (this.areTokensEnclosed(subexpressionWithoutNOT) || subexpressionWithoutNOT.size() == 1) {
                return true;
            }
        }
        return false;
    }

    // Utility method to check whether the given string is a connective
    public boolean isConnective(String value) {
        return value == "AND" || value == "OR" || value == "IMPLIES" || value == "EQUIVALENT";
    }
}
