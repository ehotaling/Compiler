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
        return true;
    }

    public ProgramNode parse() {
        return new ProgramNode(expression());

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
        Node expression = term();
        while (true) {
            if (matchAndRemove(Token.TokenType.PLUS)) {
                expression = new MathOpNode(MathOpNode.OPERATION.ADD, expression, term());
            } else if (matchAndRemove(Token.TokenType.MINUS)) {
                expression = new MathOpNode(MathOpNode.OPERATION.SUBTRACT, expression, term());
            } else {
                break;
            }
        }
        return expression;
    }

    /**
     *
     * @return Term: FACTOR {*|/ FACTOR}
     */
    public Node term() {
        Node term = factor();
        while (true) {
            if (matchAndRemove(Token.TokenType.MULTIPLY)) {
                term = new MathOpNode(MathOpNode.OPERATION.MULTIPLY, term, factor());
            } else if (matchAndRemove(Token.TokenType.DIVIDE)) {
                term = new MathOpNode(MathOpNode.OPERATION.DIVIDE, term, factor());
            } else {
                break;
            }
        }
        return term;
    }

    /**
     *
     * @return FloatNode, IntegerNode, or value from Expression
     */
    public Node factor() {
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
