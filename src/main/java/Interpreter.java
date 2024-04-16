import java.util.*;


public class Interpreter {
    private final ProgramNode programNode;
    private final StatementVisitorImpl statementVisitor;
    private HashMap<String, LabeledStatementNode> labels;
    private Queue<Node> dataQueue;

    private HashMap<String, Integer> intVariables = new HashMap<>();
    private HashMap<String, String> stringVariables = new HashMap<>();
    private HashMap<String, Float> floatVariables = new HashMap<>();
    
    private final Scanner scanner = new Scanner(System.in);

    private boolean testMode = false;
    private List<String> testInput = new ArrayList<>();
    private List<String> output = new ArrayList<>();

    public Interpreter(ProgramNode programNode) {
        this.programNode = programNode;
        this.statementVisitor = new StatementVisitorImpl();
    }

    // Sets the test mode
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }
    // Sets the input
    public void setInput(List<String> input) {
        this.testInput = new ArrayList<>(input);
    }
    // Gets the output
    public List<String> getOutput() {
        return output;
    }

    // Visits the statements in the program
    private void visitStatements() {
        List<StatementNode> statements = programNode.getStatements();
        StatementNode prev = null;
        for (StatementNode curr : statements) {
            curr.accept(statementVisitor);

            // link the current statement to the previous one
            if (prev != null) {
                prev.setNext(curr);
            }

            prev = curr;
        }

        intVariables = statementVisitor.getIntVariables();
        floatVariables = statementVisitor.getFloatVariables();
        stringVariables = statementVisitor.getStringVariables();
        dataQueue = statementVisitor.getDataNodes();
        labels = statementVisitor.getLabels();
    }
    // Interprets the program
    public void interpret() {
        visitStatements();
        for (StatementNode statement : programNode.getStatements()) {
            if (statement instanceof AssignmentNode) {
                assignment((AssignmentNode) statement);
            } else if (statement instanceof ReadNode) {
                // read data from the data queue and assign it to variables
                read((ReadNode) statement);
            } else if (statement instanceof InputNode) {
                // read data from the data queue and assign it to variables
                input((InputNode) statement);
            } else if (statement instanceof PrintNode) {
                // read data from the data queue and assign it to variables
                print((PrintNode) statement);
            }
        }
    }

    // Evaluates assignment statements and assigns the value to the variable
    private void assignment(AssignmentNode assignmentNode) {
        String name = assignmentNode.getVariableNode().getName();
        Object value = evaluate(assignmentNode.getValue());
        InterpreterDataType type = assignmentNode.getVariableNode().getType();

        if (type == InterpreterDataType.INTEGER && value instanceof Integer) {
            intVariables.put(name, (Integer) value);
        } else if (type == InterpreterDataType.FLOAT && value instanceof Float) {
            floatVariables.put(name, (Float) value);
        } else if (type == InterpreterDataType.STRING && value instanceof String) {
            stringVariables.put(name, (String) value);
        } else {
            throw new IllegalArgumentException(String.format("Invalid type %s for variable %s with value: %s", type, name, value));
        }
    }

    // Prints the prompt. Reads data and sets the variable(s)
    // If in test mode, reads from the test input list
    public void input(InputNode inputNode) {
        if (testMode) {
            for (VariableNode variableNode : inputNode.getVariables()) {
                String name = variableNode.getName();
                InterpreterDataType type = variableNode.getType();
                System.out.println(testInput.get(0));
                String inputValue = testInput.remove(0); // Get and remove the first element from the test input list
                switch (type) {
                    case INTEGER:
                        intVariables.put(name, Integer.parseInt(inputValue));
                        break;
                    case FLOAT:
                        floatVariables.put(name, Float.parseFloat(inputValue));
                        break;
                    case STRING:
                        stringVariables.put(name, inputValue);
                        break;
                }
            }
        } else {
            System.out.println(inputNode.getPrompt());
            for (VariableNode variableNode : inputNode.getVariables()) {
                String name = variableNode.getName();
                InterpreterDataType type = variableNode.getType();
                switch (type) {
                    case INTEGER:
                        intVariables.put(name, Integer.parseInt(scanner.nextLine()));
                        break;
                    case FLOAT:
                        floatVariables.put(name, Float.parseFloat(scanner.nextLine()));
                        break;
                    case STRING:
                        stringVariables.put(name, scanner.nextLine());
                        break;
                }
            }
        }
    }

    // Reads and removes from internal collection. Updates variable(s)
    private void read(ReadNode readNode) {
        for (VariableNode variableNode: readNode.getVariables()) {
            if (dataQueue.isEmpty()) {
                throw new IllegalStateException("Cannot read from empty DATA queue");
            }

            String name = variableNode.getName();
            InterpreterDataType type = variableNode.getType();
            Node value = dataQueue.poll();

            if (type == InterpreterDataType.STRING  && value instanceof StringNode) {
                stringVariables.put(name, ((StringNode) value).getValue());
            } else if (type == InterpreterDataType.FLOAT && value instanceof FloatNode) {
                floatVariables.put(name, ((FloatNode) value).getFloat());
            } else if (type == InterpreterDataType.INTEGER && value instanceof IntegerNode) {
                intVariables.put(name, ((IntegerNode) value).getInt());
            } else {
                throw new IllegalArgumentException(String.format("Cannot assign value: %s to variable %s of type: %s", value, name, type));
            }
        }
    }

    // Prints each data item in the PrintNode
    public void print(PrintNode printNode) {
        for (Node arg: printNode.getArguments()) {
            if (testMode) {
                output.add(evaluate(arg).toString());
            } else {
                System.out.println(evaluate(arg));
            }
        }
    }

    // Evaluates the node and returns the value
    private Object evaluate(Node node) {

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
        // Evaluates the variable and returns the value
        if (node instanceof VariableNode) {
            VariableNode variableNode = (VariableNode) node;
            String name = variableNode.getName();
            InterpreterDataType type = variableNode.getType();
            if (type == InterpreterDataType.INTEGER && intVariables.containsKey(name)) {
                return intVariables.get(name);
            } else if (type == InterpreterDataType.FLOAT && floatVariables.containsKey(name)) {
                return floatVariables.get(name);
            } else if (type == InterpreterDataType.STRING && stringVariables.containsKey(name)) {
                return stringVariables.get(name);
            }
            Object value = evaluate(variableNode.getVal());
            if (type == InterpreterDataType.INTEGER && value instanceof Integer) {
                return intVariables.get(name);
            } else if (type == InterpreterDataType.FLOAT && value instanceof Float) {
                return floatVariables.get(name);
            } else if (type == InterpreterDataType.STRING && value instanceof String) {
                return stringVariables.get(name);
            } else {
                throw new IllegalArgumentException(String.format("Invalid value %s for variable: %s", value, variableNode));
            }
        }
        // Evaluates the function and returns the value
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
            if (functionName.equals("VAL%")) {
                String str = (String) evaluate(parameters.get(0));
                return BuiltInFunctions.VALF(str);
            }
        }
        // Evaluates the math operation and returns the value
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
        // Evaluates the expression and returns the value
        if (node instanceof ExpressionNode) {
            ExpressionNode expressionNode = (ExpressionNode) node;
            return evaluate(expressionNode.getNode());
        }
        // Evaluates the term and returns the value
        if (node instanceof TermNode) {
            TermNode termNode = (TermNode) node;
            return evaluate(termNode.getNode());
        }
        // Evaluates the factor and returns the value
        if (node instanceof FactorNode) {
            FactorNode factorNode = (FactorNode) node;
            return evaluate(factorNode.getInnerNode());
        }
        throw new RuntimeException("Unknown node type: " + node.getClass());
    }

    // Checks if both arguments are integers
    private boolean isInteger(Object left, Object right) {
        return left instanceof Integer && right instanceof Integer;
    }

    // Checks if both arguments are numeric
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

    public Queue<Node> getDataQueue() {
        return dataQueue;
    }

}
