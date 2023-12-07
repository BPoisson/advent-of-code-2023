import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day7 {

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("inputs/day7Input.txt");

        Day7 day7 = new Day7();

        System.out.println(day7.processInputPart1(input));
    }

    private long processInputPart1(File input) throws FileNotFoundException {
        Scanner scanner = new Scanner(input);
        List<Pair<String, Integer>> hands = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String hand = line.substring(0, line.indexOf(" "));
            int bid = Integer.parseInt(line.substring(line.indexOf(" ") + 1));

            hands.add(new Pair<>(hand, bid));
        }
        scanner.close();

        return totalWinnings(hands);
    }

    // Winnings are the bid multiplied by the hand's calculated rank out of all hands.
    // Return total winnings for all hands based on their ranks with respect to each other.
    private long totalWinnings(List<Pair<String, Integer>> hands) {
        long winnings = 0;

        sortHands(hands);

        for (long i = 0; i < hands.size(); i++) {
            winnings += hands.get((int) i).value * (i + 1);
        }
        return winnings;
    }

    // Sort hands first based on rank of the hand type.
    // If ranks are the same, go left-to-right and check each card until one wins.
    private void sortHands(List<Pair<String, Integer>> hands) {
        Map<Character, Integer> cardRanks = getCardRanksPart2();

        hands.sort((o1, o2) -> {
            String hand1 = o1.key;
            String hand2 = o2.key;

            if (hand1.equals(hand2)) {
                return 0;
            }

            int hand1Score = getHandScorePart2(hand1);
            int hand2Score = getHandScorePart2(hand2);

            if (hand1Score != hand2Score) {
                return hand1Score - hand2Score;
            }

            for (int i = 0; i < hand1.length(); i++) {
                char hand1Char = hand1.charAt(i);
                char hand2Char = hand2.charAt(i);

                if (hand1Char != hand2Char) {
                    return cardRanks.get(hand1Char) - cardRanks.get(hand2Char);
                }
            }
            return 0;
        });
    }

    // Jokers will now become whichever card produces the highest rank, following the rules from Part 1.
    // Find a most frequently occurring card, and replace all jokers with that value.
    // Use the scoring logic from Part 1's solution on the new modified card to get the result.
    private int getHandScorePart2(String hand) {
        int nonJokerMaxCount = 0;
        char nonJokerMaxCard = '-';
        Map<Character, Integer> counts = new HashMap<>();

        for (int i = 0; i < hand.length(); i++) {
            char card = hand.charAt(i);

            counts.put(card, counts.getOrDefault(card, 0) + 1);

            if (card != 'J' && counts.get(card) >= nonJokerMaxCount) {
                nonJokerMaxCount = counts.get(card);
                nonJokerMaxCard = card;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.length(); i++) {
            char currCard = hand.charAt(i);

            if (currCard == 'J') {
                sb.append(nonJokerMaxCard);
            } else {
                sb.append(currCard);
            }
        }

        return getHandScorePart1(sb.toString());
    }

    //  Five of a kind, where all five cards have the same label: AAAAA
    //  Four of a kind, where four cards have the same label and one card has a different label: AA8AA
    //  Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
    //  Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
    //  Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
    //  One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
    //  High card, where all cards' labels are distinct: 23456
    private int getHandScorePart1(String hand) {
        int maxCount = 0;
        Map<Character, Integer> counts = new HashMap<>();

        for (int i = 0; i < hand.length(); i++) {
            char card = hand.charAt(i);

            counts.put(card, counts.getOrDefault(card, 0) + 1);

            maxCount = Math.max(maxCount, counts.get(card));
        }

        // Five of a kind.
        if (counts.size() == 1) {
            return 7;
        }
        // Four of a kind or full house.
        if (counts.size() == 2) {
            return maxCount == 4 ? 6 : 5;
        }
        // Three of a kind or two pair.
        if (counts.size() == 3) {
            return maxCount == 3 ? 4 : 3;
        }
        // Two or one of a kind.
        return counts.size() == 4 ? 2 : 1;
    }

    private Map<Character, Integer> getCardRanksPart1() {
        Map<Character, Integer> cardRanks = new HashMap<>();
        cardRanks.put('A', 14);
        cardRanks.put('K', 13);
        cardRanks.put('Q', 12);
        cardRanks.put('J', 11);
        cardRanks.put('T', 10);
        cardRanks.put('9', 9);
        cardRanks.put('8', 8);
        cardRanks.put('7', 7);
        cardRanks.put('6', 6);
        cardRanks.put('5', 5);
        cardRanks.put('4', 4);
        cardRanks.put('3', 3);
        cardRanks.put('2', 2);

        return cardRanks;
    }

    // Jokers are ranked lowest for part 2.
    private Map<Character, Integer> getCardRanksPart2() {
        Map<Character, Integer> cardRanks = new HashMap<>();
        cardRanks.put('A', 14);
        cardRanks.put('K', 13);
        cardRanks.put('Q', 12);
        cardRanks.put('T', 11);
        cardRanks.put('9', 10);
        cardRanks.put('8', 9);
        cardRanks.put('7', 8);
        cardRanks.put('6', 7);
        cardRanks.put('5', 6);
        cardRanks.put('4', 5);
        cardRanks.put('3', 4);
        cardRanks.put('2', 3);
        cardRanks.put('J', 2);

        return cardRanks;
    }

    static class Pair<T, N> {
        T key;
        N value;

        public Pair(T key, N value) {
            this.key = key;
            this.value = value;
        }
    }

//    // Works but not pretty. Describes each situation.
//    private int getHandScorePart2Old(String hand) {
//        int maxCount = 0;
//        int nonJokerMaxCount = 0;
//        int jokerCount = 0;
//        Map<Character, Integer> counts = new HashMap<>();
//
//        for (int i = 0; i < hand.length(); i++) {
//            char card = hand.charAt(i);
//
//            counts.put(card, counts.getOrDefault(card, 0) + 1);
//
//            if (card == 'J') {
//                jokerCount++;
//            } else {
//                nonJokerMaxCount = Math.max(nonJokerMaxCount, counts.get(card));
//            }
//            maxCount = Math.max(maxCount, counts.get(card));
//        }
//
//        // Handle joker.
//        if (jokerCount == 5) {
//            return 7;   // 5 of a kind.
//        } else if (jokerCount == 4) {
//            return 7;   // 5 of a kind.
//        } else if (jokerCount == 3) {
//            // 3 and 2
//            // 3 and 1 and 1
//            if (nonJokerMaxCount == 2) {
//                return 7;   // 5 of a kind.
//            } else {
//                return 6;   // 4 of a kind.
//            }
//        } else if (jokerCount == 2) {
//            // 2 and 3
//            // 2 and 2 and 1
//            // 2 and 1 and 1 and 1
//            if (nonJokerMaxCount == 3) {
//                return 7;   // 5 of a kind.
//            } else if (nonJokerMaxCount == 2) {
//                return 6;   // 4 of a kind.
//            } else {
//                return 4;   // 3 of a kind.
//            }
//        } else if (jokerCount == 1) {
//            // 1 and 4.
//            // 1 and 3 and 1.
//            // 1 and 2 and 1 and 1.
//            // 1 and 1 and 1 and 1 and 1.
//            if (nonJokerMaxCount == 4) {
//                return 7;   // 5 of a kind.
//            } else if (nonJokerMaxCount == 3) {
//                return 6;   // 4 of a kind.
//            } else if (nonJokerMaxCount == 2) {
//                if (counts.size() == 3) {
//                    return 5;   // Full house 2323J.
//                }
//                return 4;   // 3 of a kind.
//            } else {
//                return 2;   // 2 of a kind.
//            }
//        }
//
//        // Handle non-joker.
//        // Five of a kind.
//        if (counts.size() == 1) {
//            return 7;
//        }
//        // Four of a kind or full house.
//        if (counts.size() == 2) {
//            return maxCount == 4 ? 6 : 5;
//        }
//        // Three of a kind or two pair.
//        if (counts.size() == 3) {
//            return maxCount == 3 ? 4 : 3;
//        }
//        // Two or one of a kind.
//        return counts.size() == 4 ? 2 : 1;
//    }
}