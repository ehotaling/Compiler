import java.util.List;
import java.util.Objects;

public class InputNode extends StatementNode {
    private List<Node> variables;

    public InputNode(List<Node> variables) {
        this.variables = variables;
    }

    public List<Node> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        return String.format("InputNode(%s)", variables);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputNode inputNode = (InputNode) o;
        return Objects.equals(variables, inputNode.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables);
    }

    @Override
    public void accept(Interpreter.StatementVisitor visitor) {
    }
}