import java.util.HashMap;
import java.util.List;
import java.util.Queue;


public class Interpreter {
    private final ProgramNode programNode;
    // I don't want this in the constructor right??
    private Queue<Object> dataQueue;

    private HashMap<String, LabeledStatementNode> labels = new HashMap<>();

    public Interpreter(ProgramNode programNode) {
        this.programNode = programNode;
    }

    // TODO Is method this good?
    //  Walk the AST, searching for the DATA statements. Insert their contents into a Java collection that we can use for READ.
    public void searchDataStatements() {
        List<StatementNode> statements = programNode.getStatements();
        for (StatementNode statement: statements) {
            if (statement instanceof DataNode) {
                DataNode dataNode = (DataNode) statement;
                dataQueue.addAll(dataNode.getData());
            }
        }
    }


}
