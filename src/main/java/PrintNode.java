import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrintNode extends StatementNode {

    private List<Node> nodes = new ArrayList<>();

    public PrintNode() {}

    public PrintNode(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public List<Node> getNodes(Node node) {
        return nodes;
    }

    @Override
    public String toString() {
        return String.format("PrintNode(%s)", nodes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintNode printNode = (PrintNode) o;
        return Objects.equals(nodes, printNode.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes);
    }
}
