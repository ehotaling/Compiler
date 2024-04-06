import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class StatementVisitorImpl implements StatementVisitor {

    private final Queue<DataNode> dataNodes = new LinkedList<>();

    private final HashMap<String, Integer> intVariables = new HashMap<>();
    private final HashMap<String, Float> floatVariables = new HashMap<>();
    private final HashMap<String, String> stringVariables = new HashMap<>();

    private final HashMap<String, LabeledStatementNode> labels = new HashMap<>();

    public HashMap<String, Integer> getIntVariables() {
        return intVariables;
    }

    public HashMap<String, Float> getFloatVariables() {
        return floatVariables;
    }

    public HashMap<String, String> getStringVariables() {
        return stringVariables;
    }

    public Queue<DataNode> getDataNodes() {
        return dataNodes;
    }

    public HashMap<String, LabeledStatementNode> getLabels() {
        return labels;
    }

    public void visit(LabeledStatementNode labeledStatementNode) {
        labels.put(labeledStatementNode.getLabel(), labeledStatementNode);
    }

    public void visit(AssignmentNode assignmentNode) {
        VariableNode variableNode = assignmentNode.getVariableNode();
        String name = variableNode.getName();
        String type = variableNode.getType();
        Node valueNode = assignmentNode.getValue();
        if (type.equals("int")) {
            IntegerNode integerNode = (IntegerNode) valueNode;
            Integer value = integerNode.getInt();
            intVariables.put(name, value);
        } else if (type.equals("float")) {
            FloatNode floatNode = (FloatNode) valueNode;
            Float value = floatNode.getFloat();
            floatVariables.put(name, value);
        } else if (type.equals("string")) {
            StringNode stringNode = (StringNode) valueNode;
            String value = stringNode.getValue();
            stringVariables.put(name, value);
        }
    }
    public void visit(DataNode dataNode) {
        this.dataNodes.add(dataNode);
    }
}
