import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day4 {

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("inputs/day4Input.txt");

        Day4 day4 = new Day4();

        System.out.println(day4.processCardsPart2(input));
    }

    private int processCardsPart2(File input) throws FileNotFoundException {
        int cardNumber = 1;
        Map<Integer, CardData> gameMap = new HashMap<>();
        Deque<Integer> queue = new ArrayDeque<>();
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String winningNumberString = line.substring(line.indexOf(":") + 1, line.indexOf("|")).trim();
            String currentNumberString = line.substring(line.indexOf("|") + 1).trim();
            String[] winningTokens = winningNumberString.split(" +");
            String[] cardTokens = currentNumberString.split(" +");
            Set<Integer> winningNumbers = new HashSet<>();

            Arrays.stream(winningTokens).forEach(numberString -> winningNumbers.add(Integer.parseInt(numberString)));

            gameMap.put(cardNumber, new CardData(winningNumbers, cardTokens));
            queue.add(cardNumber);
            cardNumber++;
        }
        scanner.close();

        return cloneCards(gameMap, queue);
    }

    private int cloneCards(Map<Integer, CardData> gameMap, Deque<Integer> queue) {
        int numCards = queue.size();

        while (!queue.isEmpty()) {
            int currCard = queue.poll();
            CardData cardData = gameMap.get(currCard);

            int matches = getMatchesPart2(cardData.winningNumbers, cardData.gameNumbers);

            for (int i = currCard + 1; i <= currCard + matches; i++) {
                queue.add(i);
                numCards++;
            }
        }
        return numCards;
    }

    private int getMatchesPart2(Set<Integer> winningNumbers, String[] cardTokens) {
        int matches = 0;

        for (String numberString : cardTokens) {
            int number = Integer.parseInt(numberString);

            if (winningNumbers.contains(number)) {
                matches++;
            }
        }
        return matches;
    }

    private int processCardsPart1(File input) throws FileNotFoundException {
        int sum = 0;
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String winningNumberString = line.substring(line.indexOf(":") + 1, line.indexOf("|")).trim();
            String currentNumberString = line.substring(line.indexOf("|") + 1).trim();
            String[] winningTokens = winningNumberString.split(" +");
            String[] cardTokens = currentNumberString.split(" +");
            Set<Integer> winningNumbers = new HashSet<>();

            Arrays.stream(winningTokens).forEach(numberString -> winningNumbers.add(Integer.parseInt(numberString)));

            sum += getScorePart1(winningNumbers, cardTokens);
        }
        scanner.close();

        return sum;
    }

    private int getScorePart1(Set<Integer> winningNumbers, String[] cardTokens) {
        int score = 0;

        for (String numberString : cardTokens) {
            int number = Integer.parseInt(numberString);

            if (winningNumbers.contains(number)) {
                if (score == 0) {
                    score++;
                } else {
                    score *= 2;
                }
            }
        }
        return score;
    }
}

class CardData {
    Set<Integer> winningNumbers;
    String[] gameNumbers;

    public CardData(Set<Integer> winningNumbers, String[] gameNumbers) {
        this.winningNumbers = winningNumbers;
        this.gameNumbers = gameNumbers;
    }
}
