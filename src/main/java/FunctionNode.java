import java.util.ArrayList;
import java.util.List;

public class FunctionNode extends Node {
    private String functionName;
    private List<Node> parameters;

    public FunctionNode(String functionName) {
        this.functionName = functionName;
        this.parameters = new ArrayList<>();
    }

    public void setParameters(List<Node> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return String.format("FunctionNode(%s, %s)", functionName, parameters);
    }
}