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
        if (type.equals("int")) {
            intVariables.put(name, null);
        } else if (type.equals("float")) {
            floatVariables.put(name, null);
        } else if (type.equals("string")) {
            stringVariables.put(name, null);
        }
    }



        public void visit (DataNode dataNode){
            this.dataNodes.add(dataNode);
        }
}
