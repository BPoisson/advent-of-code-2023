import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day1 {

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("inputs/day1Input.txt");

        Day1 day1 = new Day1();

        System.out.println(day1.decode(input));
    }

    private int decode(File input) throws FileNotFoundException {
        int sum = 0;
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            int number = getNumber(line);

            sum += number;
        }
        scanner.close();

        return sum;
    }

    private int getNumber(String line) {
        int left = 0;
        int right = line.length() - 1;
        char leftChar = '-';
        char rightChar = '-';

        while (left <= right) {
            if (!Character.isDigit(leftChar)) {
                leftChar = line.charAt(left);
            }
            if (!Character.isDigit(rightChar)) {
                rightChar = line.charAt(right);
            }

            if (Character.isDigit(leftChar) && Character.isDigit(rightChar)) {
                break;
            }
            if (!Character.isDigit(leftChar)) {
                char stringDigit = getStringDigit(line, left);

                if (Character.isDigit(stringDigit)) {
                    leftChar = stringDigit;
                } else {
                    left++;
                }
            }
            if (!Character.isDigit(rightChar)) {
                char stringDigit = getStringDigit(line, right);

                if (Character.isDigit(stringDigit)) {
                    rightChar = stringDigit;
                } else {
                    right--;
                }
            }
        }

        if (Character.isDigit(leftChar) && Character.isDigit(rightChar)) {
            return (leftChar - '0') * 10 + (rightChar - '0');
        }
        return 0;
    }

    private char getStringDigit(String line, int index) {
        if (index <= line.length() - 3) {
            String[] nums = new String[] {"one", "two", "six"};

            for (String num : nums) {
                if (line.substring(index).startsWith(num)) {
                    return getDigit(num);
                }
            }
        }
        if (index <= line.length() - 4) {
            String[] nums = new String[] {"four", "five", "nine"};

            for (String num : nums) {
                if (line.substring(index).startsWith(num)) {
                    return getDigit(num);
                }
            }
        }
        if (index <= line.length() - 5) {
            String[] nums = new String[]{"three", "seven", "eight"};

            for (String num : nums) {
                if (line.substring(index).startsWith(num)) {
                    return getDigit(num);
                }
            }
        }
        return '-';
    }

    private char getDigit(String num) {
        return switch (num) {
            case "one" -> '1';
            case "two" -> '2';
            case "three" -> '3';
            case "four" -> '4';
            case "five" -> '5';
            case "six" -> '6';
            case "seven" -> '7';
            case "eight" -> '8';
            case "nine" -> '9';
            default -> '0';
        };
    }
}
