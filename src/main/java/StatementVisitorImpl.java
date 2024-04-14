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
        Object value = evaluate(assignmentNode.getValue());
        if (type.equals("int")) {
            intVariables.put(name, (Integer) value);
        } else if (type.equals("float")) {
            floatVariables.put(name, (Float) value);
        } else if (type.equals("string")) {
            stringVariables.put(name, (String) value);
        }
    }

    public Object evaluate(Node node) {

            if (node instanceof IntegerNode) {
                IntegerNode integerNode = (IntegerNode) node;
                return integerNode.getInt();
            }

            if (node instanceof FloatNode) {
                FloatNode floatNode = (FloatNode) node;
                return floatNode.getFloat();
            }

            if (node instanceof StringNode) {
                StringNode stringNode = (StringNode) node;
                return stringNode.getValue();
            }

            if (node instanceof VariableNode) {
                VariableNode variableNode = (VariableNode) node;
                String name = variableNode.getName();
                String type = variableNode.getType();
                if (type.equals("int")) {
                    return intVariables.get(name);
                } else if (type.equals("float")) {
                    return floatVariables.get(name);
                } else if (type.equals("string")) {
                    return stringVariables.get(name);
                }
            }

            if (node instanceof FunctionNode) {
                // evaluate parameters and call the right function based on name
                FunctionNode functionNode = (FunctionNode) node;
                String functionName = functionNode.getFunctionName();
                List<Node> parameters = functionNode.getParameters();
                if (functionName.equals("RANDOM")) {
                    return BuiltInFunctions.RANDOM();
                }
                if (functionName.equals("LEFT$")) {
                    String str = (String) evaluate(parameters.get(0));
                    int n = (Integer) evaluate(parameters.get(1));
                    return BuiltInFunctions.LEFT$(str, n);
                }
                if (functionName.equals("RIGHT$")) {
                    String str = (String) evaluate(parameters.get(0));
                    int n = (Integer) evaluate(parameters.get(1));
                    return BuiltInFunctions.RIGHT$(str, n);
                }
                if (functionName.equals("MID$")) {
                    String str = (String) evaluate(parameters.get(0));
                    int start = (Integer) evaluate(parameters.get(1));
                    int count = (Integer) evaluate(parameters.get(2));
                    return BuiltInFunctions.MID$(str, start, count);
                }
                if (functionName.equals("NUM$")) {
                    Number num = (Number) evaluate(parameters.get(0));
                    return BuiltInFunctions.NUM$(num);
                }
                if (functionName.equals("VAL")) {
                    String str = (String) evaluate(parameters.get(0));
                    return BuiltInFunctions.VAL(str);
                }
                if (functionName.equals("VALF")) {
                    String str = (String) evaluate(parameters.get(0));
                    return BuiltInFunctions.VALF(str);
                }
            }

            if (node instanceof MathOpNode) {
                MathOpNode mathOpNode = (MathOpNode) node;
                Object left = evaluate(mathOpNode.getLeft());
                Object right = evaluate(mathOpNode.getRight());

                if (mathOpNode.getOperator() == MathOpNode.OPERATION.ADD) {
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left + (Integer) right;
                    } else if (left instanceof String && right instanceof String) {
                        return (String) left + (String) right;
                    } else {
                        return ((Number) left).floatValue() + ((Number) right).floatValue();
                    }
                } else if (mathOpNode.getOperator() == MathOpNode.OPERATION.SUBTRACT) {
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left - (Integer) right;
                    } else {
                        return ((Number) left).floatValue() - ((Number) right).floatValue();
                    }
                } else if (mathOpNode.getOperator() == MathOpNode.OPERATION.MULTIPLY) {
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left * (Integer) right;
                    } else {
                        return ((Number) left).floatValue() * ((Number) right).floatValue();
                    }
                } else if (mathOpNode.getOperator() == MathOpNode.OPERATION.DIVIDE) {
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left / (Integer) right;
                    } else {
                        return ((Number) left).floatValue() / ((Number) right).floatValue();
                    }
                }
            }

            if (node instanceof ExpressionNode) {
                ExpressionNode expressionNode = (ExpressionNode) node;
                return evaluate(expressionNode.getNode());
            }

            if (node instanceof TermNode) {
                TermNode termNode = (TermNode) node;
                return evaluate(termNode.getNode());
            }

            if (node instanceof FactorNode) {
                FactorNode factorNode = (FactorNode) node;
                return evaluate(factorNode.getInnerNode());
            }

            if (node instanceof AssignmentNode) {
                AssignmentNode assignmentNode = (AssignmentNode) node;
                return evaluate(assignmentNode.getValue());
            }
            throw new RuntimeException("Unknown node type: " + node.getClass());
        }

        public void visit (DataNode dataNode){
            this.dataNodes.add(dataNode);
        }
}
