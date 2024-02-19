public class IntegerNode extends Node {

    private float number;

    public IntegerNode(float number) {
        this.number = number;
    }

    public float getNumber() {
        return this.number;
    }

    @Override
    public String toString() {
        return "FloatNode " + this.number;
    }
}
