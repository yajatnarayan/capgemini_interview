import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FileSystemTest {

    @Test
    void emptyDirectoryHasZeroSize() {
        Node empty = new Node("empty", 0, false, null);
        assertEquals(0, FileSystem.calculateSize(empty));
    }

    @Test
    void singleFileReturnsItsSize() {
        Node dir = new Node("dir", 0, false, null);
        FileSystem.addFile(dir, "a.txt", 100);
        assertEquals(100, FileSystem.calculateSize(dir));
    }

    @Test
    void filePassedDirectlyReturnsOwnSize() {
        Node file = new Node("solo.txt", 42, true, null);
        assertEquals(42, FileSystem.calculateSize(file));
    }

    @Test
    void nestedDirectoriesSumAllFiles() {
        Node root = new Node("root", 0, false, null);
        FileSystem.addFile(root, "top.txt", 10);
        Node sub = FileSystem.addDir(root, "sub");
        FileSystem.addFile(sub, "nested.txt", 20);
        Node deeper = FileSystem.addDir(sub, "deeper");
        FileSystem.addFile(deeper, "deep.txt", 30);
        assertEquals(60, FileSystem.calculateSize(root));
    }

    @Test
    void deepNestingHandledByRecursion() {
        Node root = new Node("root", 0, false, null);
        Node current = root;
        for (int i = 0; i < 10; i++) {
            current = FileSystem.addDir(current, "level" + i);
        }
        FileSystem.addFile(current, "deep.txt", 1000);
        assertEquals(1000, FileSystem.calculateSize(root));
    }

    @Test
    void seedTreeTotalSize() {
        // Expected total derived from FileSystem.seed():
        //   docs:   1024 + 50000                = 51024
        //   photos: 2000000 + 1500000 + 500000  = 4000000
        //   code:   3500 + 1200 + 800 + 250     = 5750
        //   total                               = 4056774
        FileSystem fs = new FileSystem();
        assertEquals(4056774, FileSystem.calculateSize(fs.root));
    }
}
