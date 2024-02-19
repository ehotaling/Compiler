public class FloatNode extends Node {

    private float number;

    public FloatNode(float number) {
        this.number = number;
    }

    public float getNumber() {
        return this.number;
    }
    @Override
    public String toString() {
        return "FloaatNode: " + this.number;
    }


}
