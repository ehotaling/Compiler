import java.util.LinkedList;

public class ExpressionNode extends Node {

//    private final TermNode termNode;
    private LinkedList<Node> children = new LinkedList<>();

    public ExpressionNode() {
    }

    public ExpressionNode(Node child) {
        this.children.add(child);
    }

    public ExpressionNode(LinkedList<Node> nodes) {
        this.children = nodes;
    }

    public void addTerms(LinkedList<Node> nodes) {
        this.children = nodes;
    }

    public void addTerm(Node node) {
        this.children.add(node);
    }

    @Override
    public String toString() {
        return String.format("ExpressionNode(%s)", children.size() > 1 ? children.toString(): children.get(0));
    }
}
