public class ExpressionNode extends Node {

    private final Node node;



    public ExpressionNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return String.format("ExpressionNode: {%s}", node);
    }
}