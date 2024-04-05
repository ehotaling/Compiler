import java.util.Objects;

public class VariableNode extends StatementNode {

    private final String name;

    private String type;

    public VariableNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        // if the name ends with a $ then it is a string
        if (name.endsWith("$")) {
            type = "string";
        } else if (name.endsWith("%")) {
            type = "float";
        } else {
            type = "int";
        }
        return type;
    }

    @Override
    public String toString() {
        return String.format("VariableNode(%s)", name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableNode that = (VariableNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public void accept(StatementVisitor visitor) {

    }
}