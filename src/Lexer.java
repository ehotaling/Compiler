import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

// Lexer class is responsible for tokenizing source code into a series of tokens.
// This class reads through the source code and identifies different elements like words, numbers, and special characters.
public class Lexer {

    // Handles the reading and navigation of the source code.
    private CodeHandler handler;

    // Tracks the current line number in the source code.
    private int lineNo;

    // Tracks the current position within the current line.
    private int position;

    // Represents a HashMap that stores known words and their corresponding token types.
    private final HashMap<String, Token.TokenType> knownWords;


    // Represents a HashMap that stores one-character symbols and their corresponding TokenType.
    private final HashMap<String, Token.TokenType> oneCharacterSymbols;

    // Stores a mapping of two-character symbols to their corresponding TokenTypes.
    private final HashMap<String, Token.TokenType> twoCharacterSymbols;

    // Constructor for the Lexer. Initializes the lexer with the source code file.
    // throws RuntimeException if an IOException occurs while reading the file.
    public Lexer() {
        knownWords = new HashMap<>();
        populateKnownWords();
        oneCharacterSymbols = new HashMap<>();
        populateOneCharacterSymbols();
        twoCharacterSymbols = new HashMap<>();
        populateTwoCharacterSymbols();
    }

    // Populates the twoCharacterSymbols HashMap with predefined two-character symbols and their corresponding token types.
    // The keys represent the symbol characters, and the values represent the corresponding TokenType enum.
    private void populateTwoCharacterSymbols() {
        twoCharacterSymbols.put("<=", Token.TokenType.LESSTHANEQUALTO);
        twoCharacterSymbols.put(">=", Token.TokenType.GREATERTHANEQUALTO);
        twoCharacterSymbols.put("<>", Token.TokenType.NOTEQUALS);
    }

    // Populates the oneCharacterSymbols HashMap with predefined one-character symbols and their corresponding token types.
    // The keys represent the symbol characters, and the values represent the corresponding TokenType enum.
    private void populateOneCharacterSymbols() {
        oneCharacterSymbols.put("=", Token.TokenType.EQUALS);
        oneCharacterSymbols.put("<", Token.TokenType.LESSTHAN);
        oneCharacterSymbols.put(">", Token.TokenType.GREATERTHAN);
        oneCharacterSymbols.put("(", Token.TokenType.LPAREN);
        oneCharacterSymbols.put(")", Token.TokenType.RPAREN);
        oneCharacterSymbols.put("+", Token.TokenType.PLUS);
        oneCharacterSymbols.put("-", Token.TokenType.MINUS);
        oneCharacterSymbols.put("*", Token.TokenType.MULTIPLY);
        oneCharacterSymbols.put("/", Token.TokenType.DIVIDE);
    }

     // Populates the knownWords HashMap with predefined words and their corresponding token types.
    private void populateKnownWords() {
        knownWords.put("if", Token.TokenType.IF);
        knownWords.put("print", Token.TokenType.PRINT);
        knownWords.put("read", Token.TokenType.READ);
        knownWords.put("input", Token.TokenType.INPUT);
        knownWords.put("data", Token.TokenType.DATA);
        knownWords.put("gosub", Token.TokenType.GOSUB);
        knownWords.put("for", Token.TokenType.FOR);
        knownWords.put("to", Token.TokenType.TO);
        knownWords.put("step", Token.TokenType.STEP);
        knownWords.put("next", Token.TokenType.NEXT);
        knownWords.put("return", Token.TokenType.RETURN);
        knownWords.put("then", Token.TokenType.THEN);
        knownWords.put("function", Token.TokenType.FUNCTION);
        knownWords.put("while", Token.TokenType.WHILE);
        knownWords.put("end", Token.TokenType.END);
    }


    /*
     * Process a word token.
     * This method reads characters from the source code and constructs a word token
     * by appending valid characters to a StringBuilder. It checks if the constructed token
     * is present in the knownWords map. If it is, it creates a Token object with the corresponding
     * TokenType, line number, and position. If the token is not known, it creates a Token object
     * with TokenType.WORD and the word as its value.
     */
    private Token processWord() {
        StringBuilder tokenBuilder = new StringBuilder();
        char currentChar = handler.peek(0);
        if (Character.isLetter(currentChar)) { // Checking for a Letter only at beginning
            // Building the token with a starting letter
            tokenBuilder.append(handler.getChar());
            position++;
        } else {
            throw new RuntimeException("Unrecognized word start: " + currentChar);
        }
        // Continue to append the remaining characters to form the complete token.
        appendTokenCharacters(tokenBuilder);
        // Convert the tokenBuilder to a String and check if it's in the knownWords map.
        String token = tokenBuilder.toString();
        if (knownWords.containsKey(token)) { //If so, return a Token with corresponding TokenType, lineNo, position.
            return new Token(knownWords.get(token), lineNo, position);
        } else if (token.charAt(token.length() - 1) == ':') {
            return new Token(Token.TokenType.LABEL, token, lineNo,
                    position - tokenBuilder.length());
        } else { // If not, create a new WORD Token with the word as its value.
            return new Token(Token.TokenType.WORD, token, lineNo,
                    position - tokenBuilder.length());
        }


    }


    // Appends valid token characters to the given tokenBuilder.
    private void appendTokenCharacters(StringBuilder tokenBuilder) {
        char currentChar;
        while (!handler.isDone()) {
            currentChar = handler.peek(0);
            // Checking for valid characters for a Java Identifier (Letter, Digit, _, $, %)
            if (Character.isLetterOrDigit(currentChar) || currentChar == '_' || currentChar == '$' ||
                    currentChar == '%' || currentChar == ':') {
                // Append the character and update position
                tokenBuilder.append(handler.getChar());
                position++;
                // If a token has ended with $, %, or : , stop appending more characters
                if (currentChar == '$' || currentChar == '%' || currentChar == ':') {
                    break; // breaking after appending $, %, or :
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
        return new Token(Token.TokenType.NUMBER, numberBuilder.toString(), lineNo,
                position - numberBuilder.length());
    }

    /*
     * Handles whitespace characters in the source code.
     * If the current character is a space character, it advances the position by one.
     * If the current character is a newline character, it adds an ENDOFLINE token to the list of tokens
     * and updates the line number and position values accordingly.
     * If the current character is a carriage return character, it does nothing.
     */
    private void handleWhitespace(char curChar, LinkedList<Token> tokens) {
        if (Character.isSpaceChar(curChar)) {
            position++;
        } else if (curChar == '\n') { // If current character is a newline character add a ENDOFLINE token to token list.
            tokens.add(new Token(Token.TokenType.ENDOFLINE, lineNo, position));
            lineNo++;
            position = 0;
        } else if (curChar == '\r') {
            // Do nothing.
        }
        handler.getChar();
    }


    // This method handles a string literal token in the source code. It reads characters until it encounters
    // a closing double quote ("). It takes care of escape sequences and constructs the string literal value.
    // If it encounters an unterminated string literal, it throws a RuntimeException.
    private Token HandleStringLiteral() {
        StringBuilder stringLiteralBuilder = new StringBuilder();
        char currentChar;
        boolean escapeNextChar = false;
        handler.getChar();
        position++;
        while (!handler.isDone()) {
            currentChar = handler.getChar();
            position++;
            if (escapeNextChar) {
                stringLiteralBuilder.append(currentChar);
                escapeNextChar = false;
            } else if (currentChar == '\\') {
                escapeNextChar = true;
            } else if (currentChar == '\"') {
                return new Token(Token.TokenType.STRINGLITERAL, stringLiteralBuilder.toString(), lineNo,
                        position - stringLiteralBuilder.length()- 2); // To account for the pair of missing quotes
            } else {
                stringLiteralBuilder.append(currentChar);
            }

        }
        throw new RuntimeException("Unterminated string literal at line " + lineNo);
    }

    /*
     * This method processes a symbol character and returns a Token object representing the symbol.
     * It checks if the symbol is a two-character symbol or a one-character symbol
     * and returns the corresponding TokenType and symbol value.
     * This method can throw a RuntimeException if the symbol character is not recognized.
     */
    private Token processSymbol() {
        StringBuilder symbolBuilder = new StringBuilder();
        while (!handler.isDone()) {

            if (twoCharacterSymbols.containsKey(handler.peek(0) + "" + handler.peek(1))) {
                symbolBuilder.append(handler.getChar());
                symbolBuilder.append(handler.getChar());
                position += 2;
                return new Token(twoCharacterSymbols.get(symbolBuilder.toString()), symbolBuilder.toString(), lineNo, position - symbolBuilder.length());
            } else if (oneCharacterSymbols.containsKey(handler.peek(0) + "")) {
                symbolBuilder.append(handler.getChar());
                position++;
                return new Token(oneCharacterSymbols.get(symbolBuilder.toString()), symbolBuilder.toString(), lineNo, position - symbolBuilder.length());

            }
        }
        throw new RuntimeException("Undetermined symbol at line " + lineNo);
    }

    // Performs the lexing of the source code.
    // It reads the source code character by character, identifying and collecting tokens.
    // Returns a LinkedList of tokens identified in the source code.
    public LinkedList<Token> lex(String filename) {
        // Stores the identified tokens.
        LinkedList<Token> tokens;
        try {
            handler = new CodeHandler(filename);
            tokens = new LinkedList<>();
            lineNo = 1;
            position = 0;
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }

        while (!handler.isDone()) {
            char curChar = handler.peek(0);
            if (Character.isWhitespace(curChar)) {
                handleWhitespace(curChar, tokens);
            } else if (Character.isLetter(curChar)) {
                tokens.add(processWord());
            } else if (Character.isDigit(curChar) || (curChar == '.' && Character.isDigit(handler.peek(1)))) {
                tokens.add(processNumber());

            } else if (curChar == '\"') {
                tokens.add(HandleStringLiteral());

            }  else {
                tokens.add(processSymbol());
            }
        }

        // If the token list is not empty and the last token is not an end of line token,
        // add an end of line token at the current line number and character position to the list of tokens.
        if (!tokens.isEmpty() && tokens.getLast().getTokenType() != Token.TokenType.ENDOFLINE) {
            tokens.add(new Token(Token.TokenType.ENDOFLINE, lineNo, position));
        }

        return tokens;
    }

}

