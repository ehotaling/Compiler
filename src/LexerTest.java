import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// LexerTest class contains unit tests for the Lexer class.
// It tests the Lexer's ability to tokenize various strings into correct sequences of tokens.
public class LexerTest {

    // Helper method to run the lexer on a given text.
    // Creates a temporary file with the text and uses the Lexer to tokenize it.
    private LinkedList<Token> runLexerOnText(String text) throws IOException {
        Path tempFilePath = Files.createTempFile("test.txt", ".txt");
        Files.writeString(tempFilePath, text);
        Lexer lexer = new Lexer();
        return lexer.lex(tempFilePath.toString());
    }

    // Tests the Lexer's ability to correctly tokenize multi-line strings.
    // Verifies if the Lexer correctly identifies words, numbers, and end-of-line tokens.
    @Test
    public void testMultiLineStrings() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("Hello\nWorld\n12345");
        assertEquals(new Token(Token.TokenType.WORD, "Hello", 1, 0), tokens.get(0));
        assertEquals(new Token(Token.TokenType.ENDOFLINE, 1, 5), tokens.get(1));
        assertEquals(new Token(Token.TokenType.WORD, "World", 2, 0), tokens.get(2));
        assertEquals(new Token(Token.TokenType.ENDOFLINE, 2, 5), tokens.get(3));
        assertEquals(new Token(Token.TokenType.NUMBER, "12345", 3, 0), tokens.get(4));
    }

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
    public void testStringLiteral() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("\"Hello there\"");
        assertEquals(new Token(Token.TokenType.STRINGLITERAL, "Hello there", 1, 0), tokens.get(0));
    }

    @Test
    public void testEmptyStringLiteral() throws IOException {
        LinkedList<Token> tokens = runLexerOnText("\"\"");
        assertEquals(new Token(Token.TokenType.STRINGLITERAL, "", 1, 0), tokens.get(0));
    }
}