import java.util.List;

public class Scanner {
    // Tokens as an array of arrays (each array represents a logical expression)
    private final List<List<Token>> tokens = List.of(
        // P
        List.of(
            new Token("identifier", "P")
        ),
        
        // Q
        List.of(
            new Token("identifier", "Q")
        ),

         // ~P (NOT P)
         List.of(
            new Token("keyword", "NOT"),
            new Token("identifier", "P")
        ),
        
        // ~Q (NOT Q)
        List.of(
            new Token("keyword", "NOT"),
            new Token("identifier", "Q")
        ),
        
        // P ∨ Q (OR)
        List.of(
            new Token("identifier", "P"),
            new Token("operator", "OR"),
            new Token("identifier", "Q")
        ),
        
        // P ∧ Q (AND)
        List.of(
            new Token("identifier", "P"),
            new Token("operator", "AND"),
            new Token("identifier", "Q")
        ),
        
        // P ⇒ Q (IMPLIES)
        List.of(
            new Token("identifier", "P"),
            new Token("operator", "IMPLIES"),
            new Token("identifier", "Q")
        ),
        
        // P ⇔ Q (EQUIVALENT)
        List.of(
            new Token("identifier", "P"),
            new Token("operator", "EQUIVALENT"),
            new Token("identifier", "Q")
        ),
        
        // (P ∨ Q) ∧ ~Q (OR, AND, NOT)
        List.of(
            new Token("open_parenthesis", "("),
            new Token("identifier", "P"),
            new Token("operator", "OR"),
            new Token("identifier", "Q"),
            new Token("close_parenthesis", ")"),
            new Token("operator", "AND"),
            new Token("keyword", "NOT"),
            new Token("identifier", "Q")
        ),
        
        // (P ∨ Q) ∧ ~Q ⇒ S (OR, AND, NOT, IMPLIES)
        List.of(
            new Token("open_parenthesis", "("),
            new Token("identifier", "P"),
            new Token("operator", "OR"),
            new Token("identifier", "Q"),
            new Token("close_parenthesis", ")"),
            new Token("operator", "AND"),
            new Token("keyword", "NOT"),
            new Token("identifier", "Q"),
            new Token("operator", "IMPLIES"),
            new Token("identifier", "S")
        )
    );

    // Method to get the tokens
    public List<List<Token>> getTokens() {
        return tokens;
    }
}