import java.util.LinkedList;
import java.util.Optional;

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
    public ExpressionNode expression() {
        LinkedList<Node> terms = new LinkedList<>();
        Node term = term();
        while (true) {
            if (matchAndRemove(Token.TokenType.PLUS)) {
                terms.add(new MathOpNode(MathOpNode.OPERATION.ADD, term, term()));
            } else if (matchAndRemove(Token.TokenType.MINUS)) {
                terms.add(new MathOpNode(MathOpNode.OPERATION.SUBTRACT, term, term()));
            } else {
                break; // Do not add the original Term, or else it will be a duplicate
            }
        }
        // If the Expression only has a single Term, do not add the empty list
        return !terms.isEmpty() ? new ExpressionNode(terms) : new ExpressionNode(term);
    }

    /**
     *
     * @return Term: FACTOR {*|/ FACTOR}
     */
    public TermNode term() {
        LinkedList<Node> factors = new LinkedList<>();
        Node factor = factor();
        while (true) {
            if (matchAndRemove(Token.TokenType.MULTIPLY)) {
                factors.add(new MathOpNode(MathOpNode.OPERATION.MULTIPLY, factor, factor()));
            } else if (matchAndRemove(Token.TokenType.DIVIDE)) {
                factors.add(new MathOpNode(MathOpNode.OPERATION.DIVIDE, factor, factor()));
            } else {
                break; // Do not add the original Factor, or else it will be a duplicate
            }
        }
        // If the Term only has a single Factor, do not add the empty list
        return !factors.isEmpty() ? new TermNode(factors) : new TermNode(factor);
    }

    /**
     *
     * @return FloatNode, IntegerNode, or value from Expression
     */
    public FactorNode factor() {
        if (matchAndRemove(Token.TokenType.LPAREN)) {
            FactorNode parenExpression = new FactorNode(expression());
            matchAndRemove(Token.TokenType.RPAREN);
            return parenExpression;
        }

        return tokenManager.matchAndRemove(Token.TokenType.NUMBER).map(n ->
            n.toString().contains(".") ?
                    new FactorNode(new FloatNode(Float.parseFloat(n.getVal()))) :
                    new FactorNode(new IntegerNode(Integer.parseInt(n.getVal())))
        ).orElse(null);
    }

    private boolean matchAndRemove(Token.TokenType type) {
        return tokenManager.matchAndRemove(type).isPresent();
    }
}
