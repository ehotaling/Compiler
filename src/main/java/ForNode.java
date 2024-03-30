public class ForNode extends StatementNode {
    private VariableNode variable;
    private ExpressionNode initialValue;
    private ExpressionNode limit;
    private Node increment;
    private StatementsNode body;

    public ForNode(VariableNode variable, ExpressionNode initialValue, ExpressionNode limit, Node increment, StatementsNode body) {
        this.variable = variable;
        this.initialValue = initialValue;
        this.limit = limit;
        this.increment = increment;
        this.body = body;
    }

    public VariableNode getVariable() {
        return variable;
    }

    public ExpressionNode getInitialValue() {
        return initialValue;
    }

    public ExpressionNode getLimit() {
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
}