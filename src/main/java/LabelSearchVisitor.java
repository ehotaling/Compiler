import java.util.HashMap;

public class LabelSearchVisitor implements Interpreter.StatementVisitor {
    private final HashMap<String, LabeledStatementNode> labels = new HashMap<>();

    @Override
    public void visit(LabeledStatementNode labeledStatementNode) {
        labels.put(labeledStatementNode.getLabel(), labeledStatementNode);
    }

    @Override
    public void visit(AssignmentNode assignmentNode) {
        // do nothing
    }



    public HashMap<String, LabeledStatementNode> getLabels() {
        return labels;
    }
}