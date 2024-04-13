import java.util.*;

public class StatementVisitorImpl implements StatementVisitor {

    private final Queue<DataNode> dataNodes = new LinkedList<>();

    private final HashMap<String, Integer> intVariables = new HashMap<>();
    private final HashMap<String, Float> floatVariables = new HashMap<>();
    private final HashMap<String, String> stringVariables = new HashMap<>();

    private final HashMap<String, LabeledStatementNode> labels = new HashMap<>();

    public HashMap<String, Integer> getIntVariables() {
        return intVariables;
    }

    public HashMap<String, Float> getFloatVariables() {
        return floatVariables;
    }

    public HashMap<String, String> getStringVariables() {
        return stringVariables;
    }

    public Queue<DataNode> getDataNodes() {
        return dataNodes;
    }

    public HashMap<String, LabeledStatementNode> getLabels() {
        return labels;
    }

    public void visit(LabeledStatementNode labeledStatementNode) {
        labels.put(labeledStatementNode.getLabel(), labeledStatementNode);
    }

    public void visit(AssignmentNode assignmentNode) {
        VariableNode variableNode = assignmentNode.getVariableNode();
        String name = variableNode.getName();
        String type = variableNode.getType();
        if (type.equals("int")) {
            int i = evaluateInt(assignmentNode.getValue());
            intVariables.put(name, i);
        } else if (type.equals("float")) {
            //float f = evaluateFloat(assignmentNode.getValue());
            floatVariables.put(name, null);
        } else if (type.equals("string")) {
            String s = evaluateString(assignmentNode.getValue());
            stringVariables.put(name, s);
        }
    }

    public int evaluateInt (Node node) {
        // handles cases like age = 28
        if (node instanceof ExpressionNode) {
            ExpressionNode expressionNode = (ExpressionNode) node;
            if (expressionNode.getNode() instanceof TermNode) {
                TermNode termNode = (TermNode) expressionNode.getNode();
                FactorNode factorNode = (FactorNode) termNode.getNode();
                Node node1 = factorNode.getInnerNode();
                if (node1 instanceof IntegerNode) {
                    IntegerNode integerNode = (IntegerNode) node1;
                    return integerNode.getInt();
                }
            // handles cases like age = 20 + 8
            } else if (expressionNode.getNode() instanceof MathOpNode) {
                MathOpNode mathOpNode = (MathOpNode) expressionNode.getNode();
                return handleMathOpNode(mathOpNode);
            }

        }
        return 0;
    }

    public int handleMathOpNode (MathOpNode mathOpNode) {
        int leftInt = 0;
        int rightInt = 0;
        MathOpNode.OPERATION operation = mathOpNode.getOperator();
        TermNode leftTermNode = (TermNode) mathOpNode.getLeft();
        TermNode rightTermNode = (TermNode) mathOpNode.getRight();
        FactorNode leftFactorNode = (FactorNode) leftTermNode.getNode();
        FactorNode rightFactorNode = (FactorNode) rightTermNode.getNode();

        if (leftFactorNode.getInnerNode() instanceof ExpressionNode) {
            ExpressionNode expressionNode = (ExpressionNode) leftFactorNode.getInnerNode();
            MathOpNode leftMathOpNode = (MathOpNode) expressionNode.getNode();
            leftInt = handleMathOpNode(leftMathOpNode);
        }
        if (rightFactorNode.getInnerNode() instanceof ExpressionNode) {
            ExpressionNode expressionNode = (ExpressionNode) rightFactorNode.getInnerNode();
            MathOpNode rightMathOpNode = (MathOpNode) expressionNode.getNode();
            rightInt = handleMathOpNode(rightMathOpNode);
        }
        IntegerNode leftNode = (IntegerNode) leftFactorNode.getInnerNode();
        IntegerNode rightNode = (IntegerNode) rightFactorNode.getInnerNode();
        leftInt = leftNode.getInt();
        rightInt = rightNode.getInt();
        if (operation == MathOpNode.OPERATION.ADD) {
            return leftInt + rightInt;
        } else if (operation == MathOpNode.OPERATION.SUBTRACT) {
            return leftInt - rightInt;
        } else if (operation == MathOpNode.OPERATION.MULTIPLY) {
            return leftInt * rightInt;
        } else if (operation == MathOpNode.OPERATION.DIVIDE) {
            return leftInt / rightInt;
        }
        return 0;
    }

//    public float evaluateFloat (Node node) {
//
//    }

    public String evaluateString (Node node) throws IllegalArgumentException {
        if (node instanceof ExpressionNode) {
            ExpressionNode expressionNode = (ExpressionNode) node;
            TermNode termNode = (TermNode) expressionNode.getNode();
            StringNode stringNode = (StringNode) termNode.getNode();
            return stringNode.getValue();
        }
         throw new IllegalArgumentException("Invalid string value");
    }

    public void visit(DataNode dataNode) {
        this.dataNodes.add(dataNode);
    }
}
