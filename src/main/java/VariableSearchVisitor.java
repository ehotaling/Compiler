import java.util.HashMap;
public class VariableSearchVisitor implements Interpreter.StatementVisitor {

    private final HashMap<String, Integer> intVariables = new HashMap<>();
    private final HashMap<String, Float> floatVariables = new HashMap<>();
    private final HashMap<String, String> stringVariables = new HashMap<>();

    @Override
    public void visit(AssignmentNode assignmentNode) {
        VariableNode variableNode = assignmentNode.getVariableNode();
        String name = variableNode.getName();
        String type = variableNode.getType();

        if (type.equals("int")) {
            Integer value = assignmentNode.getValue();
            intVariables.put(name,value);
        } else if (type.equals("float")) {
            Float value = assignmentNode.getValue();
            floatVariables.put(name, value);
        } else if (type.equals("string")) {
            String value = assignmentNode.getValue();
            stringVariables.put(name, value);
        }


    }
    @Override
    public void visit(LabeledStatementNode labeledStatementNode) {
        // do nothing
    }

    public HashMap<String, Integer> getIntVariables() {
        return intVariables;
    }

    public HashMap<String, Float> getFloatVariables() {
        return floatVariables;
    }

    public HashMap<String, String> getStringVariables() {
        return stringVariables;
    }
}
