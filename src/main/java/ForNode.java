import java.util.Objects;

public class ForNode extends StatementNode {
    private VariableNode variable;
    private Node initialValue;
    private Node limit;
    private Node increment;
    private StatementsNode body;

    public ForNode(VariableNode variable, Node initialValue, Node limit, Node increment, StatementsNode body) {
        this.variable = variable;
        this.initialValue = initialValue;
        this.limit = limit;
        this.increment = increment;
        this.body = body;
    }

    public VariableNode getVariable() {
        return variable;
    }

    public Node getInitialValue() {
        return initialValue;
    }

    public Node getLimit() {
        return limit;
    }

    public Node getIncrement() {
        return increment;
    }

    public StatementsNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return String.format("ForNode(variable=%s, initialValue=%s, limit=%s, increment=%s, body=%s)", variable, initialValue, limit, increment, body);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForNode forNode = (ForNode) o;
        return variable.equals(forNode.variable) &&
                initialValue.equals(forNode.initialValue) &&
                limit.equals(forNode.limit) &&
                increment.equals(forNode.increment) &&
                body.equals(forNode.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, initialValue, limit, increment, body);
    }
}