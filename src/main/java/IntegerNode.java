public class IntegerNode extends Node {

    private final int value;

    public IntegerNode(int value) {
        this.value = value;
    }

    public int getInt() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.format("IntegerNode(%d)", value);
    }
}
