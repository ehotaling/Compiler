public class LabeledStatementNode extends StatementNode {
    private String label;
    private StatementNode statementNode;

    public LabeledStatementNode(String label, StatementNode statementNode) {
        this.label = label;
        this.statementNode = statementNode;
    }

    public String getLabel() {
        return label;
    }

    public StatementNode getStatementNode() {
        return statementNode;
    }

    @Override
    public String toString() {
        return String.format("LabeledStatementNode(%s, %s)", label, statementNode);
    }
}