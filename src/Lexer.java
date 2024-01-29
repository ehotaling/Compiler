import java.io.IOException;
import java.util.LinkedList;

// Lexer class is responsible for tokenizing source code into a series of tokens.
// This class reads through the source code and identifies different elements like words, numbers, and special characters.
public class Lexer {

    private CodeHandler handler; // Handles the reading and navigation of the source code.
    private LinkedList<Token> tokens; // Stores the identified tokens.
    private int lineNo; // Tracks the current line number in the source code.
    private int position; // Tracks the current position within the current line.

    // Constructor for the Lexer. Initializes the lexer with the source code file.
    // throws RuntimeException if an IOException occurs while reading the file.
    public Lexer(String filename) {
        try {
            handler = new CodeHandler(filename);
            tokens = new LinkedList<>();
            lineNo = 1;
            position = 0;
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }
    }

    // The 'processWord' function starts the identification of a word token.
    private Token processWord() {
        StringBuilder tokenBuilder = new StringBuilder();
        char currentChar = handler.peek(0);
        if (Character.isLetter(currentChar)) { // Checking for a Letter only at beginning
            // Building the token with a starting letter
            tokenBuilder = appendCharFromHandler(tokenBuilder);
        } else {
            throw new RuntimeException("Unrecognized word start: " + currentChar);
        }
        // Continue to append the remaining characters to form the complete token.
        appendTokenCharacters(tokenBuilder);
        return new Token(TokenType.WORD, tokenBuilder.toString(), lineNo, position - tokenBuilder.length());
    }

    // The 'appendCharFromHandler' function is for incrementing character and updating position counter.
    private StringBuilder appendCharFromHandler(StringBuilder tokenBuilder) {
        tokenBuilder.append(handler.getChar());
        position++;
        return tokenBuilder;
    }

    // The 'appendTokenCharacters' appends valid token characters till a non-valid character or end.
    private void appendTokenCharacters(StringBuilder tokenBuilder) {
        char currentChar;
        while (!handler.isDone()) {
            currentChar = handler.peek(0);
            // Checking for valid characters for a Java Identifier (Letter, Digit, _, $, %)
            if (Character.isLetterOrDigit(currentChar) || currentChar == '_' || currentChar == '$' || currentChar == '%') {
                // Append the character and update position
                tokenBuilder = appendCharFromHandler(tokenBuilder);
                // If a token has ended with $ or %, stop appending more characters
                if (currentChar == '$' || currentChar == '%') {
                    break; // breaking after appending $ or %
                }
            } else {
                // If an invalid character for a Java Identifier is encountered, stop appending
                break;
            }
        }
    }

    // Processes a sequence of numerical characters into a Token object.
    // Handles decimals (accepting only one per number).
    // The process stops when it encounters a whitespace or any non-numeric character.
    private Token processNumber() {
        StringBuilder numberBuilder = new StringBuilder();
        boolean decimalFound = false;
        char currentChar;
        while (!handler.isDone()) {
            currentChar = handler.peek(0);
            // Stop when a whitespace or a second dot is encountered.
            if (Character.isWhitespace(currentChar) || (currentChar == '.' && decimalFound)) {
                break;
            }
            numberBuilder.append(currentChar);
            if (currentChar == '.') {
                decimalFound = true;
            }
            handler.getChar();
            position++;
        }
        return new Token(TokenType.NUMBER, numberBuilder.toString(), lineNo, position - numberBuilder.length());
    }

    // Performs the lexing of the source code.
    // It reads the source code character by character, identifying and collecting tokens.
    // Returns a LinkedList of tokens identified in the source code.
    public LinkedList<Token> lex() {
        while (!handler.isDone()) {
            char curChar = handler.peek(0);
            if (Character.isWhitespace(curChar)) {
                if (!Character.isSpaceChar(curChar)) {
                    if (curChar == '\n') {
                        tokens.add(new Token(TokenType.ENDOFLINE, lineNo, position));
                        lineNo++;
                        position = 0;
                    }
                } else {
                    position++; // Increment position for spaces.
                }
                handler.getChar();
                continue;
            }
            else if (Character.isLetter(curChar)) {
                tokens.add(processWord());
            } else if (Character.isDigit(curChar)) {
                tokens.add(processNumber());
            } else {
                throw new RuntimeException("Unrecognized character: " + curChar);
            }
        }

        // Add end-of-line token if the last token is not an end-of-line.
        if (!tokens.isEmpty() && tokens.getLast().getTokenType() != TokenType.ENDOFLINE) {
            tokens.add(new Token(TokenType.ENDOFLINE, lineNo, position));
        }

        return tokens;
    }
}