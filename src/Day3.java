import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day3 {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("inputs/day3Input.txt");
        Day3 day3 = new Day3();

        System.out.println(day3.getSum(file));
    }

    private int getSum(File input) throws FileNotFoundException {
        char[][] chars = new char[140][140];
        Scanner scanner = new Scanner(input);

        for (int i = 0; i < 140; i++) {
            chars[i] = scanner.nextLine().toCharArray();
        }
        scanner.close();
        return getSumPart2(chars);
    }

    // Sum the products for each gear symbol * that is next to exactly two numbers.
    private int getSumPart2(char[][] chars) {
        int sum = 0;
        int[][] dirs = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};

        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[0].length; j++) {
                char currChar = chars[i][j];

                if (currChar == '*') {
                    Set<String> processed = new HashSet<>();
                    List<Integer> adjacentNums = new ArrayList<>();

                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];

                        if (isValidPart2(chars, processed, nextRow, nextCol)
                                && Character.isDigit(chars[nextRow][nextCol])) {
                            int currNum = 0;
                            int leftBoundary = nextCol;
                            int rightBoundary = nextCol;

                            while (leftBoundary > 0 && Character.isDigit(chars[nextRow][leftBoundary - 1])) {
                                leftBoundary--;
                            }
                            while (rightBoundary < chars[0].length - 1
                                    && Character.isDigit(chars[nextRow][rightBoundary + 1])) {
                                rightBoundary++;
                            }

                            for (int k = leftBoundary; k <= rightBoundary; k++) {
                                currNum *= 10;
                                currNum += chars[nextRow][k] - '0';
                                processed.add(nextRow + "." + k);
                            }
                            adjacentNums.add(currNum);
                        }
                    }
                    if (adjacentNums.size() == 2) {
                        sum += adjacentNums.get(0) * adjacentNums.get(1);
                    }
                }
            }
        }
        return sum;
    }

    private boolean isValidPart2(char[][] chars, Set<String> processed, int row, int col) {
        return row >= 0 && col >= 0
                && row < chars.length && col < chars[0].length
                && !processed.contains(row + "." + col);
    }

    // Sum all numbers next to non-period, non-number symbols.
    private int getSumPart1(char[][] chars) {
        int sum = 0;
        int[][] dirs = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};

        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[0].length; j++) {
                char currChar = chars[i][j];
                boolean isAdjacent = false;

                if (Character.isDigit(currChar)) {
                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];

                        if (isValid1(chars, nextRow, nextCol)
                                && !Character.isDigit(chars[nextRow][nextCol])
                                && chars[nextRow][nextCol] != '.') {
                            isAdjacent = true;
                            break;
                        }
                    }

                    if (isAdjacent) {
                        int currNum = 0;
                        int leftBoundary = j;
                        int rightBoundary = j;

                        while (leftBoundary > 0 && Character.isDigit(chars[i][leftBoundary - 1])) {
                            leftBoundary--;
                        }
                        while (rightBoundary < chars[0].length - 1
                                && Character.isDigit(chars[i][rightBoundary + 1])) {
                            rightBoundary++;
                        }

                        for (int k = leftBoundary; k <= rightBoundary; k++) {
                            currNum *= 10;
                            currNum += chars[i][k] - '0';
                        }
                        sum += currNum;
                        j = rightBoundary + 1;
                    }
                }
            }
        }
        return sum;
    }

    private boolean isValid1(char[][] chars, int row, int col) {
        return row >= 0 && col >= 0 && row < chars.length && col < chars[0].length;
    }
}
