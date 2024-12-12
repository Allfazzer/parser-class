import java.util.*;

public class Evaluator {
    private LinkedHashSet<String> variables = new LinkedHashSet<>();
    private Map<String, Boolean> results = new HashMap<>();
    private List<String> subExpressions = new ArrayList<>();
    private boolean headerPrinted = false;
    private boolean printSubExpression = true;

    // Main method to generate the truth table
    public void generateTruthTable(Node root) {
        headerPrinted = false;
        collectVariables(root);
        collectSubExpressions(root, subExpressions);
        List<Map<String, Boolean>> truthCombinations = generateTruthCombinations(variables);

        for (Map<String, Boolean> variableValues : truthCombinations) {
            boolean result = evaluateExpression(root, variableValues, subExpressions);
            printSubExpressionResults(subExpressions, results);
        }
        printBorder();
    }

    private String collectSubExpressions(Node node, List<String> subExpressions) {
        String expString = "";

        if (node.getValue().equals("<Sentence>") || node.getValue().equals("<Complex Sentence>")) {

            if (node.getValue().equals("<Complex Sentence>") && node.getChildren().get(1).getValue().equals("<Sentence>") && node.getChildren().get(1).getChildren().get(0).getValue().equals("<Atomic Sentence>")) {
                expString += "NOT ";
            }

            for (Node child : node.getChildren()) {
                expString += collectSubExpressions(child, subExpressions);
            }

        } else if (node.getValue().equals("<Connective>")) {
            expString += " " + node.getChildren().get(0).getValue() + " ";
        } else if (node.getValue().equals("(")) {
            expString += "(";
        } else if (node.getValue().equals(")")) {
            expString += ")";
        } else if (node.getValue().equals("<Atomic Sentence>")) {
            expString += node.getChildren().get(0).getValue();
        }

        if (!expString.isEmpty() && !subExpressions.contains(expString)) {
            subExpressions.add(expString);
        }
        return expString;
    }

    private void printSubExpressionResults(List<String> subExpressions, Map<String, Boolean> results) {
        printHeader(subExpressions);
        printBorder();

        System.out.print("|");
        for (String subExpression : subExpressions) {
            if (results.containsKey(subExpression)) {
                boolean printSubExpression = true;

                // Check if the subexpression is enclosed in parentheses and
                // if the subexpression without parentheses also exists in the list
                if (subExpression.startsWith("(") && subExpression.endsWith(")")
                        && subExpressions.contains(subExpression.substring(1, subExpression.length() - 1))) {
                    printSubExpression = false;
                }

                if (printSubExpression) {
                    String value = results.get(subExpression) ? "T" : "F";
                    System.out.printf(" %-20s |", value);
                }
            }
        }
        System.out.println();
    }

    // Modified printHeader to include sub-expressions
    private void printHeader(List<String> subExpressions) {
        if (headerPrinted) return;

        // Print header for subexpression results
        System.out.print("|");
        for (String subExpression : subExpressions) {
            if (results.containsKey(subExpression)) {
                boolean printSubExpression = true;

                // Check if the subexpression is enclosed in parentheses and
                // if the subexpression without parentheses also exists in the list
                if (subExpression.startsWith("(") && subExpression.endsWith(")")
                        && subExpressions.contains(subExpression.substring(1, subExpression.length() - 1))) {
                    printSubExpression = false;
                }

                if (printSubExpression) {
                    System.out.printf(" %-20s |", subExpression);
                }
            }
        }
        System.out.println();
        headerPrinted = true;
    }

    private void printBorder() {
        StringBuilder border = new StringBuilder();
        border.append("+");

        for (String subExpression : subExpressions) {
            if (results.containsKey(subExpression)){
                boolean printSubExpression = true;

                // Prevent duplicate printing of subexpressions enclosed in parentheses
                if (subExpression.startsWith("(") && subExpression.endsWith(")")
                        && subExpressions.contains(subExpression.substring(1, subExpression.length() - 1))) {
                    printSubExpression = false;
                }

                if (printSubExpression){
                    border.append("-".repeat(22)).append("+"); // Add Result column border
                }
            }
        }
        System.out.println(border.toString());
    }

    private List<Map<String, Boolean>> generateTruthCombinations(LinkedHashSet<String> variables) {
        List<Map<String, Boolean>> truthCombinations = new ArrayList<>();
        List<String> varList = new ArrayList<>(variables);
        int numVars = varList.size();
        int numCombinations = 1 << numVars; // 2^n

        for (int i = 0; i < numCombinations; i++) {
            Map<String, Boolean> combination = new LinkedHashMap<>();
            for (int j = 0; j < numVars; j++) {
                combination.put(varList.get(j), (i & (1 << (numVars - j - 1))) != 0);
            }
            truthCombinations.add(combination);
        }
        return truthCombinations;
    }

    // Method to collect all variables in the expression tree
    private void collectVariables(Node node) {
        if (node == null) return;

        // If the node is an atomic sentence, add the variable (e.g., P, Q)
        if (node.getValue().equals("<Atomic Sentence>")) {
            String atomValue = node.getChildren().get(0).getValue();
            if (!"TRUE".equals(atomValue) && !"FALSE".equals(atomValue)) {
                variables.add(atomValue); // Atomic variables
            }
        }

        // Recursively collect variables from child nodes
        for (Node child : node.getChildren()) {
            collectVariables(child);
        }
    }

    public boolean evaluateExpression(Node node, Map<String, Boolean> variableValues, List<String> subExpressions) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null.");
        }
        switch (node.getValue()) {
            case "<Sentence>":
                return evaluateSentence(node, variableValues, subExpressions);
            case "<Complex Sentence>":
                return evaluateComplexSentence(node, variableValues);
            case "<Atomic Sentence>":
                return evaluateAtomic(node, variableValues);
            default:
                throw new RuntimeException("Unsupported node type: " + node.getValue());
        }
    }

    private boolean evaluateSentence(Node node, Map<String, Boolean> variableValues, List<String> subExpressions) {
        if (node == null || node.getChildren().isEmpty()) {
            throw new RuntimeException("Sentence node must have valid children.");
        }

        // Evaluate the leftmost child
        boolean result = evaluateExpression(node.getChildren().get(0), variableValues, subExpressions);

        // Iterate over the connective and subsequent operands
        for (int i = 1; i < node.getChildren().size(); i += 2) {
            String connective = node.getChildren().get(i).getChildren().get(0).getValue().toUpperCase();
            boolean rightOperand = evaluateExpression(node.getChildren().get(i + 1), variableValues, subExpressions);

            // Apply the connective logic and update the result
            switch (connective) {
                case "NOT":
                    result = !result;
                    break;
                case "OR":
                    result = result || rightOperand;
                    break;
                case "AND":
                    result = result && rightOperand;
                    break;
                case "IMPLIES":
                    result = !result || rightOperand;
                    break;
                case "EQUIVALENT":
                    result = result == rightOperand;
                    break;
                default:
                    throw new RuntimeException("Invalid connective: " + connective);
            }
        }

        // Store the result of the current subexpression in the results map
        String currentSubExpression = collectSubExpressions(node, subExpressions);

        if (!subExpressions.contains(currentSubExpression)) {
            subExpressions.add(currentSubExpression);
        }
        results.put(currentSubExpression, result);
        return result;
    }


    private boolean evaluateComplexSentence(Node node, Map<String, Boolean> variableValues) {
        if (node == null) return false;

        // Get the first child of the complex sentence node
        Node firstChild = node.getChildren().get(0);

        // Case 1: Parentheses - Evaluate the sub-expression inside the parentheses
        if (firstChild.getValue().equals("(")) {
            return evaluateExpression(node.getChildren().get(1), variableValues, subExpressions);
        }

        // Case 2: NOT operation - Apply the NOT operation to the sub-expression
        if (firstChild.getValue().equals("NOT")) {
            return !evaluateExpression(node.getChildren().get(1), variableValues, subExpressions);
        }

        // Case 3: Default - Handle complex sentence (compound expressions)
        return evaluateSentence(node, variableValues, subExpressions);
    }

    // Evaluates atomic sentences (like P, Q)
    private boolean evaluateAtomic(Node node, Map<String, Boolean> variableValues) {
        if (node.getChildren().isEmpty()) {
            throw new RuntimeException("Atomic node has no children.");
        }

        // Get the value of the atomic sentence (e.g., P, Q, TRUE, FALSE)
        String atomValue = node.getChildren().get(0).getValue();
        boolean result;

        // If it's a variable look up its value in the variableValues map
        if (variableValues.containsKey(atomValue)) {
            result = variableValues.get(atomValue);
        } else if ("TRUE".equals(atomValue)) {
            result = true;
        } else if ("FALSE".equals(atomValue)) {
            result = false;
        } else {
            throw new RuntimeException("Invalid atomic expression: " + atomValue);
        }
        return result;
    }
}