import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The LexerTest class is responsible for testing the lexer functionality.
 */
public class ParserTest {

    Lexer lexer = new Lexer();

    Parser parser;

    private LinkedList<Token> lexTokens(String text) throws IOException {
        Path tempFilePath = Files.createTempFile("temp.bas", ".txt");
        Files.writeString(tempFilePath, text);
        return lexer.lex(tempFilePath.toString());
    }

    /**
     * Runs the lexer on the given text and returns a list of tokens identified in the source code.
     *
     * @param text The text to be lexed.
     * @return A LinkedList of tokens identified in the source code.
     * @throws IOException If an I/O error occurs while reading the text.
     */
    private ProgramNode parseExpressions(String text) throws IOException {
        return new Parser(lexTokens(text)).parseExpressions();
    }

    private ProgramNode parseStatements(String text) throws IOException {
        return new Parser(lexTokens(text)).parse();
    }

    private ExpressionNode negate(ExpressionNode expression) {
        return new ExpressionNode(
                new TermNode(
                        new MathOpNode(
                                MathOpNode.OPERATION.MULTIPLY,
                                new FactorNode(new IntegerNode(-1)),
                                new FactorNode(expression)
                        )
                )
        );
    }

    @Test
    public void testExpressionWithOneTerminal() throws IOException {
        ProgramNode testProgram =  parseExpressions("1");

        ExpressionNode expectedExpression = new ExpressionNode(new TermNode(new FactorNode(new IntegerNode(1))));
        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        System.out.println(expectedProgram);
        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testAddTwoTerms() throws IOException {
        ProgramNode testProgram =  parseExpressions("1+2");

        ExpressionNode expectedExpression = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(1))),
                        new TermNode(new FactorNode(new IntegerNode(2)))
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testAddNegativeTerms() throws IOException {
        ProgramNode testProgram =  parseExpressions("-1 + -2");

        ExpressionNode expectedExpression = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(-1))),
                        new TermNode(new FactorNode(new IntegerNode(-2)))
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        System.out.println(testProgram);
        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testSubtractExpression() throws IOException {
        ProgramNode testProgram =  parseExpressions("1-2");

        ExpressionNode expectedExpression = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.SUBTRACT,
                        new TermNode(new FactorNode(new IntegerNode(1))),
                        new TermNode(new FactorNode(new IntegerNode(2)))
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testExpressionAddThreeTerms() throws IOException {
        ProgramNode program =  parseExpressions("1+2+3");
        System.out.println(program);
    }

    @Test
    public void testMultiplyThreeFactors() throws IOException {
        ProgramNode testProgram =  parseExpressions("2*3*4");

        MathOpNode left = new MathOpNode(
                MathOpNode.OPERATION.MULTIPLY,
                new FactorNode(new IntegerNode(2)),
                new FactorNode(new IntegerNode(3))
        );
        MathOpNode right = new MathOpNode(
                MathOpNode.OPERATION.MULTIPLY,
                left,
                new FactorNode(new IntegerNode(4))
        );
        ExpressionNode expectedExpression = new ExpressionNode(new TermNode(right));

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testAddExpressionAndDivideTerm() throws IOException {
        ProgramNode program =  parseExpressions("1+2/2");

        ExpressionNode expectedExpression = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(1))),
                        new TermNode(
                                new MathOpNode(MathOpNode.OPERATION.DIVIDE,
                                        new FactorNode(new IntegerNode(2)),
                                        new FactorNode(new IntegerNode(2))
                                )
                        )
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, program);
    }

    @Test
    public void testMultiFactorExpressionWithFloat() throws IOException {
        ProgramNode program =  parseExpressions("6/2*(1+2.0)");

        ExpressionNode terminalExpression = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(1))),
                        new TermNode(new FactorNode(new FloatNode(2.0f)))
                )
        );
        ExpressionNode expectedExpression = new ExpressionNode(
                new TermNode(
                        new MathOpNode(MathOpNode.OPERATION.MULTIPLY,
                                new MathOpNode(
                                        MathOpNode.OPERATION.DIVIDE,
                                        new FactorNode(new IntegerNode(6)),
                                        new FactorNode(new IntegerNode(2))
                                ),
                                new FactorNode(terminalExpression)
                        )
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, program);
    }

    @Test
    public void testNestedExpression() throws IOException {
        ProgramNode program =  parseExpressions("((1 + 2.0))");

        ExpressionNode terminalExpression = new ExpressionNode(
                new MathOpNode(
                        MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(1))),
                        new TermNode(new FactorNode(new FloatNode(2.0f)))
                )
        );

        ExpressionNode expectedExpression = new ExpressionNode(
                new TermNode(
                        new FactorNode(
                                new ExpressionNode(
                                        new TermNode(
                                                new FactorNode(terminalExpression)
                                        )
                                )
                        )
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, program);
    }

    @Test
    public void testNestedNegativeExpression() throws IOException {
        ProgramNode program =  parseExpressions("4 * -(1 + 2.0)");

        ExpressionNode terminalExpression = new ExpressionNode(
                new MathOpNode(
                        MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(1))),
                        new TermNode(new FactorNode(new FloatNode(2.0f)))
                )
        );

        ExpressionNode negatedExpression = negate(terminalExpression);

        ExpressionNode expectedExpression = new ExpressionNode(
                new TermNode(new MathOpNode(MathOpNode.OPERATION.MULTIPLY,
                        new FactorNode(new IntegerNode(4)),
                        new FactorNode(negatedExpression))
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, program);
    }

    @Test
    public void testTermsWithFloats() throws IOException {
        ProgramNode program = parseExpressions("3.0 * 5 + 2");

        ExpressionNode expectedExpression =
                new ExpressionNode(new MathOpNode(
                        MathOpNode.OPERATION.ADD,
                        new TermNode(
                                new MathOpNode(MathOpNode.OPERATION.MULTIPLY,
                                        new FactorNode(new FloatNode(3.0f)),
                                        new FactorNode(new IntegerNode(5))
                                )
                        ),
                        new TermNode(new FactorNode(new IntegerNode(2))))
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, program);
    }

    // Test for the acceptSeparators method in the Parser class.
    @Test
    public void testAcceptSeparators() {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.ENDOFLINE, "\n",1,0));
        tokens.add(new Token(Token.TokenType.ENDOFLINE, "\n",2,0));
        tokens.add(new Token(Token.TokenType.NUMBER, "42",3,0));
        Parser parser = new Parser(tokens);

        // Multiple ENDOFLINE tokens exist, the method should return true
        assertTrue(parser.acceptSeparators(), "The method did not correctly accept multiple separators.");

        // No ENDOFLINE tokens exist at this point, the method should return false
        assertFalse(parser.acceptSeparators(), "The method did not correctly handle the case when no separators exist.");
    }

    // Test for the acceptSeparators method in the Parser class when no ENDOFLINE tokens exist.
    @Test
    public void testAcceptSeparatorsNoEndOfLine() {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "3",1,0));
        Parser parser = new Parser(tokens);

        // No ENDOFLINE tokens exist, the method should return false
        assertFalse(parser.acceptSeparators(), "The method did not correctly handle the case when no separators exist.");
    }

    // Test for the acceptSeparators method in the Parser class when only one ENDOFLINE token exists.
    @Test
    public void testAcceptSeparatorsSingleEndOfLine() {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.ENDOFLINE, "\n", 1, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5",2,1));
        Parser parser = new Parser(tokens);

        // Single ENDOFLINE token exists, the method should return true
        assertTrue(parser.acceptSeparators(), "The method did not correctly accept a single separator.");
    }

    /**
     * 1 + 2
     * 2 * 2 + 4
     * 4 / (2 + 2)
     * 5 - -(4 * 4)
     * @throws IOException
     */
    @Test
    public void testReadExpressionsFromFile() throws IOException {
        LinkedList<Token> tokens =  lexer.lex("src/test/resources/expression_list.txt");
        ProgramNode program = new Parser(tokens).parseExpressions();

        ExpressionNode line1 = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(1))),
                        new TermNode(new FactorNode(new IntegerNode(2)))
                )
        );

        ExpressionNode line2 = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.ADD,
                        new TermNode(new MathOpNode(MathOpNode.OPERATION.MULTIPLY,
                                new FactorNode(new IntegerNode(2)),
                                new FactorNode(new IntegerNode(2)))),
                        new TermNode(new FactorNode(new IntegerNode(4)))
                )
        );

        ExpressionNode terminalExpression = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(2))),
                        new TermNode(new FactorNode(new IntegerNode(2)))
                )
        );
        ExpressionNode line3 = new ExpressionNode(
                new TermNode(new MathOpNode(MathOpNode.OPERATION.DIVIDE,
                        new FactorNode(new IntegerNode(4)),
                        new FactorNode(terminalExpression)
                )
        ));

        ExpressionNode terminalExpression2 = new ExpressionNode(
                new TermNode(new MathOpNode(MathOpNode.OPERATION.MULTIPLY,
                        new FactorNode(new IntegerNode(4)),
                        new FactorNode(new IntegerNode(4)))
                )
        );

        ExpressionNode negatedExpression = negate(terminalExpression2);

        ExpressionNode line4 = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.SUBTRACT,
                        new TermNode(
                                new FactorNode(new IntegerNode(5))),
                        new TermNode(new FactorNode(negatedExpression))
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(line1);
        expectedProgram.addExpression(line2);
        expectedProgram.addExpression(line3);
        expectedProgram.addExpression(line4);

        System.out.println(program);

        assertEquals(expectedProgram, program);
    }

    @Test
    public void testParseAssignment() throws IOException {
        ProgramNode program = parseStatements("F% = 5");

        StatementsNode statements = new StatementsNode();
        statements.addStatement(new AssignmentNode(
                new VariableNode("F%"),
                new ExpressionNode(
                        new TermNode(
                                new FactorNode(
                                        new IntegerNode(5)
                                )
                        )
                )
        ));

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, program);
    }

    @Test
    public void testParsePrintStatement() throws IOException {
        ProgramNode program = parseStatements("PRINT F%, \"DEG F = \",C%, \"DEG C\"");

        PrintNode printNode = new PrintNode();
        printNode.addNode(new ExpressionNode(new TermNode(new VariableNode("F%"))));
        printNode.addNode(new StringNode("DEG F = "));
        printNode.addNode(new ExpressionNode(new TermNode(new VariableNode("C%"))));
        printNode.addNode(new StringNode("DEG C"));

        StatementsNode statements = new StatementsNode();
        statements.addStatement(printNode);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, program);
    }

//    @Test
//    public void testParsePrintStatementMultipleCommas() throws IOException {
//        LinkedList<Token> tokens =  lexer.lex("src/test/resources/print_list_multiple_commas.txt");
//        ProgramNode program = new Parser(tokens).parse();
//
//        PrintNode printNode = new PrintNode();
//        printNode.addNode(new ExpressionNode(new TermNode(new VariableNode("F%"))));
//        printNode.addNode(new StringNode("DEG F = "));
//        printNode.addNode(new ExpressionNode(new TermNode(new VariableNode("C%"))));
//        printNode.addNode(new StringNode("DEG C"));
//
//        StatementsNode statements = new StatementsNode();
//        statements.addStatement(printNode);
//
//        ProgramNode expectedProgram = new ProgramNode();
//        expectedProgram.addStatements(statements);
//
//        assertEquals(expectedProgram, program);
//    }

    @Test
    public void testReadStatementsFromFile() throws IOException {
        LinkedList<Token> tokens =  lexer.lex("src/test/resources/statements.txt");
        ProgramNode program = new Parser(tokens).parse();

        AssignmentNode assignment1 = new AssignmentNode(
                new VariableNode("F%"),
                new ExpressionNode(
                        new TermNode(
                                new FactorNode(
                                        new FloatNode(10.0f)
                                )
                        )
                )
        );

        AssignmentNode assignment2 = new AssignmentNode(
                new VariableNode("C%"),
                new ExpressionNode(
                        new TermNode(
                                new FactorNode(
                                        new IntegerNode(-5)
                                )
                        )
                )
        );

        PrintNode printNode = new PrintNode();
        printNode.addNode(new ExpressionNode(new TermNode(new VariableNode("F%"))));
        printNode.addNode(new StringNode("DEG F = "));
        printNode.addNode(new ExpressionNode(new TermNode(new VariableNode("C%"))));
        printNode.addNode(new StringNode("DEG C"));
        printNode.addNode(new ExpressionNode(new TermNode(new FactorNode(new IntegerNode(4)))));

        ExpressionNode inner = new ExpressionNode(new TermNode(new MathOpNode(MathOpNode.OPERATION.MULTIPLY,
                new FactorNode(new IntegerNode(5)),
                new FactorNode(new FloatNode(4.0f)))));

        printNode.addNode(new ExpressionNode(
                new TermNode(new FactorNode(inner))
        ));

        StatementsNode statements = new StatementsNode();
        statements.addStatement(assignment1);
        statements.addStatement(assignment2);
        statements.addStatement(printNode);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, program);
    }

    @Test
    public void testFactorWithWordToken() throws IOException {
        ProgramNode testProgram = parseExpressions("x");

        ExpressionNode expectedExpression = new ExpressionNode(new TermNode(new VariableNode("x")));
        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testAssignmentWithExpression() throws IOException {
        ProgramNode testProgram = parseStatements("x = 1 + 2");

        ExpressionNode expression = new ExpressionNode(
                new MathOpNode(MathOpNode.OPERATION.ADD,
                        new TermNode(new FactorNode(new IntegerNode(1))),
                        new TermNode(new FactorNode(new IntegerNode(2)))
                )
        );

        AssignmentNode assignment = new AssignmentNode(new VariableNode("x"), expression);

        StatementsNode statements = new StatementsNode();
        statements.addStatement(assignment);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testFactorWithNegativeNumber() throws IOException {
        ProgramNode testProgram = parseExpressions("-1");

        ExpressionNode expectedExpression = new ExpressionNode(
                new TermNode(
                        new FactorNode(
                                new IntegerNode(-1)
                        )
                )
        );

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addExpression(expectedExpression);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testInputStatement_SingleInput() throws IOException {
        ProgramNode testProgram = parseStatements("input \"Enter your name\"");

        StatementNode statement = new InputNode(List.of(new StringNode("Enter your name")));
        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testInputStatement_MultipleInputs() throws IOException {
        ProgramNode testProgram = parseStatements("input \"x\", y, z");

        List<Node> inputs = new ArrayList<>();
        inputs.add(new StringNode("x"));
        inputs.add(new VariableNode("y"));
        inputs.add(new VariableNode("z"));

        StatementNode statement = new InputNode(inputs);
        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testInputStatement_NoInputs() {
        assertThrows(IllegalArgumentException.class, () -> {
            parseStatements("input");
        });
    }

    @Test
    public void testInputStatement_WhitespaceBetweenVariablesAndCommas() throws IOException {
        ProgramNode testProgram = parseStatements("input \"Input vars: var1, var2\" y , z");

        StatementNode statement = new InputNode(new ArrayList<>(
                Arrays.asList(new StringNode("Input vars: var1, var2"),
                        new VariableNode("y"),
                        new VariableNode("z")
                )
        ));

        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void readStatement_ValidTokens_ReturnsReadNode() throws IOException {
        ProgramNode testProgram = parseStatements("read x, y, z");

        List<VariableNode> variables = new ArrayList<>();
        variables.add(new VariableNode("x"));
        variables.add(new VariableNode("y"));
        variables.add(new VariableNode("z"));

        StatementNode statement = new ReadNode(variables);
        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void readStatement_SingleVariable_ReturnsReadNode() throws IOException {
        ProgramNode testProgram = parseStatements("read x");

        List<VariableNode> variables = new ArrayList<>();
        variables.add(new VariableNode("x"));

        StatementNode statement = new ReadNode(variables);
        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void readStatement_NoVariables_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            parseStatements("read");
        });
    }

    @Test
    public void readStatement_InvalidTokens_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            parseStatements("read x, 123, z");
        });
    }

    @Test
    public void readStatement_WhitespaceBetweenVariablesAndCommas_ReturnsReadNode() throws IOException {
        ProgramNode testProgram = parseStatements("read x , y , z");

        List<VariableNode> variables = new ArrayList<>();
        variables.add(new VariableNode("x"));
        variables.add(new VariableNode("y"));
        variables.add(new VariableNode("z"));

        StatementNode statement = new ReadNode(variables);
        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void dataStatement_ValidTokens_ReturnsDataNode() throws IOException {
        ProgramNode testProgram = parseStatements("data x, y, z");

        List<Node> nodes = new ArrayList<>();
        nodes.add(new VariableNode("x"));
        nodes.add(new VariableNode("y"));
        nodes.add(new VariableNode("z"));

        StatementNode statement = new DataNode(nodes);
        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void dataStatement_SingleVariable_ReturnsDataNode() throws IOException {
        ProgramNode testProgram = parseStatements("data x");
        // TODO, but "DATA" is a WORD, not a LABEL

        List<Node> nodes = new ArrayList<>();
        nodes.add(new VariableNode("x"));

        StatementNode statement = new DataNode(nodes);
        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void dataStatement_NoVariables_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            parseStatements("data");
        });
    }

    @Test
    public void dataStatement_InvalidTokens_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            parseStatements("data x, 123, z");
        });
    }

    @Test
    public void dataStatement_WhitespaceBetweenVariablesAndCommas_ReturnsDataNode() throws IOException {
        ProgramNode testProgram = parseStatements("data x , y , z");

        List<Node> nodes = new ArrayList<>();
        nodes.add(new VariableNode("x"));
        nodes.add(new VariableNode("y"));
        nodes.add(new VariableNode("z"));

        StatementNode statement = new DataNode(nodes);
        StatementsNode statements = new StatementsNode();
        statements.addStatement(statement);

        ProgramNode expectedProgram = new ProgramNode();
        expectedProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testPrintInputRead() throws IOException {
        LinkedList<Token> tokens =  lexer.lex("src/test/resources/input_test.txt");
        ProgramNode expectedProgram = new Parser(tokens).parse();

        InputNode inputNode = new InputNode(new ArrayList<>(
                Arrays.asList(new StringNode("What is your name and age?"),
                        new VariableNode("name$"),
                        new VariableNode("age")
                )
        ));

        StatementsNode statements = new StatementsNode();
        statements.addStatement(inputNode);

        ProgramNode testProgram = new ProgramNode();
        testProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testInputInvalid() throws IOException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            LinkedList<Token> tokens =  lexer.lex("src/test/resources/input_test_invalid.txt");
            ProgramNode program = new Parser(tokens).parse();
            System.out.println(program);
        });

        String expectedMessage = "Invalid Input Statement";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRead() throws IOException {
        LinkedList<Token> tokens =  lexer.lex("src/test/resources/read_test.txt");
        ProgramNode expectedProgram = new Parser(tokens).parse();

        ReadNode readNode = new ReadNode(new ArrayList<>(Arrays.asList(
                new VariableNode("a"), new VariableNode("a$")
        )));

        StatementsNode statements = new StatementsNode();
        statements.addStatement(readNode);

        ProgramNode testProgram = new ProgramNode();
        testProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }

    @Test
    public void testReadInvalid() throws IOException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            LinkedList<Token> tokens =  lexer.lex("src/test/resources/read_test_invalid.txt");
            ProgramNode program = new Parser(tokens).parse();
        });
    }

    @Test
    public void testPrintList() throws IOException {
        LinkedList<Token> tokens =  lexer.lex("src/test/resources/print_list.txt");
        ProgramNode expectedProgram = new Parser(tokens).parse();

        ExpressionNode expression1 = new ExpressionNode(new TermNode(new VariableNode("F%")));
        StringNode stringNode = new StringNode("DEG F = ");
        ExpressionNode expression2 = new ExpressionNode(new TermNode(new VariableNode("C%")));
        StringNode stringNode2 = new StringNode("DEG C");

        PrintNode printNode = new PrintNode(new ArrayList<>(
                Arrays.asList(expression1, stringNode, expression2, stringNode2)
        ));

        StatementsNode statements = new StatementsNode();
        statements.addStatement(printNode);

        ProgramNode testProgram = new ProgramNode();
        testProgram.addStatements(statements);

        assertEquals(expectedProgram, testProgram);
    }
}


