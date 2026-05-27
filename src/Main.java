import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Directory Size Calculator");
        System.out.println("Commands: cd <name>, cd .., cd /, ls, size, tree, exit");
        System.out.println();

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(" ", 2);
            String cmd = parts[0];

            switch (cmd) {
                case "cd":
                    if (parts.length < 2) {
                        System.out.println("usage: cd <name>");
                    } else {
                        fs.cd(parts[1]);
                    }
                    break;
                case "ls":
                    fs.ls();
                    break;
                case "size":
                    System.out.println(fs.size() + " bytes");
                    break;
                case "tree":
                    fs.tree();
                    break;
                case "exit":
                case "quit":
                    return;
                default:
                    System.out.println("unknown command: " + cmd);
            }
        }
    }
}
