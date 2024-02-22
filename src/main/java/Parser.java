
import java.util.LinkedList;
import java.util.Optional;

public class Parser {
    private final TokenManager tokenManager;

    public Parser(LinkedList<Token> tokens) {
        this.tokenManager = new TokenManager(tokens);
    }

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

    public ExpressionNode expression() {
        Node term = term();
        while (true) {
            if (matchAndRemove(Token.TokenType.PLUS)) {
                term = new MathOpNode(MathOpNode.OPERATION.ADD, term, term());
            } else if (matchAndRemove(Token.TokenType.MINUS)) {
                term = new MathOpNode(MathOpNode.OPERATION.SUBTRACT, term, term());
            } else {
                break;
            }
        }
        return new ExpressionNode(term);
    }

    public TermNode term() {
        Node factor = factor();
        while (tokenManager.moreTokens()) {
            if (matchAndRemove(Token.TokenType.MULTIPLY)) {
                factor = new MathOpNode(MathOpNode.OPERATION.MULTIPLY, factor, factor());
            } else if (matchAndRemove(Token.TokenType.DIVIDE)) {
                factor = new MathOpNode(MathOpNode.OPERATION.DIVIDE, factor, factor());
            } else {
                break;
            }
        }
        return new TermNode(factor);
    }

    public Node factor() {
        if (matchAndRemove(Token.TokenType.MINUS)) {
            return new FactorNode(new MathOpNode(MathOpNode.OPERATION.SUBTRACT, new IntegerNode(0), factor()));
        }
        if (matchAndRemove(Token.TokenType.LPAREN)) {
            ExpressionNode innerExpr = expression();
            if (!matchAndRemove(Token.TokenType.RPAREN)) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            return new FactorNode(innerExpr);
        }
        Optional<Token> numberToken = tokenManager.matchAndRemove(Token.TokenType.NUMBER);
        if (numberToken.isPresent()) {
            if (numberToken.get().getVal().contains(".")) {
                return new FactorNode(new FloatNode(Float.parseFloat(numberToken.get().getVal())));
            } else {
                return new FactorNode(new IntegerNode(Integer.parseInt(numberToken.get().getVal())));
            }
        } else {
            throw new IllegalArgumentException("Unexpected end of factor");
        }
    }

    private boolean matchAndRemove(Token.TokenType type) {
        return tokenManager.matchAndRemove(type).isPresent();
    }
}