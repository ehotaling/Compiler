import java.util.LinkedList;
import java.util.Optional;
public class TokenManager {

    private LinkedList<Token> tokens;
    private int currentTokenIndex;

    public TokenManager(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    // peek “j” tokens ahead and return the token if we
    // aren’t past the end of the token list.
    Optional<Token> Peek(int j) {
        int peekIndex = currentTokenIndex + j;
        if (peekIndex < tokens.size()) {
            return Optional.of(tokens.get(peekIndex));
        } else {
            return Optional.empty();
        }
    }

    // returns true if the token list is not empty
    boolean moreTokens() {
        return currentTokenIndex < tokens.size();
    }

    // looks at the head of the list. If the token type of the head
    // is the same as what was passed in, remove that token from the
    // list and return it. In all other cases, returns Optional.empty().
    Optional<Token> matchAndRemove(Token.TokenType t) {
        Token head = tokens.getFirst();
        if (head.getTokenType().equals(t)) {
            tokens.removeFirst();
            return Optional.of(head);
        }
        return Optional.empty();
    }
}
