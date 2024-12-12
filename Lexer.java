import java.util.*;
import java.util.regex.*;

public class Lexer {
    private String input;
    private List<Token> tokens;

    public Lexer (String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        String tokenSpec =
                "(?<IDENTIFIER>[pPqQsS])|" +					//Variables P, Q, and S
                "(?<AND>and|AND|&&)|" +							//AND Operator
                "(?<OR>or|OR|\\|\\|)|" +						//OR Operator
                "(?<NOT>not|NOT|!)|" +							//NOT Operator
                "(?<IMPLIES>implies|IMPLIES|->)|" +				//IMPLIES Operator
                "(?<EQUIVALENT>equivalent|EQUIVALENT|<->)|" +	//EQUIVALENT Operator
                "(?<LPAREN>\\()|" +								//Left Parenthesis
                "(?<RPAREN>\\))|" +								//Right Parenthesis
                "(?<WHITESPACE>[ \\t]+)";						//Whitespace

        //Compile regular expression and match input
        Pattern pattern = Pattern.compile(tokenSpec, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        int lastMatchEnd = 0;

        while (matcher.find()) {
            //Detect invalid tokens
            if (matcher.start() != lastMatchEnd) {
                throw new IllegalArgumentException("Invalid input. Only P, Q, S, and logical operators (AND, OR, NOT, IMPLIES, EQUIVALENT) are allowed.");
            }
            lastMatchEnd = matcher.end();

            //Skip whitespace
            if (matcher.group("WHITESPACE") != null) {
                continue;
            }

            //Initialize type and value of the token
            String type = null;
            String value = null;

            //Check for matching token type
            for (String groupName : new String[] {"IDENTIFIER", "AND", "OR", "NOT", "IMPLIES", "EQUIVALENT", "LPAREN", "RPAREN"}) {
                if (matcher.group(groupName) != null) {
                    type = groupName;
                    value = matcher.group(groupName);
                    break;
                }
            }

            tokens.add(new Token(type, value));
        }

        //Checks if entire input was matched
        if (lastMatchEnd != input.length()) {
            throw new IllegalArgumentException("Invalid input. Only P, Q, S, and logical operators (AND, OR, NOT, IMPLIES, EQUIVALENT) are allowed.");
        }

        return tokens;
    }
}