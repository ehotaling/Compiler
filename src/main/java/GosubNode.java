public class GosubNode extends StatementNode {
    private String label;

    public GosubNode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("GosubNode(%s)", label);
    }
}