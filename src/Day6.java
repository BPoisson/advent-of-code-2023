import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day6 {

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("inputs/day6Input.txt");

        Day6 day6 = new Day6();

        System.out.println(day6.processInputPart2(input));
    }

    // The input is now treated as a single race, concatenating the time and distance values.
    private long processInputPart2(File input) throws FileNotFoundException {
        Scanner scanner = new Scanner(input);

        String times = scanner.nextLine();
        String[] timeTokens = times.substring(times.indexOf(":") + 1).trim().split(" +");
        String distances = scanner.nextLine();
        String[] distanceTokens = distances.substring(distances.indexOf(":") + 1).trim().split(" +");

        scanner.close();

        long time = getLongVal(timeTokens);
        long distance = getLongVal(distanceTokens);

        return waysToWinPart2(time, distance);
    }

    private long waysToWinPart2(long time, long distance) {
        int left = 1;

        while ((time - left) * left <= distance) {
            left++;
        }
        return time - (left * 2L) + 1;
    }

    // For each race, determine the number of ways we can beat the record distance in the time limit.
    // The boat moves mm/ms based on the duration that a button is held. 1ms -> 1 mm/ms, 2ms -> 2mm/ms.
    // The time we spend holding the button is included in the total race time.
    private long processInputPart1(File input) throws FileNotFoundException {
        Scanner scanner = new Scanner(input);

        String times = scanner.nextLine();
        String[] timeTokens = times.substring(times.indexOf(":") + 1).trim().split(" +");
        String distances = scanner.nextLine();
        String[] distanceTokens = distances.substring(distances.indexOf(":") + 1).trim().split(" +");

        int[][] raceData = new int[timeTokens.length][2];
        for (int i = 0; i < raceData.length; i++) {
            raceData[i] = new int[] {Integer.parseInt(timeTokens[i]), Integer.parseInt(distanceTokens[i])};
        }
        scanner.close();

        return waysToWin(raceData);
    }

    // Based on example in comments below, the ways to win form a mirror image.
    // We don't need to scan through each combination. Just find the first time we win and compute the rest.
    private long waysToWin(int[][] raceData) {
        long result = 1;

        for (int[] race : raceData) {
            int time = race[0];
            int distance = race[1];

            int left = 1;

            while ((time - left) * left <= distance) {
                left++;
            }
            result *= time - (left * 2L) + 1;
        }
        return result;
    }

    private long getLongVal(String[] tokens) {
        StringBuilder sb = new StringBuilder();

        for (String token : tokens) {
            sb.append(token);
        }
        return Long.parseLong(sb.toString());
    }
}

//Time:      7  15   30
//Distance:  9  40  200


//7 - 6 * 6 = 1 * 6 = 6
//7 - 5 * 5 = 2 * 5 = 10
//7 - 4 * 4 = 3 * 4 = 12
//7 - 3 * 3 = 4 * 3 = 12
//7 - 2 * 2 = 5 * 2 = 10
//7 - 1 * 1 = 6 * 1 = 6


//Don't hold the button at all (that is, hold it for 0 milliseconds) at the start of the race. The boat won't move; it will have traveled 0 millimeters by the end of the race.
//Hold the button for 1 millisecond at the start of the race. Then, the boat will travel at a speed of 1 millimeter per millisecond for 6 milliseconds, reaching a total distance traveled of 6 millimeters.
//Hold the button for 2 milliseconds, giving the boat a speed of 2 millimeters per millisecond. It will then get 5 milliseconds to move, reaching a total distance of 10 millimeters.
//Hold the button for 3 milliseconds. After its remaining 4 milliseconds of travel time, the boat will have gone 12 millimeters.
//Hold the button for 4 milliseconds. After its remaining 3 milliseconds of travel time, the boat will have gone 12 millimeters.
//Hold the button for 5 milliseconds, causing the boat to travel a total of 10 millimeters.
//Hold the button for 6 milliseconds, causing the boat to travel a total of 6 millimeters.
//Hold the button for 7 milliseconds. That's the entire duration of the race. You never let go of the button. The boat can't move until you let go of the button. Please make sure you let go of the button so the boat gets to move. 0 millimeters.
