# Directory Size Calculator

A small Java application that simulates a file system in memory and supports `cd`, `ls`, and `size` commands. Built for the Capgemini take-home.

## Approach

The file system is modeled as a tree of `Node` objects. Each `Node` is either:
- a **file** with a size in bytes, or
- a **directory** with a list of child `Node`s.

Every node also holds a reference to its parent, which makes `cd ..` a one-line operation (no separate path stack needed).

The `size` command is implemented **recursively**: a file returns its own size; a directory sums the sizes of all its children. That's the recursion the prompt calls out as the ideal approach.

A seed file system is built in the `FileSystem` constructor so the app has data to explore the moment you launch it — no manual setup.

## Files

```
src/
  Node.java            data model: name, size, isFile, children, parent
  FileSystem.java      tree, commands (cd/ls/size/tree), seed data
  Main.java            REPL loop
  FileSystemTest.java  JUnit 5 tests for calculateSize
lib/
  junit-platform-console-standalone-6.1.0.jar   bundled JUnit (no build tool needed)
README.md
```

Three production files plus a JUnit 5 test. **No build tool (no Maven/Gradle):** JUnit ships as a single self-contained jar checked into `lib/`, so the project still clones and runs with just `javac`/`java`. Requires **JDK 17+** (JUnit 6 needs it; the app on its own runs on JDK 11+).

## Run the application

From the repo root:

```
javac -d out src/Node.java src/FileSystem.java src/Main.java
java -cp out Main
```

You'll get an interactive prompt:

```
Directory Size Calculator
Commands: cd <name>, cd .., cd /, ls, size, tree, exit

> ls
[DIR]  docs/
[DIR]  photos/
[DIR]  code/
> cd photos
> ls
[DIR]  vacation/
[FILE] profile.jpg (500000 bytes)
> size
4000000 bytes
> tree
photos/
  vacation/
    beach.jpg (2000000 bytes)
    sunset.jpg (1500000 bytes)
  profile.jpg (500000 bytes)
> cd ..
> exit
```

## Run the tests

Tests use **JUnit 5**, bundled as the standalone jar in `lib/`. Compile with the jar on the classpath, then run via the JUnit console launcher:

```
javac -cp lib/junit-platform-console-standalone-6.1.0.jar -d out src/Node.java src/FileSystem.java src/FileSystemTest.java
java -jar lib/junit-platform-console-standalone-6.1.0.jar execute --class-path out --scan-class-path
```

Expected output:

```
├─ JUnit Jupiter ✔
│  └─ FileSystemTest ✔
│     ├─ filePassedDirectlyReturnsOwnSize() ✔
│     ├─ singleFileReturnsItsSize() ✔
│     ├─ deepNestingHandledByRecursion() ✔
│     ├─ emptyDirectoryHasZeroSize() ✔
│     ├─ seedTreeTotalSize() ✔
│     └─ nestedDirectoriesSumAllFiles() ✔

[         6 tests successful      ]
[         0 tests failed          ]
```

The launcher exits non-zero if any test fails (useful for CI).

The tests cover:
- empty directory → 0
- single file in a directory → returns the file's size
- a file (not a directory) passed to `calculateSize` → returns its own size
- nested directories with files at multiple depths → correct sum
- 10 levels of nesting → recursion handles depth correctly
- the seeded tree as a whole → matches the known total of 4,056,774 bytes

## Seed data

The `FileSystem` constructor builds this tree:

```
/
├── docs/
│   ├── readme.txt        (1,024 bytes)
│   └── guide.pdf         (50,000 bytes)
├── photos/
│   ├── vacation/
│   │   ├── beach.jpg     (2,000,000 bytes)
│   │   └── sunset.jpg    (1,500,000 bytes)
│   └── profile.jpg       (500,000 bytes)
└── code/
    ├── project/
    │   ├── src/
    │   │   ├── main.java   (3,500 bytes)
    │   │   └── utils.java  (1,200 bytes)
    │   └── tests/
    │       └── test.java   (800 bytes)
    └── notes.md            (250 bytes)
```

**Total: 4,056,774 bytes**

## Verifying behavior manually

A quick sanity check after launch:

1. `size` at root → `4056774 bytes`
2. `cd photos`, `size` → `4000000 bytes`
3. `cd vacation`, `size` → `3500000 bytes`
4. `cd ..`, `cd ..` → back at root
5. `tree` → prints the full hierarchy from current directory
