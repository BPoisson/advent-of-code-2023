import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day8 {

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("inputs/day8Input.txt");

        Day8 day8 = new Day8();

        System.out.println(day8.processInputPart1(input));
    }

    private long processInputPart1(File input) throws FileNotFoundException {
        Scanner scanner = new Scanner(input);

        String instructionsLine = scanner.nextLine();
        List<Character> instructions = getInstructions(instructionsLine);
        Map<String, String[]> nodeMap = new HashMap<>();
        scanner.nextLine(); // Skip blank line.

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String node = line.substring(0, line.indexOf(" "));
            String left = line.substring(line.indexOf("(") + 1, line.indexOf(","));
            String right = line.substring(line.indexOf(",") + 2, line.indexOf(")"));

            nodeMap.put(node, new String[] {left, right});
        }
        scanner.close();

        return countStepsPart2(instructions, nodeMap);
    }

    // Count the number of steps until all nodes ending with A finish by ending at Z, if they all move together.
    private long countStepsPart2(List<Character> instructions, Map<String, String[]> nodeMap) {
        long steps = 0;
        int instructionIndex = 0;
        List<String> nodes = getStartNodesPart2(nodeMap.keySet());
        List<String> originalNodes = new ArrayList<>(nodes);
        Map<String, Long> stepsToFinished = new HashMap<>();

        while (stepsToFinished.size() != nodes.size()) {
            char instruction = instructions.get(instructionIndex);

            for (int i = 0; i < nodes.size(); i++) {
                String currNode = nodes.get(i);

                if (currNode.charAt(2) == 'Z') {
                    if (!stepsToFinished.containsKey(originalNodes.get(i))) {
                        stepsToFinished.put(originalNodes.get(i), steps);
                    }
                    continue;
                }

                String nextNode;
                if (instruction == 'L') {
                    nextNode = nodeMap.get(currNode)[0];
                } else {
                    nextNode = nodeMap.get(currNode)[1];
                }
                nodes.set(i, nextNode);
            }
            steps++;
            instructionIndex = instructionIndex == instructions.size() - 1 ? 0 : instructionIndex + 1;
        }
        return calculateFinish(new ArrayList<>(stepsToFinished.values().stream().toList()));
    }

    // Since the nodes repeat in cycles of the same number of steps, the result will be the least common multiple
    // of the number of steps for each node to end in Z by itself.
    // A more generalized approach would be a brute-force approach, or Chinese remainder theorem.
    private long calculateFinish(List<Long> steps) {
        long leastCommonMultiple = steps.get(0);

        for (int i = 1; i < steps.size(); i++) {
            long num1 = leastCommonMultiple;
            long num2 = steps.get(i);
            long greatestCommonDivisor = leastCommonMultiple(num1, num2);

            leastCommonMultiple = (leastCommonMultiple * steps.get(i)) / greatestCommonDivisor;
        }
        return leastCommonMultiple;
    }

    public static long leastCommonMultiple(long a, long b) {
        if (b == 0) {
            return a;
        }
        return leastCommonMultiple(b, a % b);
    }

    private List<String> getStartNodesPart2(Set<String> nodeSet) {
        List<String> nodes = new ArrayList<>(nodeSet.size());

        for (String node : nodeSet) {
            if (node.charAt(2) == 'A') {
                nodes.add(node);
            }
        }
        return nodes;
    }

    // Count the number of steps to go from AAA to ZZZ.
    private long countStepsPart1(List<Character> instructions, Map<String, String[]> nodeMap) {
        int steps = 0;
        int instructionIndex = 0;
        String currNode = "AAA";

        while (!currNode.equals("ZZZ")) {
            char instruction = instructions.get(instructionIndex);

            if (instruction == 'L') {
                currNode = nodeMap.get(currNode)[0];
            } else {
                currNode = nodeMap.get(currNode)[1];
            }
            steps++;
            instructionIndex = instructionIndex == instructions.size() - 1 ? 0 : instructionIndex + 1;
        }
        return steps;
    }

    private List<Character> getInstructions(String instructionsLine) {
        List<Character> instructions = new ArrayList<>();

        for (int i = 0; i < instructionsLine.length(); i++) {
            instructions.add(instructionsLine.charAt(i));
        }
        return instructions;
    }
}