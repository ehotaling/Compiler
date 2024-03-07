import java.util.List;

public class DataNode extends StatementNode {
    private List<Node> data;

    public DataNode(List<Node> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("DataNode(%s)", data);
    }

    public List<Node> getData() {
        return data;
    }
}