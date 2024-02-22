public class MathOpNode extends Node {


    public enum OPERATION {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    private final Node left;

    private final Node right;

    private final OPERATION operation;

    public MathOpNode(OPERATION operator, Node left, Node right) {
        this.operation = operator;
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

//    public int eval() {
//        return left.eval() + right.eval();
//    }
//
//    public float eval() {
//        return left.eval() + right.eval();
//    }

    @Override
    public String toString() {
        return String.format("MathOpNode(%s %s %s)", left, operation, right);
    }
}
