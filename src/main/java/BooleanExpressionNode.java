public class BooleanExpressionNode {

    public enum OPERATOR {
        GREATERTHAN,
        GREATERTHANEQUALTO,
        LESSTHAN,
        LESSTHANEQUALTO,
        NOTEQUALS,
        EQUALS
    }

    private ExpressionNode left;
    private OPERATOR operator;
    private ExpressionNode right;

    public BooleanExpressionNode(ExpressionNode left, OPERATOR operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public OPERATOR getOperator() {
        return operator;
    }

    public ExpressionNode getRight() {
        return right;
    }

    @Override
    public String toString() {
        return String.format("BooleanExpressionNode(%s %s %s)", left, operator, right);
    }
}