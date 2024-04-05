import java.util.HashMap;
import java.util.List;
import java.util.Queue;


public class Interpreter {
    private final ProgramNode programNode;

    private final StatementVisitorImpl statementVisitor;
    private HashMap<String, LabeledStatementNode> labels;
    private Queue<DataNode> dataQueue;

    private HashMap<String, Integer> intVariables;
    private HashMap<String, String> stringVariables;
    private HashMap<String, Float> floatVariables;

    public Interpreter(ProgramNode programNode) {
        this.programNode = programNode;
        this.statementVisitor = new StatementVisitorImpl();
    }

    // TODO Is method this good?
    //  Walk the AST, searching for the DATA statements. Insert their contents into a Java collection that we can use for READ.
    public void visitStatements() {
        List<StatementNode> statements = programNode.getStatements();
        for (StatementNode statement: statements) {
            statement.accept(statementVisitor);
        }

        intVariables = statementVisitor.getIntVariables();
        floatVariables = statementVisitor.getFloatVariables();
        stringVariables = statementVisitor.getStringVariables();
        dataQueue = statementVisitor.getDataNodes();
        labels = statementVisitor.getLabels();
    }
}
