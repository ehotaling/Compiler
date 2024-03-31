public class WhileNode extends StatementNode {
    private final BooleanExpressionNode condition;
    private final String label;

    public WhileNode(BooleanExpressionNode condition, String label) {
        this.condition = condition;
        this.label = label;
    }

    public BooleanExpressionNode getCondition() {
        return condition;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("WhileNode(%s, %s)", condition, label);
    }
}