public abstract class Node {

    public Node(){}

    @Override
    public abstract String toString();

    public abstract void accept(StatementVisitor visitor);
}
