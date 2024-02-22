public class FactorNode extends Node {

    private final Node node;

    public FactorNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return String.format("FactorNode: {%s}", node);
    }
}