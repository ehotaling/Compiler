public class LabeledStatementNode extends StatementNode {
    private String label;
    private StatementNode statementNode;

    public LabeledStatementNode(String label, StatementNode statementNode) {
        this.label = label;
        this.statementNode = statementNode;
    }

    // Getter method for label
    public String getLabel() {
        return this.label;
    }

    // Getter method for statementNode
    public StatementNode getStatementNode() {
        return this.statementNode;
    }

    @Override
    public String toString() {
        return String.format("LabeledStatementNode(%s, %s)", label, statementNode);
    }
}