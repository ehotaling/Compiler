public class WhileNode extends StatementNode {
    private final BooleanExpressionNode condition;
    private final String label;
    private final StatementsNode body;

    public WhileNode(BooleanExpressionNode condition, String label, StatementsNode body) {
        this.condition = condition;
        this.label = label;
        this.body = body;
    }

    public BooleanExpressionNode getCondition() {
        return condition;
    }

    public String getLabel() {
        return label;
    }

    public StatementsNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return String.format("WhileNode(%s, %s)", condition, label);
    }
}