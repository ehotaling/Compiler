public interface StatementVisitor {
    void visit(LabeledStatementNode labeledStatementNode);
    void visit(AssignmentNode assignmentNode);

    void visit(DataNode dataNode);
}
