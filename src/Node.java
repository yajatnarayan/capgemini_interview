import java.util.ArrayList;
import java.util.List;

public class Node {
    String name;
    int size;
    boolean isFile;
    List<Node> children;
    Node parent;

    public Node(String name, int size, boolean isFile, Node parent) {
        this.name = name;
        this.size = size;
        this.isFile = isFile;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
}
