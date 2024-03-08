
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Parser {
    /**
     * The TokenManager class manages the token stream. It keeps track
     * of the current position in the token list and provides methods to access and manipulate the tokens.
     */
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


    /**
     * This method is responsible for parsing expressions from the token stream.
     * It creates a new ProgramNode and then repeatedly calls the expression() method to parse individual expressions.
     * It continues parsing expressions as long as there are more tokens and the next token is a separator (i.e., an ENDOFLINE token).
     *
     * @return A ProgramNode representing the root of the Abstract Syntax Tree (AST) for the parsed expressions.
     */
    public ProgramNode parseExpressions() {
        ProgramNode program = new ProgramNode();
        do {
            program.addExpression(expression());
        } while (acceptSeparators() && tokenManager.moreTokens());
        return program;
    }

    /**
    * This method is responsible for parsing statements from the token stream.
    * It creates a new StatementsNode and then repeatedly calls the statement() method to parse individual statements.
    * It continues parsing statements as long as there are more tokens and the next token is a separator (i.e., an ENDOFLINE token).
    * If the statement() method returns null, it stops parsing and returns the StatementsNode.
    *
    * @return A StatementsNode representing the parsed statements.
    */
    public StatementsNode statements() {
        StatementsNode statements = new StatementsNode();
        StatementNode statementNode;
        while ((statementNode = statement()) != null) {
            statements.addStatement(statementNode);
            acceptSeparators();
        }
        return statements;
    }

    /**
     * This method is responsible for parsing a statement from the token stream.
     * It first checks if the next token is a PRINT token. If it is, it calls the printStatement() method to parse a print statement.
     * If the next token is a WORD token, it calls the assignment() method to parse an assignment statement.
     * If the next token is neither a PRINT nor a WORD token, it returns null.
     *
     * @return A StatementNode representing the parsed statement, or null if the next token is neither a PRINT nor a WORD token.
     */
    public StatementNode statement() {
        if (peekAndMatch(Token.TokenType.READ)) {
            return readStatement();
        } else if (peekAndMatch(Token.TokenType.DATA)) {
            return dataStatement();
        } else if (peekAndMatch(Token.TokenType.PRINT)) {
            return printStatement();
        } else if (peekAndMatch(Token.TokenType.INPUT)) {
            return inputStatement();
        } else if (peekAndMatch(Token.TokenType.WORD)) {
            return assignment();
        }
        return null;
    }

    /**
     * This method is responsible for parsing an input statement from the token stream.
     * It first checks if the next token is an INPUT token. If it is, it creates a new InputNode.
     * It then calls the factor() method to parse the first parameter and adds the returned parameter to the InputNode.
     * It then repeatedly calls the factor() method to parse a list of variables to be input and adds the returned variables to the InputNode.
     * Finally, it returns the InputNode.
     *
     * @return An InputNode representing the parsed input statement.
     */
    private StatementNode inputStatement() {
        if (!matchAndRemove(Token.TokenType.INPUT)) {
            throw new IllegalArgumentException("Invalid Input Statement");
        }
        Node firstParameter = factor();
        List <VariableNode> variables = new ArrayList<>();
        while (matchAndRemove(Token.TokenType.COMMA)) {
            variables.add((VariableNode) factor());
        }
        return new InputNode(firstParameter, variables);
    }


    /**
     * This method is responsible for parsing a read statement from the token stream.
     * It first checks if the next token is a READ token. If it is, it creates a new ReadNode.
     * It then calls the factor() method to parse a list of variables to be read and adds the returned variables to the ReadNode.
     * Finally, it returns the ReadNode.
     *
     * @return A ReadNode representing the parsed read statement.
     */
    public StatementNode readStatement() {
        if (!matchAndRemove(Token.TokenType.READ)) {
            throw new IllegalArgumentException("Invalid Read Statement");
        }
        List<VariableNode> variables = new ArrayList<>();
        do {
            variables.add((VariableNode) factor());
        } while (matchAndRemove(Token.TokenType.COMMA));
        return new ReadNode(variables);
    }

    /**
     * This method is responsible for parsing a data statement from the token stream.
     * It first checks if the next token is a DATA token. If it is, it creates a new DataNode.
     * It then calls the expression() method to parse a list of data values and adds the returned values to the DataNode.
     * Finally, it returns the DataNode.
     *
     * @return A DataNode representing the parsed data statement.
     */
    public StatementNode dataStatement() {
        if (!matchAndRemove(Token.TokenType.DATA)) {
            throw new IllegalArgumentException("Invalid Data Statement");
        }
        List<Node> data = new ArrayList<>();
        do {
            data.add(expression());
        } while (matchAndRemove(Token.TokenType.COMMA));
        return new DataNode(data);
    }

    /**
     * This method is responsible for parsing a print statement from the token stream.
     * It first checks if the next token is a PRINT token. If it is, it creates a new PrintNode.
     * It then calls the printList() method to parse a list of nodes to be printed and adds the returned nodes to the PrintNode.
     * Finally, it returns the PrintNode.
     *
     * @return A PrintNode representing the parsed print statement.
     */
    public PrintNode printStatement() {
        if (!matchAndRemove(Token.TokenType.PRINT)) {
            throw new IllegalArgumentException("Invalid Print Statement");
        }
        PrintNode printNode = new PrintNode();

        // Call the printList method and add the returned nodes to the PrintNode
        List<Node> nodes = printList();
        for (Node node : nodes) {
            printNode.addNode(node);
        }

        return printNode;
    }

/**
     * This method is responsible for parsing a list of nodes to be printed from the token stream.
     * It creates a new list of nodes and repeatedly calls the expression() method to parse individual nodes.
     * It continues parsing nodes as long as there are more tokens and the next token is not an ENDOFLINE token.
     *
     * @return A list of nodes to be printed.
     */
public List<Node> printList() {
    List<Node> nodes = new ArrayList<>();
    while (!peekAndMatch(Token.TokenType.ENDOFLINE)) {
        if (peekAndMatch(Token.TokenType.STRINGLITERAL)) {
            nodes.add(new StringNode(tokenManager.matchAndRemove(Token.TokenType.STRINGLITERAL).get().getVal()));
        } else {
            nodes.add(expression());
        }
        if (peekAndMatch(Token.TokenType.COMMA)) {
            matchAndRemove(Token.TokenType.COMMA);
        } else if (!peekAndMatch(Token.TokenType.ENDOFLINE)) {
            throw new RuntimeException("Expected a comma between expressions");
        }
    }
    return nodes;
}

    /**
     * This method is responsible for parsing an assignment statement from the token stream.
     * It first calls the factor() method to parse a variable name.
     * If the next token is an EQUALS token, it calls the expression() method to parse the expression to be assigned to the variable.
     * If the next token is not an EQUALS token, it returns null.
     *
     * @return An AssignmentNode representing the parsed assignment statement, or null if the next token is not an EQUALS token.
     */
    public AssignmentNode assignment() {
        VariableNode variableNode = (VariableNode) factor();
        if (matchAndRemove(Token.TokenType.EQUALS)) {
            return new AssignmentNode(variableNode, expression());
        }
        return null;
    }

    /**
     * Parses and generates an ExpressionNode from the given tokens. An ExpressionNode represents an expression in the grammar.
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
     * Parses and generates a TermNode from the given tokens. A TermNode represents a term in the grammar.
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
     * Parses and generates a FactorNode from the given tokens. A FactorNode represents a factor in the grammar.
     * It evaluates the input tokens and creates a binary expression tree.
     *
     * @return The FactorNode representing the root of the factor tree.
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

    /**
     * Checks if the next token in the token list has the specified TokenType, without removing it from the list.
     * If the token matches and there are more tokens in the list, it returns true. Otherwise, it returns false.
     *
     * @param type The TokenType to match against.
     * @return true if the next token matches the specified TokenType, false otherwise.
     */
    private boolean peekAndMatch(Token.TokenType type) {
        Optional<Token> tokenOpt = tokenManager.peek(0);
        return tokenOpt.isPresent() && tokenOpt.get().getTokenType() == type;
    }
}

