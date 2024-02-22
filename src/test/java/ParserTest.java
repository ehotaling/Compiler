import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The LexerTest class is responsible for testing the lexer functionality.
 */
public class ParserTest {

    Lexer lexer = new Lexer();

    Parser parser;

    /**
     * Runs the lexer on the given text and returns a list of tokens identified in the source code.
     *
     * @param text The text to be lexed.
     * @return A LinkedList of tokens identified in the source code.
     * @throws IOException If an I/O error occurs while reading the text.
     */
    private ProgramNode runParserOnText(String text) throws IOException {
        Path tempFilePath = Files.createTempFile("temp.bas", ".txt");
        Files.writeString(tempFilePath, text);
        LinkedList<Token> tokens = lexer.lex(tempFilePath.toString());

        return new Parser(tokens).parse();
    }

    @Test
    public void testExpression() throws IOException {
        ProgramNode program =  runParserOnText("1");
        System.out.println(program);
    }

    @Test
    public void testAddExpression() throws IOException {
        ProgramNode program =  runParserOnText("1+2");
        System.out.println(program);
    }

    @Test
    public void testSubtractExpression() throws IOException {
        ProgramNode program =  runParserOnText("1-2");
        System.out.println(program);
    }

    @Test
    public void testExpressionThreeTerms() throws IOException {
        ProgramNode program =  runParserOnText("1+2+3");
        System.out.println(program);
    }

    @Test
    public void testTermWithThreeFactors() throws IOException {
        ProgramNode program =  runParserOnText("2*2*2");
        System.out.println(program);
    }

    @Test
    public void testAddExpressionAndDivideTerm() throws IOException {
        ProgramNode program =  runParserOnText("1+2/2");
        System.out.println(program);
    }

    @Test
    public void testMultiFactorExpression() throws IOException {
        ProgramNode program =  runParserOnText("6/2*(1+2)");
        System.out.println(program);
    }

    @Test
    public void testMultiplyTermWithAddExpression() throws IOException {
        ProgramNode program =  runParserOnText("3*5+2");
        System.out.println(program);
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
        assertTrue(parser.acceptSeperators(), "The method did not correctly accept multiple separators.");

        // No ENDOFLINE tokens exist at this point, the method should return false
        assertFalse(parser.acceptSeperators(), "The method did not correctly handle the case when no separators exist.");
    }

    // Test for the acceptSeparators method in the Parser class when no ENDOFLINE tokens exist.
    @Test
    public void testAcceptSeparatorsNoEndOfLine() {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "3",1,0));
        Parser parser = new Parser(tokens);

        // No ENDOFLINE tokens exist, the method should return false
        assertFalse(parser.acceptSeperators(), "The method did not correctly handle the case when no separators exist.");
    }

    // Test for the acceptSeparators method in the Parser class when only one ENDOFLINE token exists.
    @Test
    public void testAcceptSeparatorsSingleEndOfLine() {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.ENDOFLINE, "\n", 1, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5",2,1));
        Parser parser = new Parser(tokens);

        // Single ENDOFLINE token exists, the method should return true
        assertTrue(parser.acceptSeperators(), "The method did not correctly accept a single separator.");
    }
}


