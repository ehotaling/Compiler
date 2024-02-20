public class ExpressionNode extends Node {

    private final TermNode termNode;



    public ExpressionNode(TermNode termNode) {
        this.termNode = termNode;
    }

    @Override
    public String toString() {
        return String.format("ExpressionNode: {%s}", termNode);
    }
}
