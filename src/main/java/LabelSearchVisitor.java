import java.util.HashMap;

public class LabelSearchVisitor implements Interpreter.StatementVisitor {
    private final HashMap<String, LabeledStatementNode> labels = new HashMap<>();

    @Override
    public void visit(LabeledStatementNode labeledStatementNode) {
        labels.put(labeledStatementNode.getLabel(), labeledStatementNode);
    }


    public HashMap<String, LabeledStatementNode> getLabels() {
        return labels;
    }
}