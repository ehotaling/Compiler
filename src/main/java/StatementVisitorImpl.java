import java.util.*;

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

    // TODO I have no idea if this is correct. I'm just guessing.
    public void visit(FunctionNode functionNode) {
        String functionName = functionNode.getFunctionName();
        List<Node> parameters = functionNode.getParameters();

        switch (functionName) {
            case "RANDOM":
                intVariables.put(functionName, BuiltInFunctions.RANDOM());
                break;
            case "LEFT$":
                StringNode leftParam1 = (StringNode) parameters.get(0);
                IntegerNode leftParam2 = (IntegerNode) parameters.get(1);
                stringVariables.put(functionName, BuiltInFunctions.LEFT$(leftParam1.getValue(), leftParam2.getInt()));
                break;
            case "RIGHT$":
                StringNode rightParam1 = (StringNode) parameters.get(0);
                IntegerNode rightParam2 = (IntegerNode) parameters.get(1);
                stringVariables.put(functionName, BuiltInFunctions.RIGHT$(rightParam1.getValue(), rightParam2.getInt()));
                break;
            case "MID$":
                StringNode midParam1 = (StringNode) parameters.get(0);
                IntegerNode midParam2 = (IntegerNode) parameters.get(1);
                IntegerNode midParam3 = (IntegerNode) parameters.get(2);
                stringVariables.put(functionName, BuiltInFunctions.MID$(midParam1.getValue(), midParam2.getInt(), midParam3.getInt()));
                break;
            case "NUM$":
                IntegerNode numParam = (IntegerNode) parameters.get(0);
                stringVariables.put(functionName, BuiltInFunctions.NUM$(numParam.getInt()));
                break;
            case "VAL":
                StringNode valParam = (StringNode) parameters.get(0);
                intVariables.put(functionName, BuiltInFunctions.VAL(valParam.getValue()));
                break;
            case "VAL$":
                StringNode valDollarParam = (StringNode) parameters.get(0);
                floatVariables.put(functionName, BuiltInFunctions.VAL$(valDollarParam.getValue()));
                break;
            default:
                throw new IllegalArgumentException("Unknown function: " + functionName);
        }
    }
}
