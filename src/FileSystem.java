public class FileSystem {
    Node root;
    Node current;

    public FileSystem() {
        root = new Node("/", 0, false, null);
        current = root;
        seed();
    }

    private void seed() {
        Node docs = addDir(root, "docs");
        addFile(docs, "readme.txt", 1024);
        addFile(docs, "guide.pdf", 50000);

        Node photos = addDir(root, "photos");
        Node vacation = addDir(photos, "vacation");
        addFile(vacation, "beach.jpg", 2000000);
        addFile(vacation, "sunset.jpg", 1500000);
        addFile(photos, "profile.jpg", 500000);

        Node code = addDir(root, "code");
        Node project = addDir(code, "project");
        Node src = addDir(project, "src");
        addFile(src, "main.java", 3500);
        addFile(src, "utils.java", 1200);
        Node tests = addDir(project, "tests");
        addFile(tests, "test.java", 800);
        addFile(code, "notes.md", 250);
    }

    static Node addFile(Node parent, String name, int size) {
        Node n = new Node(name, size, true, parent);
        parent.children.add(n);
        return n;
    }

    static Node addDir(Node parent, String name) {
        Node n = new Node(name, 0, false, parent);
        parent.children.add(n);
        return n;
    }

    public void cd(String target) {
        if (target.equals("..")) {
            if (current.parent != null) {
                current = current.parent;
            }
            return;
        }
        if (target.equals("/")) {
            current = root;
            return;
        }
        for (Node child : current.children) {
            if (child.name.equals(target) && !child.isFile) {
                current = child;
                return;
            }
        }
        System.out.println("cd: no such directory: " + target);
    }

    public void ls() {
        if (current.children.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (Node child : current.children) {
            if (child.isFile) {
                System.out.println("[FILE] " + child.name + " (" + child.size + " bytes)");
            } else {
                System.out.println("[DIR]  " + child.name + "/");
            }
        }
    }

    public int size() {
        return calculateSize(current);
    }

    static int calculateSize(Node node) {
        if (node.isFile) {
            return node.size;
        }
        int total = 0;
        for (Node child : node.children) {
            total += calculateSize(child);
        }
        return total;
    }

    public void tree() {
        printTree(current, 0);
    }

    private void printTree(Node node, int depth) {
        String indent = "  ".repeat(depth);
        if (node.isFile) {
            System.out.println(indent + node.name + " (" + node.size + " bytes)");
        } else {
            System.out.println(indent + node.name + "/");
            for (Node child : node.children) {
                printTree(child, depth + 1);
            }
        }
    }
}
