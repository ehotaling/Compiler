public class ProgramNode extends Node {

    private final Node root;

    public ProgramNode(Node root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return String.format("ProgramNode: {%s}", root);
    }
}
