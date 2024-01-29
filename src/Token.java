import java.util.Objects;

// Token class - represents a token for the lexer
public class Token {

    private TokenType tokenType; // as defined in TokenType enum.
    private String val; // Value of the token. It can be null for tokens where value is not applicable, like ENDOFLINE.
    private int lineNo; // Line number in the source code where the token is located.
    private int position; // Position within its line, as an index.

    // Constructor for a Token without value.
    // Useful for tokens where a specific value is not applicable.
    public Token(TokenType tokenType, int lineNo, int position) {
        this.tokenType = tokenType;
        this.lineNo = lineNo;
        this.position = position;
    }

    // Overloaded constructor to create a Token instance with specific value.
    // This will be used in the case where our token carries a value, like identifiers or literals.
    public Token(TokenType tokenType, String val, int lineNo, int position) {
        this(tokenType, lineNo, position);
        this.val = val;
    }

    // Returns a string representation of the Token object.
    // The format is "TokenType" or "TokenType(Value)" if there is a value inside the token.
    @Override
    public String toString() {
        if (tokenType == TokenType.WORD || tokenType == TokenType.NUMBER) {
            return String.format("%s(%s)", tokenType, val);
        } else {
            return tokenType.toString();
        }
    }

    // Override the equals method from Object class.
    // This method is used to compare two Token objects for equality.
    // Two tokens are equal if their types, values, line numbers, and positions are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return lineNo == token.lineNo &&
                position == token.position &&
                tokenType == token.tokenType &&
                Objects.equals(val, token.val);
    }

    // Override the hashCode method from Object class.
    // This method returns a hash code value for the object on which this method is invoked.
    // This method is primarily used for insertion of this object in a Map or a Set.
    @Override
    public int hashCode() {
        return Objects.hash(tokenType, val, lineNo, position);
    }

    // Getter for TokenType
    // Return the token's type
    public TokenType getTokenType() {
        return this.tokenType;
    }
}


// TokenType enum defines the types of tokens that can be identified during lexical analysis.
// Each type represents a distinct category of token found in the source code.
// WORD: Represents a sequence of characters that form a word.
// NUMBER: Represents a numeric value, either integer or floating point.
// ENDOFLINE: Represents the end of a line in the text.
enum TokenType {
    WORD,
    NUMBER,
    ENDOFLINE
}