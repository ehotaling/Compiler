import java.util.Objects;

public class WhileNode extends StatementNode {
    private final BooleanExpressionNode condition;
    private final String label;
    private StatementsNode body;

    public WhileNode(BooleanExpressionNode condition, StatementsNode body, String label) {
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
        return String.format("WhileNode(%s, %s, %s)", condition, body, label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhileNode that = (WhileNode) o;
        return this.condition.equals(that.condition) && this.label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, label);
    }

    @Override
    public void accept(Interpreter.StatementVisitor visitor) {
    }
}