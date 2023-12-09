import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day9 {

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("inputs/day9Input.txt");

        Day9 day9 = new Day9();

        System.out.println(day9.processInputPart2(input));
    }

    private long processInputPart2(File input) throws FileNotFoundException {
        long nextValSum = 0;
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            long[] nums = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray();

            nextValSum += getNextValPart2(nums);
        }
        scanner.close();

        return nextValSum;
    }

    private long processInputPart1(File input) throws FileNotFoundException {
        long nextValSum = 0;
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            long[] nums = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray();

            nextValSum += getNextValPart1(nums);
        }
        scanner.close();

        return nextValSum;
    }

    /**
     * This is the same as Part 1, except when going back up, instead of adding to the end,
     * we subtract from the start of the arrays.
     */
    private long getNextValPart2(long[] nums) {
        long nextVal = 0;
        boolean zeroDiffs = false;
        Deque<long[]> stack = new ArrayDeque<>();
        stack.push(nums);
        long[] currNums = nums;

        while (!zeroDiffs) {
            long[] nextNums = new long[currNums.length - 1];
            zeroDiffs = true;

            for (int i = 0; i < currNums.length - 1; i++) {
                long diff = currNums[i + 1] - currNums[i];
                zeroDiffs &= diff == 0;

                nextNums[i] = diff;

            }
            stack.push(nextNums);
            currNums = nextNums;
        }

        while (!stack.isEmpty()) {
            currNums = stack.pop();
            currNums[0] -= nextVal;
            nextVal = currNums[0];
        }
        return nextVal;
    }

    /** Get the differences of each number in the list to the previous number.
     * If all differences are not yet zero, continue on the list of differences.
     * When all differences are zero, we now have a recursive stack of arrays of differences.
     * Take the last number in the previous list of differences and at it to the
     * last number in the current list of differences.
     * Once we are back to the original list, return it's new next number at the end of the list.
     */
    private long getNextValPart1(long[] nums) {
        long nextVal = 0;
        boolean zeroDiffs = false;
        Deque<long[]> stack = new ArrayDeque<>();
        stack.push(nums);
        long[] currNums = nums;

        while (!zeroDiffs) {
            long[] nextNums = new long[currNums.length - 1];
            zeroDiffs = true;

            for (int i = 0; i < currNums.length - 1; i++) {
                long diff = currNums[i + 1] - currNums[i];
                zeroDiffs &= diff == 0;

                nextNums[i] = diff;

            }
            stack.push(nextNums);
            currNums = nextNums;
        }

        while (!stack.isEmpty()) {
            currNums = stack.pop();
            currNums[currNums.length - 1] += nextVal;
            nextVal = currNums[currNums.length - 1];
        }
        return nextVal;
    }
}