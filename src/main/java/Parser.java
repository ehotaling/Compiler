import java.util.LinkedList;

public class Parser {

    private TokenManager tokenManager;

    // constructor that accepts a LinkedList of Token
    public Parser(LinkedList<Token> tokens) {
       this.tokenManager = new TokenManager(tokens);
    }

    // accepts any number of separators (EndOfLine) and
    // returns true if it finds at least one.
    public boolean acceptSeperators() {
        return true;
    }
}
