import java.util.ArrayList;
import java.util.List;

public class LexerStateMachine {
    private static final int STATE_START = 0;
    private static final int STATE_IN_WORD = 1;

    enum Token2Type {
        WORD
    }

    class Token2 {
        private final Token2Type type;
        private final String value;

        public Token2(Token2Type type, String value) {
            this.type = type;
            this.value = value;
        }

        public Token2Type getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Token2{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public List<Token2> tokenize(String input) {
        List<Token2> tokens = new ArrayList<>();
        StringBuilder tokenBuilder = new StringBuilder();
        int currentState = STATE_START;

        for (char c : input.toCharArray()) {
            switch (currentState) {
                case STATE_START:
                    if (Character.isLetterOrDigit(c)) {
                        tokenBuilder.append(c);
                        currentState = STATE_IN_WORD;
                    }
                    break;
                case STATE_IN_WORD:
                    if (Character.isLetterOrDigit(c)) {
                        tokenBuilder.append(c);
                    } else {
                        tokens.add(new Token2(Token2Type.WORD, tokenBuilder.toString()));
                        tokenBuilder.setLength(0);
                        currentState = STATE_START;
                    }
                    break;
            }
        }

        // Add the last token if there's one left
        if (currentState == STATE_IN_WORD) {
            tokens.add(new Token2(Token2Type.WORD, tokenBuilder.toString()));
        }

        return tokens;
    }

    public static void example(String[] args) {
        String input = "Hello123 World! This is a simple lexer123.";

        LexerStateMachine lexer = new LexerStateMachine();
        List<Token2> tokens = lexer.tokenize(input);

        for (Token2 token : tokens) {
            System.out.println(token);
        }
    }
}
