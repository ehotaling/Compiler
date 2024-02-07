import java.util.LinkedList;

// Entry point for the lexer program.
// It processes a file to tokenize its content and prints out the tokens.
public class Basic {

    // Starting point to initiate the tokenization process.
    // Command line arguments expect single argument: the file name which has to be tokenized.
    public static void main(String[] args) {

        // Validate that exactly one argument (filename) is provided
        if (args.length != 1) {
            System.out.println("Error: A single filename is required as an argument.");
            System.exit(1); //  Exiting with an error status
        }

        Lexer lexer = new Lexer();

        // Perform lexical analysis on the file and store the resulting tokens
        LinkedList<Token> tokens = lexer.lex(args[0]);

        // Iterating through each of the tokens and printing their string representation
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }
}
