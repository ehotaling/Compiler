import java.util.HashMap;
public class VariableSearchVisitor implements Interpreter.StatementVisitor {

    // TODO are we storing values for variables? or the type of variables? If storing types are we storing the type as a string?
    private final HashMap<String, Integer> intVariables = new HashMap<>();
    private final HashMap<String, Float> floatVariables = new HashMap<>();
    private final HashMap<String, String> stringVariables = new HashMap<>();

    @Override
    public void visit(VariableNode variableNode) {
        // TODO again are we storing values here? or types? I put some placeholders for now.
        String name = variableNode.getName();
        String type = variableNode.getType();
        if (type.equals("int")) {
            intVariables.put(name, 0);
        } else if (type.equals("float")) {
            floatVariables.put(name, 0.0f);
        } else if (type.equals("string")) {
            stringVariables.put(name, "");
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
