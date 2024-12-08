public class ParserError extends Exception{
    public ParserError(String message) {
        super("Parsing Error: " + message);
    }
}