import java.util.LinkedList;

// Entry point for the lexer program.
// It processes a file to tokenize its content and prints out the tokens.
public class Basic {

    // Starting point to initiate the tokenization process.
    // Command line arguments, expects single argument: the file name which has to be tokenized.
    public static void main(String[] args) {

        // Validate that exactly one argument (filename) is provided
        if (args.length != 1) {
            System.out.println("Error: A single filename is required as an argument.");
            System.exit(1); // Cleanly exiting with an error status
        }

        // Initialize Lexer with the provided filename
        Lexer lexer = new Lexer(args[0]);

        // Perform lexical analysis on the file and store the resulting tokens
        LinkedList<Token> tokens = lexer.lex();

        // Iterating through each of the tokens and printing their string representation
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }
}
