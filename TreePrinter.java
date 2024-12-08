public class TreePrinter {
    // Function to print the tree
    public static void printTree(Node root) {
        printTreeHelper(root, "", true);
    }

    // Helper function for recursive tree printing
    private static void printTreeHelper(Node node, String prefix, boolean isLast) {
        if (node == null) {
            return;
        }

        // Print the current node with a prefix and connection line
        System.out.println(prefix + (isLast ? "└── " : "├── ") + node.getValue());

        // Update the prefix for children
        String newPrefix = prefix + (isLast ? "    " : "│   ");

        // Recursively print all children
        for (int i = 0; i < node.children.size(); i++) {
            printTreeHelper(node.children.get(i), newPrefix, i == node.children.size() - 1);
        }
    }

    // Main method for testing (optional)
    public static void main(String[] args) {
        // Example usage
        Node root = new Node("<Sentence>");
        Node child1 = new Node("<Atomic Sentence>");
        Node child2 = new Node("P");
        Node child3 = new Node("Q");

        child1.insertChild(child2);
        root.insertChild(child1);
        root.insertChild(child3);

        printTree(root);
    }
}