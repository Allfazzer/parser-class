import java.util.List;

public class Main { 
    public static void main(String[] args) {
        // Initialize a Scanner
        Scanner scanner =  new Scanner();

        // Retrieve the tokens from the scanner
        List <List<Token>> tokens = scanner.getTokens();

        // Retrieve the token table of the first sentence
        List <Token> tokensOfFirstSentence = tokens.get(1);

        // Initialize a Parser
        Parser parser = new Parser();

        // Parse the tokens
        Node root = parser.parseSentence(tokensOfFirstSentence);
    }
}