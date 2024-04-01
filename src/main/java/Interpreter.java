import java.util.HashMap;
import java.util.List;
import java.util.Queue;


public class Interpreter {
    private final ProgramNode programNode;
    private HashMap<String, LabeledStatementNode> labels;
    private Queue<Object> dataQueue;

    private HashMap<String, IntegerNode> intVariables;
    private HashMap<String, StringNode> stringVariables;
    private HashMap<String, FloatNode> floatVariables;

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

    // Use a visitor pattern to walk the statementsnode searching for labeledstatements.
    // TODO I made a visitor interface in the Interpreter class(below), but I'm not sure if that's the right place for it.
    // TODO I made a seperate LabelSearchVisitor class, do I need a seperate class for each visitor? i.e visiting variables, etc.
    // TODO In each class that extends StatementNode, I added an accept method that takes a StatementVisitor as an argument.
    //  The accept methods are all blank except labeledstatementNode I'm not sure if that's the right way to do it, but it seems to work.
    public interface StatementVisitor {
        void visit(LabeledStatementNode labeledStatementNode);
    }

    public void searchForLabels() {
        LabelSearchVisitor visitor = new LabelSearchVisitor();
        List<StatementNode> statements = programNode.getStatements();
        for (StatementNode statement: statements) {
            statement.accept(visitor);
        }
        labels = visitor.getLabels();

    }



}
