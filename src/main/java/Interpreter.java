import java.util.HashMap;
import java.util.List;
import java.util.Queue;


public class Interpreter {
    private final ProgramNode programNode;
    private final StatementVisitorImpl statementVisitor;
    private HashMap<String, LabeledStatementNode> labels;
    private Queue<DataNode> dataQueue;

    private HashMap<String, Integer> intVariables;
    private HashMap<String, String> stringVariables;
    private HashMap<String, Float> floatVariables;

    public Interpreter(ProgramNode programNode) {
        this.programNode = programNode;
        this.statementVisitor = new StatementVisitorImpl();
    }

    public void visitStatements() {
        List<StatementNode> statements = programNode.getStatements();
        for (StatementNode statement: statements) {
            statement.accept(statementVisitor);
        }

        intVariables = statementVisitor.getIntVariables();
        floatVariables = statementVisitor.getFloatVariables();
        stringVariables = statementVisitor.getStringVariables();
        dataQueue = statementVisitor.getDataNodes();
        labels = statementVisitor.getLabels();
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
            Object value = evaluate(variableNode.getVal());
            if (type.equals("int") && value instanceof IntegerNode) {
                return intVariables.get(name);
            } else if (type.equals("float") && value instanceof FloatNode) {
                return floatVariables.get(name);
            } else if (type.equals("string") && value instanceof StringNode) {
                return stringVariables.get(name);
            } else {
                throw new IllegalArgumentException(String.format("Invalid value %s for variable: %s", value, variableNode));
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

            if (!isNumeric(left, right)) {
                throw new IllegalArgumentException(String.format("Illegal math operation for arguments: \n%s\n%s\n", left, right));
            }

            if (mathOpNode.getOperator() == MathOpNode.OPERATION.ADD) {
                if (isInteger(left, right)) {
                    return (Integer) left + (Integer) right;
                } else if (isNumeric(left, right)) {
                    return ((Number) left).floatValue() + ((Number) right).floatValue();
                }
            } else if (mathOpNode.getOperator() == MathOpNode.OPERATION.SUBTRACT) {
                if (isInteger(left, right)) {
                    return (Integer) left - (Integer) right;
                } else if (isNumeric(left, right)) {
                    return ((Number) left).floatValue() - ((Number) right).floatValue();
                }
            } else if (mathOpNode.getOperator() == MathOpNode.OPERATION.MULTIPLY) {
                if (isInteger(left, right)) {
                    return (Integer) left * (Integer) right;
                } else if (isNumeric(left, right)) {
                    return ((Number) left).floatValue() * ((Number) right).floatValue();
                }
            } else if (mathOpNode.getOperator() == MathOpNode.OPERATION.DIVIDE) {
                if (isInteger(left, right)) {
                    return (Integer) left / (Integer) right;
                } else if (isNumeric(left, right)) {
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

    private boolean isInteger(Object left, Object right) {
        return left instanceof Integer && right instanceof Integer;
    }

    private boolean isNumeric(Object left, Object right) {
        return (left instanceof Float || left instanceof Integer) &&
                (right instanceof Float || right instanceof Integer);
    }

    public HashMap<String, Integer> getIntVariables() {
        return intVariables;
    }

    public HashMap<String, String> getStringVariables() {
        return stringVariables;
    }

    public HashMap<String, Float> getFloatVariables() {
        return floatVariables;
    }

    public HashMap<String, LabeledStatementNode> getLabels() {
        return labels;
    }

    public Queue<DataNode> getDataQueue() {
        return dataQueue;
    }

}
