import java.util.LinkedList;


public class TermNode extends Node {
    private LinkedList<Node> factorNodes = new LinkedList<>();

    public TermNode() {}

    public TermNode(Node factorNode) {
        this.factorNodes.add(factorNode);
    }

    public TermNode(LinkedList<Node> factorNodes) { //Changed FactorNode to Node
        this.factorNodes = factorNodes;
    }

    public void addFactor(Node factorNode) { //Changed FactorNode to Node
        this.factorNodes.add(factorNode);
    }

    @Override
    public String toString() {
        return String.format("TermNode: {%s} ", this.factorNodes);
    }
}