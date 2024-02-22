import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProgramNode extends Node {

    private final List<Node> expressions;

    public ProgramNode() {
        this.expressions = new ArrayList<>();
    }

    public void addExpression(Node expression) {
        expressions.add(expression);
    }

    @Override
    public String toString() {
        return "ProgramNode: {" +
                expressions.stream()
                        .map(Node::toString)
                        .collect(Collectors.joining(", ")) +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramNode that = (ProgramNode) o;
        return Objects.equals(expressions, that.expressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions);
    }
}
