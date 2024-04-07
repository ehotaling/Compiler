import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    @Test
    public void testDataAndLabelProcessing() {
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
    public void testVariableStorage() {
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

        assertEquals(1, intVariables.size());
        assertEquals(1, floatVariables.size());
        assertEquals(1, stringVariables.size());
        assertEquals(5, intVariables.get("x"));
        assertEquals(5.0f, floatVariables.get("y%"));
        assertEquals("Hello World", stringVariables.get("z$"));

    }
}