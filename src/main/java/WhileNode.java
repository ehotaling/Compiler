public class WhileNode extends StatementNode {
    private BooleanExpressionNode condition;
    private String label;
    private StatementsNode body;

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