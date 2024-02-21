import java.util.LinkedList;

public class TermNode extends Node {

    private LinkedList<Node> children = new LinkedList<>();

    public TermNode() {
    }

    public TermNode(Node factorNode) {
        this.children.add(factorNode);
    }

    public TermNode(LinkedList<Node> factorNodes) {
        this.children = factorNodes;
    }

    public void addFactor(Node factorNode) {
        this.children.add(factorNode);
    }

    public String toString() {
        return String.format("TermNode(%s)", children.size() > 1 ? children.toString(): children.get(0));
    }
}
