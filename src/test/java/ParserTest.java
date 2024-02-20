import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    public void testAddExpression() throws IOException {
        ProgramNode program =  runParserOnText("1+2");
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
}
