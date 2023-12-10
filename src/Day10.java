import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day10 {

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("inputs/day10Input.txt");

        Day10 day10 = new Day10();

        System.out.println(day10.processInputPart2(input));
    }

    // Find the number of cells enclosed by the loop of pipes.
    private long processInputPart2(File input) throws FileNotFoundException {
        int startRow = 0;
        int startCol = 0;
        List<List<Character>> graph = new ArrayList<>();
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Character> row = new ArrayList<>();

            for (int i = 0; i < line.length(); i++) {
                char currChar = line.charAt(i);

                if (currChar == 'S') {
                    startRow = graph.size();
                    startCol = i;
                }
                row.add(currChar);
            }
            graph.add(row);
        }
        scanner.close();

        fillPath(graph, startRow, startCol);

        return findNodes(graph);
    }

    private long findNodes(List<List<Character>> graph) {
        long nodes = 0;

        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < graph.get(0).size(); j++) {
                if (graph.get(i).get(j) != '#' && graph.get(i).get(j) != '$') {
                    nodes += count(graph, i, j);
                }
            }
        }
        return nodes;
    }

    private long count(List<List<Character>> graph, int startRow, int startCol) {
        long count = 0;
        Deque<int[]> queue = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        int[][] dirs = new int[][] {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        queue.add(new int[] {startRow, startCol});
        List<int[]> rowCols = new ArrayList<>();

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int row = curr[0];
            int col = curr[1];

            if (seen.contains(row + "." + col)) {
                continue;
            }
            seen.add(row + "." + col);

            for (int[] dir : dirs) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];

                if (!inBounds(graph, nextRow, nextCol)) {
                    return 0;
                }
                if (graph.get(nextRow).get(nextCol) == '#' || seen.contains(nextRow + "." + nextCol)) {
                    continue;
                }
                queue.add(new int[] {nextRow, nextCol});
                rowCols.add(new int[] {nextRow, nextCol});
            }
            rowCols.add(new int[] {row, col});
            count++;
        }

        for (int[] rowCol : rowCols) {
            graph.get(rowCol[0]).set(rowCol[1], '$');
        }
        return count;
    }

    private void fillPath(List<List<Character>> graph, int startRow, int startCol) {
        int count = 0;
        List<int[]> fillCells = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[] {startRow, startCol});
        seen.add(startRow + "." + startCol);

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                int row = curr[0];
                int col = curr[1];

                fillCells.add(new int[] {row, col});
                seen.add(row + "." + col);

                List<List<Integer>> nextNodes;
                if (graph.get(row).get(col) == 'S') {
                    nextNodes = getAfterSNodes(graph, new HashSet<>(), row, col);
                } else {
                    nextNodes = getNextNodes(graph, graph.get(row).get(col), row, col);
                }

                if (nextNodes.isEmpty()) {
                    continue;
                }

                for (List<Integer> nextNode: nextNodes) {
                    int nextRow = nextNode.get(0);
                    int nextCol = nextNode.get(1);
                    String nextString = nextRow + "." + nextCol;

                    if (!isValid(graph, new HashSet<>(), nextRow, nextCol) || seen.contains(nextString)) {
                        continue;
                    }
                    queue.add(new int[] {nextRow, nextCol});
                    seen.add(nextString);
                }
            }
            count = queue.isEmpty() ? count : count + 1;
        }

        for (int[] fillCell : fillCells) {
            graph.get(fillCell[0]).set(fillCell[1], '#');
        }
        System.out.println(count);
    }

    private long processInputPart1(File input) throws FileNotFoundException {
        int startRow = 0;
        int startCol = 0;
        List<List<Character>> graph = new ArrayList<>();
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Character> row = new ArrayList<>();

            for (int i = 0; i < line.length(); i++) {
                char currChar = line.charAt(i);

                if (currChar == 'S') {
                    startRow = graph.size();
                    startCol = i;
                }
                row.add(currChar);
            }
            graph.add(row);
        }
        scanner.close();

        return getPathPart1(graph, startRow, startCol);
    }

    // Find the distance to the furthest cell in the grid of pipes.
    private long getPathPart1(List<List<Character>> graph, int startRow, int startCol) {
        int steps = 0;
        Set<String> seen = new HashSet<>();
        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[] {startRow, startCol});
        seen.add(startRow + "." + startCol);

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                int row = curr[0];
                int col = curr[1];

                List<List<Integer>> nextNodes;
                if (graph.get(row).get(col) == 'S') {
                    nextNodes = getAfterSNodes(graph, seen, row, col);
                } else {
                    nextNodes = getNextNodes(graph, graph.get(row).get(col), row, col);
                }

                if (nextNodes.isEmpty()) {
                    continue;
                }

                for (List<Integer> nextNode: nextNodes) {
                    int nextRow = nextNode.get(0);
                    int nextCol = nextNode.get(1);
                    String nextString = nextRow + "." + nextCol;

                    if (!isValid(graph, seen, nextRow, nextCol)) {
                        continue;
                    }
                    seen.add(nextString);
                    queue.add(new int[] {nextRow, nextCol});
                }
            }
            steps = queue.isEmpty() ? steps : steps + 1;
        }
        return steps;
    }

    private boolean isValid(List<List<Character>> graph, Set<String> seen, int nextRow, int nextCol) {
        String nextString = nextRow + "." + nextCol;

        return nextRow >= 0 && nextCol >= 0 && nextRow < graph.size() && nextCol < graph.get(0).size()
                && !seen.contains(nextString)
                && graph.get(nextRow).get(nextCol) != '.';
    }

    private List<List<Integer>> getAfterSNodes(List<List<Character>> graph, Set<String> seen, int row, int col) {
        List<List<Integer>> nextNodes = new ArrayList<>();

        if (isValid(graph, seen, row, col - 1)) {
            char nextPipe = graph.get(row).get(col - 1);

            if (nextPipe == 'F' || nextPipe == 'L' || nextPipe == '-') {
                nextNodes.add(Arrays.asList(row, col - 1));
            }
        }
        if (isValid(graph, seen, row, col + 1)) {
            char nextPipe = graph.get(row).get(col + 1);

            if (nextPipe == '7' || nextPipe == 'J' || nextPipe == '-') {
                nextNodes.add(Arrays.asList(row, col + 1));
            }
        }
        if (isValid(graph, seen, row - 1, col)) {
            char nextPipe = graph.get(row - 1).get(col);

            if (nextPipe == '7' || nextPipe == 'F' || nextPipe == '|') {
                nextNodes.add(Arrays.asList(row - 1, col));
            }
        }
        if (isValid(graph, seen, row + 1, col)) {
            char nextPipe = graph.get(row + 1).get(col);

            if (nextPipe == 'L' || nextPipe == 'J' || nextPipe == '|') {
                nextNodes.add(Arrays.asList(row + 1, col));
            }
        }
        return nextNodes;
    }

    //    | is a vertical pipe connecting north and south.
    //    - is a horizontal pipe connecting east and west.
    //    L is a 90-degree bend connecting north and east.
    //    J is a 90-degree bend connecting north and west.
    //    7 is a 90-degree bend connecting south and west.
    //    F is a 90-degree bend connecting south and east.
    //    . is ground; there is no pipe in this tile.
    //    S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
    private List<List<Integer>> getNextNodes(List<List<Character>> graph, char pipe, int row, int col) {
        List<List<Integer>> result = new ArrayList<>();

        switch (pipe) {
            case '|':
                if (inBounds(graph, row - 1, col)) {
                    char nextPipe = graph.get(row - 1).get(col);

                    if (nextPipe == 'F' || nextPipe == '7' || nextPipe == '|') {
                        result.add(Arrays.asList(row - 1, col));
                    }
                }
                if (inBounds(graph, row + 1, col)) {
                    char nextPipe = graph.get(row + 1).get(col);

                    if (nextPipe == 'L' || nextPipe == 'J' || nextPipe == '|') {
                        result.add(Arrays.asList(row + 1, col));
                    }
                }
                break;
            case '-':
                if (inBounds(graph, row, col + 1)) {
                    char nextPipe = graph.get(row).get(col + 1);

                    if (nextPipe == 'J' || nextPipe == '7' || nextPipe == '-') {
                        result.add(Arrays.asList(row, col + 1));
                    }
                }
                if (inBounds(graph, row, col - 1)) {
                    char nextPipe = graph.get(row).get(col - 1);

                    if (nextPipe == 'L' || nextPipe == 'F' || nextPipe == '-') {
                        result.add(Arrays.asList(row, col - 1));
                    }
                }
                break;
            case 'L':
                if (inBounds(graph, row - 1, col)) {
                    char nextPipe = graph.get(row - 1).get(col);

                    if (nextPipe == 'F' || nextPipe == '7' || nextPipe == '|') {
                        result.add(Arrays.asList(row - 1, col));
                    }
                }
                if (inBounds(graph, row, col + 1)) {
                    char nextPipe = graph.get(row).get(col + 1);

                    if (nextPipe == '7' || nextPipe == 'J' || nextPipe == '-') {
                        result.add(Arrays.asList(row, col + 1));
                    }
                }
                break;
            case 'J':
                if (inBounds(graph, row - 1, col)) {
                    char nextPipe = graph.get(row - 1).get(col);

                    if (nextPipe == 'F' || nextPipe == '7' || nextPipe == '|') {
                        result.add(Arrays.asList(row - 1, col));
                    }
                }
                if (inBounds(graph, row, col - 1)) {
                    char nextPipe = graph.get(row).get(col - 1);

                    if (nextPipe == 'F' || nextPipe == 'L' || nextPipe == '-') {
                        result.add(Arrays.asList(row, col - 1));
                    }
                }
                break;
            case '7':
                if (inBounds(graph, row + 1, col)) {
                    char nextPipe = graph.get(row + 1).get(col);

                    if (nextPipe == 'J' || nextPipe == 'L' || nextPipe == '|') {
                        result.add(Arrays.asList(row + 1, col));
                    }
                }
                if (inBounds(graph, row, col - 1)) {
                    char nextPipe = graph.get(row).get(col - 1);

                    if (nextPipe == 'F' || nextPipe == 'L' || nextPipe == '-') {
                        result.add(Arrays.asList(row, col - 1));
                    }
                }
                break;
            case 'F':
                if (inBounds(graph, row + 1, col)) {
                    char nextPipe = graph.get(row + 1).get(col);

                    if (nextPipe == 'L' || nextPipe == 'J' || nextPipe == '|') {
                        result.add(Arrays.asList(row + 1, col));
                    }
                }
                if (inBounds(graph, row, col + 1)) {
                    char nextPipe = graph.get(row).get(col + 1);

                    if (nextPipe == '7' || nextPipe == 'J' || nextPipe == '-') {
                        result.add(Arrays.asList(row, col + 1));
                    }
                }
                break;
        }
        return result;
    }

    private boolean inBounds(List<List<Character>> graph, int row, int col) {
        return row >= 0 && col >= 0 && row < graph.size() && col < graph.get(0).size();
    }
}

// 864