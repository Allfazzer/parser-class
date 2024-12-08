import java.util.ArrayList;

public class Node {
    // Value of the current node
    private String value;

    // Children node of the current node
    ArrayList <Node> children;

    // Constructor
    public Node(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    // get method of value
    public String getValue() {
        return this.value;
    }

    // Insert a child to the current node
    public void insertChild(Node child) {
        children.add(child);
    }
}