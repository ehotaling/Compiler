import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * The Basic class is the entry point for the lexer program. It processes
 * a file to tokenize its content and prints out the tokens.
 */
public class Basic {

    /**
     * Starting point to initiate the tokenization process.
     *
     * @param args Command line arguments
     *             - Expects a single argument: the file name which has to be tokenized.
     */
    public static void main(String[] args) {

        // Validate that exactly one argument (filename) is provided
        if (args.length != 1) {
            System.out.println("Error: A single filename is required as an argument.");
            System.exit(1); //  Exiting with an error status
        }

        if (args[0].equals("-interactive") || args[0].equals("-i")) {
            runInteractiveInterpreter();
        }

        Lexer lexer = new Lexer();

        // Perform lexical analysis on the file and store the resulting tokens
        LinkedList<Token> tokens = lexer.lex(args[0]);

        // Iterating through each of the tokens and printing their string representation
        for (Token token : tokens) {
            System.out.println(token.toString());
        }

        Parser parser = new Parser(tokens);

        Node ast = parser.parse();

        System.out.println(ast.toString());
    }

    private static void runInteractiveInterpreter() {
        List<String> program = new ArrayList<>();

        String input;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("RUN")) {
                break;
            } else if (input.equalsIgnoreCase("EXIT")) {
                System.exit(0);
            }
            program.add(input);
        }

        System.out.println("\nExecuting Program...");
        if (program.isEmpty()) {
            System.exit(0);
        }

        LinkedList<Token> tokens;
        try {
            tokens = runLexerOnText(String.join("\n", program), new Lexer());

            Parser parser = new Parser(tokens);
            ProgramNode programNode = parser.parse();

            Interpreter interpreter = new Interpreter(programNode);
            interpreter.interpret();

            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    private static LinkedList<Token> runLexerOnText(String text, Lexer lexer) throws IOException {
        Path tempFilePath = Files.createTempFile("interactive_program", ".txt");
        Files.writeString(tempFilePath, text);
        return lexer.lex(tempFilePath.toString());
    }
}
