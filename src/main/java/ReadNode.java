import java.util.List;

public class ReadNode extends StatementNode {
    private List<VariableNode> variables;

    public ReadNode(List<VariableNode> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return String.format("ReadNode(%s)", variables);
    }

    public List<VariableNode> getVariables() {
        return variables;
    }
}