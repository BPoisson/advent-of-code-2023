import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day2 {
    static final int MAX_RED = 12;
    static final int MAX_GREEN = 13;
    static final int MAX_BLUE = 14;

    public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException {
        File file = new File("inputs/day2Input.txt");

        Day2 day2 = new Day2();

        System.out.println(day2.possibleGames(file));
    }

    private int possibleGames(File input) throws FileNotFoundException, IllegalArgumentException {
        int sum = 0;
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            int number = getPossiblePart2(line);

            sum += number;
        }
        scanner.close();

        return sum;
    }

    // Get the product of the min red, green blue cubes needed to make each game possible.
    private int getPossiblePart2(String line) throws IllegalArgumentException {
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;
        String gameString = line.substring(line.indexOf(":") + 2);
        String[] gameTokens = gameString.split("; ");

        for (String game : gameTokens) {
            String[] cubeTokens = game.split(", ");

            for (String cubeToken : cubeTokens) {
                String numString = cubeToken.substring(0, cubeToken.indexOf(" "));
                int num = Integer.parseInt(numString);

                String color = cubeToken.substring(cubeToken.indexOf(" ") + 1);

                switch (color) {
                    case "red" -> maxRed = Math.max(maxRed, num);
                    case "green" -> maxGreen = Math.max(maxGreen, num);
                    case "blue" -> maxBlue = Math.max(maxBlue, num);
                    default -> throw new IllegalArgumentException("Bad stuff happened.");
                }
            }
        }
        return maxRed * maxGreen * maxBlue;
    }

    // Get the sum of all game IDs that are possible - meaning number of cubes for each round doesn't exceed maxes.
    private int getPossiblePart1(String line) throws IllegalAccessException {
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;
        String gameIdString = line.substring(line.indexOf(" ") + 1, line.indexOf(":"));
        int gameId = Integer.parseInt(gameIdString);

        String gameString = line.substring(line.indexOf(":") + 2);
        String[] gameTokens = gameString.split("; ");

        for (String game : gameTokens) {
            String[] cubeTokens = game.split(", ");

            for (String cubeToken : cubeTokens) {
                String numString = cubeToken.substring(0, cubeToken.indexOf(" "));
                int num = Integer.parseInt(numString);

                String color = cubeToken.substring(cubeToken.indexOf(" ") + 1);

                switch (color) {
                    case "red" -> maxRed = Math.max(maxRed, num);
                    case "green" -> maxGreen = Math.max(maxGreen, num);
                    case "blue" -> maxBlue = Math.max(maxBlue, num);
                    default -> throw new IllegalAccessException("Bad stuff happened.");
                }
            }
        }

        if (maxRed > MAX_RED || maxGreen > MAX_GREEN || maxBlue > MAX_BLUE) {
            return 0;
        }
        return gameId;
    }
}
