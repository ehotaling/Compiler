
public abstract class StatementNode extends Node {
    public abstract void accept(Interpreter.StatementVisitor visitor);

}
