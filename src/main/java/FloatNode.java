public class FloatNode extends Node {

    private final float value;

    public FloatNode(float value) {
        this.value = value;
    }

    public float getFloat() {
        return this.value;
    }
    @Override
    public String toString() {
        return String.format("FloatNode: <%f>", value);
    }
}
