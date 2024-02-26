
import java.util.LinkedList;
import java.util.Optional;

public class Parser {
    /**
     * The TokenManager class manages the token stream. It keeps track
     * of the current position in the token list and provides methods to access and manipulate the tokens.
     */
    // Token Manager manages the token stream.
    private final TokenManager tokenManager;

    /**
     * Parser class that is responsible for parsing a list of tokens and
     * generating an Abstract Syntax Tree (AST).
     */
    public Parser(LinkedList<Token> tokens) {
        this.tokenManager = new TokenManager(tokens);
    }

    /**
     * This method is used to accept separators in the input tokens.
     * It checks if the next token is of type "ENDOFLINE" and continues to remove and match the "ENDOFLINE" tokens
     * until a non-"ENDOFLINE" token is encountered.
     *
     * @return true if any "ENDOFLINE" tokens are found and matched, false otherwise.
     */
    public boolean acceptSeperators() {
        boolean found = false;
        while (tokenManager.moreTokens() && tokenManager.peek(0).isPresent() && tokenManager.peek(0)
                .get().getTokenType().equals(Token.TokenType.ENDOFLINE)) {
            tokenManager.matchAndRemove(Token.TokenType.ENDOFLINE);
            found = true;
        }
        return found;
    }

    /**
     * Parses a list of tokens and generates an Abstract Syntax Tree (AST).
     *
     * @return The ProgramNode representing the root of the AST.
     */
    public ProgramNode parse() {
        ProgramNode program = new ProgramNode();
        do {
            program.addExpression(expression());
        } while (acceptSeperators() && tokenManager.moreTokens());
        return program;
    }

    /**
     * Parses and generates an ExpressionNode from the given tokens.
     * It evaluates the input tokens and creates a binary expression tree.
     *
     * @return The ExpressionNode representing the root of the expression tree.
     */
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

    /**
     * Parses and generates a TermNode from the given tokens. A TermNode represents a term in an expression.
     * It evaluates the input tokens and creates a binary expression tree.
     *
     * @return The TermNode representing the root of the term tree.
     */
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

    /**
     * Evaluates and generates a Node representing a factor in an expression.
     *
     * @return The FactorNode representing the evaluated factor.
     * @throws IllegalArgumentException if there is a mismatched parentheses or an unexpected end of factor.
     */
    public Node factor() {
        boolean isNegative = matchAndRemove(Token.TokenType.MINUS);

        if (matchAndRemove(Token.TokenType.LPAREN)) {
            ExpressionNode innerExpr = expression();
            if (!matchAndRemove(Token.TokenType.RPAREN)) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }

            if (isNegative) {
                // wrap the original expression in a new Term (-1 * expr) to conform to the original grammar
                innerExpr = new ExpressionNode(
                        new TermNode(
                                new MathOpNode(
                                        MathOpNode.OPERATION.MULTIPLY,
                                        new FactorNode(new IntegerNode(-1)),
                                        new FactorNode(innerExpr)
                                )
                        )
                );
            }
            return new FactorNode(innerExpr);
        }

        Optional<Token> numberTokenOpt = tokenManager.matchAndRemove(Token.TokenType.NUMBER);
        if (numberTokenOpt.isEmpty()) {
            throw new IllegalArgumentException("Unexpected end of factor");
        }

        Token numberToken = numberTokenOpt.get();
        if (numberToken.getVal().contains(".")) {
            float val = Float.parseFloat(numberToken.getVal());
            FloatNode floatNode = isNegative ? new FloatNode(-val) : new FloatNode(val);
            return new FactorNode(floatNode);
        }
        else {
            int val = Integer.parseInt(numberToken.getVal());
            IntegerNode integerNode = isNegative ? new IntegerNode(-val) : new IntegerNode(val);
            return new FactorNode(integerNode);
        }
    }

    /**
     * Checks if the next token in the token list has the specified TokenType,
     * removes the token from the list if it matches, and returns true.
     * If the token does not match or there are no more tokens in the list, it returns false.
     *
     * @param type The TokenType to match against.
     * @return true if the next token matches the specified TokenType and is successfully removed, false otherwise.
     */
    private boolean matchAndRemove(Token.TokenType type) {
        return tokenManager.matchAndRemove(type).isPresent();
    }
}