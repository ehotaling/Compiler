
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
    public boolean acceptSeparators() {
        boolean found = false;
        while (peekAndMatch(Token.TokenType.ENDOFLINE)) {
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
        program.addStatements(statements());
        return program;
    }

    public ProgramNode parseExpressions() {
        ProgramNode program = new ProgramNode();
        do {
            program.addExpression(expression());
        } while (acceptSeparators() && tokenManager.moreTokens());
        return program;
    }

    public StatementsNode statements() {
        StatementsNode statements = new StatementsNode();
        do {
            StatementNode statementNode = statement();
            if (statementNode != null) {
                statements.addStatement(statementNode);
            } else {
                return statements;
            }
        } while(acceptSeparators() && tokenManager.moreTokens());
        return statements;
    }

    public StatementNode statement() {
        if (peekAndMatch(Token.TokenType.PRINT)) {
            return printStatement();
        } else if (peekAndMatch(Token.TokenType.WORD)) {
            return assignment();
        }
        return null;
    }

    public PrintNode printStatement() {
        if (!matchAndRemove(Token.TokenType.PRINT)) {
            throw new IllegalArgumentException("Invalid Print Statement");
        }
        PrintNode printNode = new PrintNode();
        while (!peekAndMatch(Token.TokenType.ENDOFLINE)) {
            // TODO should a string literal be an Expression -> Term -> Factor or a new rule?
            Optional<Token> stringLiteralOpt = tokenManager.matchAndRemove(Token.TokenType.STRINGLITERAL);
            if (stringLiteralOpt.isPresent()) {
                printNode.addNode(new StringNode(stringLiteralOpt.get().getVal()));
            } else {
                printNode.addNode(expression());
            }
        }
        return printNode;
    }

    public AssignmentNode assignment() {
        VariableNode variableNode = (VariableNode) factor();
        if (matchAndRemove(Token.TokenType.EQUALS)) {
            return new AssignmentNode(variableNode, expression());
        }
        return null;
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
     * Expression: TERM {+|- TERM}
     * Term: FACTOR {*|/ FACTOR}
     * Factor: VARIABLE | number | ( EXPRESSION )
     *
     * @return The FactorNode representing the evaluated factor.
     * @throws IllegalArgumentException if there is a mismatched parentheses or an unexpected end of factor.
     */
    public Node factor() {
        Optional<Token> wordTokenOpt = tokenManager.matchAndRemove(Token.TokenType.WORD);
        if (wordTokenOpt.isPresent()) {
            return new VariableNode(wordTokenOpt.get().getVal());
        }

        Optional<Token> stringLiteralOpt = tokenManager.matchAndRemove(Token.TokenType.STRINGLITERAL);
        if (stringLiteralOpt.isPresent()) {
            return new VariableNode(stringLiteralOpt.get().getVal());
        }

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

        return number(numberTokenOpt.get(), isNegative);
    }

    private FactorNode number(Token numberToken, boolean isNegative) {
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

    private boolean peekAndMatch(Token.TokenType type) {
        Optional<Token> tokenOpt = tokenManager.peek(0);
        return tokenOpt.isPresent() && tokenOpt.get().getTokenType() == type;
    }
}