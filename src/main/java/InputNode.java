import java.util.List;

public class InputNode extends StatementNode {
    private Node firstParameter;
    private List<VariableNode> variables;

    public InputNode(Node firstParameter, List<VariableNode> variables) {
        this.firstParameter = firstParameter;
        this.variables = variables;
    }

    public Node getFirstParameter() {
        return firstParameter;
    }

    public List<VariableNode> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        return String.format("InputNode(%s, %s)", firstParameter, variables);
    }
}