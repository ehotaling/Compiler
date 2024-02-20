import java.util.LinkedList;
import java.util.List;

public class TermNode extends Node {

    private LinkedList<FactorNode> factorNodes = new LinkedList<>();

    public TermNode() {
    }

    public TermNode(FactorNode factorNode) {
        this.factorNodes.add(factorNode);
    }

    public TermNode(LinkedList<FactorNode> factorNodes) {
        this.factorNodes = factorNodes;
    }

    public void addFactor(FactorNode factorNode) {
        this.factorNodes.add(factorNode);
    }

    public String toString() {
        return String.format("TermNode: {%s} ", this.factorNodes);
    }
}
