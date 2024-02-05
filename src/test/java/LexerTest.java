import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


// LexerTest class contains unit tests for the Lexer class.
// It tests the Lexer's ability to tokenize various strings into correct sequences of tokens.
public class LexerTest {

    Lexer lexer = new Lexer();

    // Helper method to run the lexer on a given text.
    // Creates a temporary file with the text and uses the Lexer to tokenize it.
    private LinkedList<Token> runLexerOnText(String text) throws IOException {
        Path tempFilePath = Files.createTempFile("test.txt", ".txt");
        Files.writeString(tempFilePath, text);
        return lexer.lex(tempFilePath.toString());
    }

    /**
     * Newlines generally cannot be escaped in BASIC
     * @throws IOException
     */
    // Tests the Lexer's ability to correctly tokenize multi-line strings.
    // Verifies if the Lexer correctly identifies words, numbers, and end-of-line tokens.
//    @Test
//    public void testMultiLineStrings() throws IOException {
//        LinkedList<Token> tokens = runLexerOnText("Hello\nWorld\n12345");
//        assertEquals(new Token(Token.TokenType.WORD, "Hello", 1, 0), tokens.get(0));
//        assertEquals(new Token(Token.TokenType.ENDOFLINE, 1, 5), tokens.get(1));
//        assertEquals(new Token(Token.TokenType.WORD, "World", 2, 0), tokens.get(2));
//        assertEquals(new Token(Token.TokenType.ENDOFLINE, 2, 5), tokens.get(3));
//        assertEquals(new Token(Token.TokenType.NUMBER, "12345", 3, 0), tokens.get(4));
//    }

    // Tests the Lexer's ability to tokenize a string containing words followed by numbers.
    // Verifies if words and numbers are correctly identified and tokenized.
    @Test
    public void testWordsThenNumbers() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("Hello 12345");
        assertEquals(new Token(Token.TokenType.WORD, "Hello", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.NUMBER, "12345", 1, 6), tokens.get(1));
    }

    // Tests the Lexer's ability to tokenize a string containing numbers followed by words.
    // Verifies if numbers and words are correctly identified and tokenized in sequence.
    @Test
    public void testNumbersThenWords() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("12345 Hello");
        assertEquals(new Token(Token.TokenType.NUMBER, "12345", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.WORD, "Hello", 1, 6), tokens.get(1));
    }

    @Test
    public void testNumberWithNewline() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("12345\n");
        assertEquals(new Token(Token.TokenType.NUMBER, "12345", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.ENDOFLINE, 1, 5), tokens.get(1));
    }

    @Test
    public void testNumberWithDecimal() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("123.45");
        assertEquals(new Token(Token.TokenType.NUMBER, "123.45", 1, 0), tokens.get(0));
    }

    @Test
    public void testInvalidNumber() throws IOException {
        assertThrows(IllegalStateException.class, () -> runLexerOnText("123_345"));
        assertThrows(IllegalStateException.class, () -> runLexerOnText("123.34.55"));
        assertThrows(IllegalStateException.class, () -> runLexerOnText("123:"));
    }

    @Test
    public void testNumberWithNewlines() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("12345 \n 54321");
        assertEquals(new Token(Token.TokenType.NUMBER, "12345", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.ENDOFLINE, 1, 6), tokens.get(1));
        assertEquals(new Token(Token.TokenType.NUMBER, "54321", 2, 1), tokens.get(2));
    }

    @Test
    public void testStringLiteral() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("\"Hello there\"");
        assertEquals(new Token(Token.TokenType.STRINGLITERAL, "\"Hello there\"", 1, 0), tokens.get(0));
    }

    @Test
    public void testStringLiteralEscapedQuotes() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("\"Hello there\" this is a string");
        assertEquals(new Token(Token.TokenType.STRINGLITERAL, "\"Hello there\" this is a string", 1, 0), tokens.get(0));
    }

    @Test
    public void testEmptyStringLiteral() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("\"\"");
        assertEquals(new Token(Token.TokenType.STRINGLITERAL, "\"\"", 1, 0), tokens.get(0));
    }

    @Test
    public void testStringLiteralWithoutMatchingQuote() throws IOException {
        assertThrows(IllegalStateException.class, () -> runLexerOnText("\""));
        assertThrows(IllegalStateException.class, () -> runLexerOnText("abcd \"Hello there"));
        assertThrows(IllegalStateException.class, () -> runLexerOnText("abcd \"Hello there\"\""));
        assertThrows(IllegalStateException.class, () -> runLexerOnText("Hello \""));
    }

    @Test
    public void testProcessSymbol() throws IOException {
        String text = "<= >= <> = < > ( ) + - * ///";
        LinkedList<Token> tokens = runLexerOnText(text);
        assertEquals(new Token(Token.TokenType.LESSTHANEQUALTO, "<=", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.GREATERTHANEQUALTO, ">=", 1, 3), tokens.get(1));
        assertEquals(new Token(Token.TokenType.NOTEQUALS, "<>", 1, 6), tokens.get(2));
        assertEquals(new Token(Token.TokenType.EQUALS, "=", 1, 9), tokens.get(3));
        assertEquals(new Token(Token.TokenType.LESSTHAN, "<", 1, 11), tokens.get(4));
        assertEquals(new Token(Token.TokenType.GREATERTHAN, ">", 1, 13), tokens.get(5));
        assertEquals(new Token(Token.TokenType.LPAREN, "(", 1, 15), tokens.get(6));
        assertEquals(new Token(Token.TokenType.RPAREN, ")", 1, 17), tokens.get(7));
        assertEquals(new Token(Token.TokenType.PLUS, "+", 1, 19), tokens.get(8));
        assertEquals(new Token(Token.TokenType.MINUS, "-", 1, 21), tokens.get(9));
        assertEquals(new Token(Token.TokenType.MULTIPLY, "*", 1, 23), tokens.get(10));
        assertEquals(new Token(Token.TokenType.DIVIDE, "/", 1, 25), tokens.get(11));
    }

    // ERIC: paranthesis are not generally used in BASIC except in function calls like Print()
    // Not sure what the requirements are for this
    public void testNumberInParantheses() throws IOException {
        String text = "(4 + 2)\n";
        LinkedList<Token> tokens = runLexerOnText(text);
        assertEquals(new Token(Token.TokenType.LPAREN, "(", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.NUMBER, "4", 1, 1), tokens.get(1));
        assertEquals(new Token(Token.TokenType.PLUS, "+", 1, 3), tokens.get(2));
        assertEquals(new Token(Token.TokenType.NUMBER, "2", 1, 5), tokens.get(3));
        assertEquals(new Token(Token.TokenType.RPAREN, ")", 1, 6), tokens.get(4));
    }

    @Test
    public void comparisonStatement() throws IOException {
        String text = "4 <= 4.5\n";
        LinkedList<Token> tokens = runLexerOnText(text);
        assertEquals(new Token(Token.TokenType.NUMBER, "4", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.LESSTHANEQUALTO, "<=", 1, 2), tokens.get(1));
        assertEquals(new Token(Token.TokenType.NUMBER, "4.5", 1, 5), tokens.get(2));
        assertEquals(new Token(Token.TokenType.ENDOFLINE, 1, 8), tokens.get(3));
    }

    @Test
    public void testLabels() throws IOException {
        String text = "Hello: World: Testing testing";
        LinkedList<Token> tokens = runLexerOnText(text);
        assertEquals(new Token(Token.TokenType.LABEL, "Hello:", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.LABEL, "World:", 1, 7), tokens.get(1));
    }

    @Test
    public void testInvalidSymbol() throws IOException {
        assertThrows(IllegalStateException.class, () -> runLexerOnText("InvalidSymbol:: World: Testing testing"));
        assertThrows(IllegalStateException.class, () -> runLexerOnText(":: World: Testing testing"));
    }

    @Test
    public void testHelloWorld() throws IOException {
        LinkedList<Token> tokensList =  lexer.lex("src/test/resources/hello_world.bas");
        assertThrows(IllegalStateException.class, () -> runLexerOnText("InvalidSymbol:: World: Testing testing"));
        assertThrows(IllegalStateException.class, () -> runLexerOnText(":: World: Testing testing"));
    }
}