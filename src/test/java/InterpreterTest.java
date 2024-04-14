import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    Lexer lexer = new Lexer();

    @Test
    public void testDataAndLabelProcessing() throws IOException {
        // Create a mock ProgramNode with some DATA statements and labels
        ProgramNode programNode = new ProgramNode();
        StatementsNode statementsNode = new StatementsNode();
        List<Node> data =  new ArrayList<>();
        data.add(new IntegerNode(1));
        data.add(new IntegerNode(2));
        data.add(new IntegerNode(3));
        DataNode dataNode = new DataNode(data);
        statementsNode.addStatement(dataNode);

        List<Node> printList = new ArrayList<>();
        printList.add(new StringNode("Hello World"));
        PrintNode printNode = new PrintNode(printList);
        statementsNode.addStatement(printNode);
        LabeledStatementNode labeledStatementNode = new LabeledStatementNode(
                "label:", statementsNode.getStatements().get(1));
        statementsNode.addStatement(labeledStatementNode);
        programNode.addStatements(statementsNode);

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(programNode);
        interpreter.visitStatements();

        // Check that the data and labels were correctly stored
        HashMap<String, LabeledStatementNode> labels = interpreter.getLabels();
        Queue<DataNode> dataQueue = interpreter.getDataQueue();

        assertEquals(1, labels.size());
        assertTrue(labels.containsKey("label:"));
        assertEquals(labeledStatementNode, labels.get("label:"));

        assertEquals(1, dataQueue.size());
        assertEquals(dataNode, dataQueue.peek());

    }

    @Test
    public void testDataAndLabelProcessingFromFile()  throws IOException {
        LinkedList<Token> tokens = lexer.lex("src/test/resources/data_and_label.bas");
        ProgramNode actualProgram = new Parser(tokens).parse();

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(actualProgram);
        interpreter.visitStatements();

        HashMap<String, LabeledStatementNode> labels = interpreter.getLabels();
        Queue<DataNode> dataQueue = interpreter.getDataQueue();

        LabeledStatementNode labeledStatementNode = new LabeledStatementNode(
                "beginning:", new PrintNode(List.of(new StringNode("Hello!"))));

        assertEquals(1, labels.size());
        assertTrue(labels.containsKey("beginning:"));
        assertEquals(labeledStatementNode, labels.get("beginning:"));
        assertEquals(1, dataQueue.size());
        assertEquals(3, dataQueue.peek().getData().size());
        assertEquals(10, ((IntegerNode) dataQueue.peek().getData().get(0)).getInt());
        assertEquals("mphipps", ((StringNode) dataQueue.peek().getData().get(1)).getValue());
        assertEquals(10.0f, ((FloatNode) dataQueue.peek().getData().get(2)).getFloat());
    }

    @Test
    public void testVariableStorage() throws IOException {
        // Create a mock ProgramNode with some variable assignments
        ProgramNode programNode = new ProgramNode();
        StatementsNode statementsNode = new StatementsNode();
        AssignmentNode intAssignment = new AssignmentNode(
                new VariableNode("x"), new IntegerNode(5));
        AssignmentNode floatAssignment = new AssignmentNode(
                new VariableNode("y%"), new FloatNode(5.0f));
        AssignmentNode stringAssignment = new AssignmentNode(
                new VariableNode("z$"), new StringNode("Hello World"));
        statementsNode.addStatement(intAssignment);
        statementsNode.addStatement(floatAssignment);
        statementsNode.addStatement(stringAssignment);
        programNode.addStatements(statementsNode);

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(programNode);
        interpreter.visitStatements();

        // Check that the variables were correctly stored
        HashMap<String, Integer> intVariables = interpreter.getIntVariables();
        HashMap<String, Float> floatVariables = interpreter.getFloatVariables();
        HashMap<String, String> stringVariables = interpreter.getStringVariables();

//        assertEquals(1, intVariables.size());
//        assertEquals(1, floatVariables.size());
//        assertEquals(1, stringVariables.size());
//        assertEquals(intVariables.get("x"), 5);
//        assertEquals(floatVariables.get("y%"), 5.0f);
//        assertEquals(stringVariables.get("z$"), "Hello World");

    }

    @Test
    public void testVariableStorageFromFile() throws IOException {
        LinkedList<Token> tokens = lexer.lex("src/test/resources/variable_storage.bas");
        ProgramNode actualProgram = new Parser(tokens).parse();

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(actualProgram);
        interpreter.visitStatements();

        HashMap<String, Integer> intVariables = interpreter.getIntVariables();
        HashMap<String, Float> floatVariables = interpreter.getFloatVariables();
        HashMap<String, String> stringVariables = interpreter.getStringVariables();

        // TODO, add test once evaluate is finished

//        assertEquals(1, intVariables.size());
//        assertEquals(1, floatVariables.size());
//        assertEquals(1, stringVariables.size());
//        assertEquals(intVariables.get("x"), 5);
//        assertEquals(floatVariables.get("y%"), 5.0f);
//        assertEquals(stringVariables.get("z$"), "Hello World");
    }

    @Test
    public void testStringVariableStorage() throws IOException {
        LinkedList<Token> tokens = lexer.lex("src/test/resources/string_storage.bas");
        ProgramNode actualProgram = new Parser(tokens).parse();

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(actualProgram);
        interpreter.visitStatements();

        HashMap<String, String> stringVariables = interpreter.getStringVariables();

//        assertEquals(1, stringVariables.size());
//        assertEquals("Eric", stringVariables.get("name$"));
    }

    @Test
    public void testIntVariableStorage() throws IOException {
        LinkedList<Token> tokens = lexer.lex("src/test/resources/int_storage.bas");
        ProgramNode actualProgram = new Parser(tokens).parse();

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(actualProgram);
        interpreter.visitStatements();

        HashMap<String, Integer> intVariables = interpreter.getIntVariables();

//        assertEquals(3, intVariables.size());
//        assertEquals(28, intVariables.get("age"));
//        assertEquals(28, intVariables.get("age2"));
//        assertEquals(20, intVariables.get("age3"));
//        assertEquals(25, intVariables.get("age4"));
    }


    @Test
    public void testFunctionsFromFile() throws IOException {
        LinkedList<Token> tokens = lexer.lex("src/test/resources/function_storage.bas");
        ProgramNode actualProgram = new Parser(tokens).parse();

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(actualProgram);
        interpreter.visitStatements();

        HashMap<String, Integer> intVariables = interpreter.getIntVariables();
        HashMap<String, Float> floatVariables = interpreter.getFloatVariables();
        HashMap<String, String> stringVariables = interpreter.getStringVariables();

//        // Testing RANDOM() function
//        assertTrue(intVariables.containsKey("randomNumber"));
//        assertNotNull(intVariables.get("randomNumber"));
//
//        // Testing LEFT$() function
//        assertTrue(stringVariables.containsKey("leftString$"));
//        assertEquals("HEL", stringVariables.get("leftString$"));
//
//        // Testing RIGHT$() function
//        assertTrue(stringVariables.containsKey("rightString$"));
//        assertEquals("LO", stringVariables.get("rightString$"));
//
//        // Testing MID$() function
//        assertTrue(stringVariables.containsKey("midString$"));
//        assertEquals("ban", stringVariables.get("midString$"));
//
//        // Testing NUM$() function
//        assertTrue(stringVariables.containsKey("numToString$"));
//        assertEquals("5", stringVariables.get("numToString$"));
//
//        // Testing VAL() function
//        assertTrue(intVariables.containsKey("stringToNum"));
//        assertEquals(5, intVariables.get("stringToNum"));
//
//        // Testing VALF() function
//        assertTrue(floatVariables.containsKey("stringToFloat%"));
//        assertEquals(5.0f, floatVariables.get("stringToFloat%"));


    }

    @Test
    public void testMathOperations() throws IOException {
        LinkedList<Token> tokens = lexer.lex("src/test/resources/math_operations.bas");
        ProgramNode actualProgram = new Parser(tokens).parse();

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(actualProgram);
        interpreter.visitStatements();

        HashMap<String, Integer> intVariables = interpreter.getIntVariables();
        HashMap<String, Float> floatVariables = interpreter.getFloatVariables();

//        assertEquals(5.0f, floatVariables.get("floatVar%"));
//        assertEquals(5, intVariables.get("intVar"));
//        assertEquals(5, intVariables.get("intVar"));
//        assertEquals(10.0f, floatVariables.get("floatVar2%"));
//        assertEquals(25.0f, floatVariables.get("floatVar3%"));
//        assertEquals(1.0f, floatVariables.get("floatVar4%"));
    }

    // TODO running into issues with
    //  valPlusInt = VAL("5") + 5
    //  valPlusVal = VAL("5") + VAL("5")
    //  valPlusVal = (VAL("5") + VAL("5"))
    @Test
    public void testFunctionMathOperations() throws IOException {
        LinkedList<Token> tokens = lexer.lex("src/test/resources/function_math_ops.bas");
        ProgramNode actualProgram = new Parser(tokens).parse();

        // Run Interpreter on it
        Interpreter interpreter = new Interpreter(actualProgram);
        interpreter.visitStatements();

        HashMap<String, Integer> intVariables = interpreter.getIntVariables();
        HashMap<String, Float> floatVariables = interpreter.getFloatVariables();

//        assertEquals(10, intVariables.get("valPlusInt"));
//        assertEquals(10, intVariables.get("valPlusVal"));
    }
}