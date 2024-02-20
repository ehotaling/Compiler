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
        boolean found = false;
        while (tokenManager.moreTokens() && tokenManager.peek(0).isPresent() && tokenManager.peek(0)
                .get().getTokenType().equals(Token.TokenType.ENDOFLINE)) {
            tokenManager.matchAndRemove(Token.TokenType.ENDOFLINE);
            found = true;
        }
       return found;
    }

    public ProgramNode parse() {
        ProgramNode program = new ProgramNode();
        do {
            program.addExpression(expression());
        } while (acceptSeperators() && tokenManager.moreTokens());
        return program;
    }

    /**
     * Expression: TERM {+|- TERM}
     * Term: FACTOR {*|/ FACTOR}
     * Factor: number | ( EXPRESSION )
     */

    /**
     * Expression: TERM {+|- TERM}
     * @return
     */
    public Node expression() {
        Node left = term();
        while (true) {
            if (matchAndRemove(Token.TokenType.PLUS)) {
                Node right = term();
                left = new MathOpNode(MathOpNode.OPERATION.ADD, left, right);
            } else if (matchAndRemove(Token.TokenType.MINUS)) {
                Node right = term();
                left = new MathOpNode(MathOpNode.OPERATION.SUBTRACT, left, right);
            } else {
                break;
            }
        }
        return left;
    }

    /**
     *
     * @return Term: FACTOR {*|/ FACTOR}
     */
    public Node term() {
        Node left = factor();
        while (true) {
            if (matchAndRemove(Token.TokenType.MULTIPLY)) {
                Node right = factor();
                left = new MathOpNode(MathOpNode.OPERATION.MULTIPLY, left, right);
            } else if (matchAndRemove(Token.TokenType.DIVIDE)) {
                Node right = factor();
                left = new MathOpNode(MathOpNode.OPERATION.DIVIDE, left, right);
            } else {
                break;
            }
        }
        return left;
    }

    /**
     *
     * @return FloatNode, IntegerNode, or value from Expression
     */
    public Node factor() {
        // Check if the next token is a unary minus.
        if (matchAndRemove(Token.TokenType.MINUS)) {
            // If so, parse the next factor and subtract it from zero.
            return new MathOpNode(MathOpNode.OPERATION.SUBTRACT, new IntegerNode(0), factor());
        }

        if (matchAndRemove(Token.TokenType.LPAREN)) {
            Node expression = expression();
            matchAndRemove(Token.TokenType.RPAREN);
            return expression;
        }

        return tokenManager.matchAndRemove(Token.TokenType.NUMBER).map(n ->
                n.toString().contains(".") ?
                        new FloatNode(Float.parseFloat(n.getVal())) :
                        new IntegerNode(Integer.parseInt(n.getVal()))
        ).orElse(null);
    }

    private boolean matchAndRemove(Token.TokenType type) {
        return tokenManager.matchAndRemove(type).isPresent();
    }

}
