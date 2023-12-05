import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day5 {

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("inputs/day5Input.txt");

        Day5 day5 = new Day5();

        System.out.println(day5.processMapsPart2(input));
    }

    // Process the maps of destinationRangeStart, sourceRangeStart, rangeSize
    // For part 2, the seeds are now also given in ranges. Search for the min location for all seeds across all ranges.
    private long processMapsPart2(File input) throws FileNotFoundException {
        long minLocation = Long.MAX_VALUE;
        Scanner scanner = new Scanner(input);

        scanner.nextLine();
        scanner.nextLine();

        List<long[][]> maps = getMaps(scanner);
        scanner.close();

        scanner = new Scanner(input);
        String line = scanner.nextLine();
        String seedString = line.substring(line.indexOf(" ") + 1);
        String[] seedTokens = seedString.split(" ");

        for (int i = 0; i < seedTokens.length; i += 2) {
            long startSeed = Long.parseLong(seedTokens[i]);
            long endSeed = startSeed + Long.parseLong(seedTokens[i + 1]) - 1;

            for (long j = startSeed; j <= endSeed; j++) {
                minLocation = Math.min(minLocation, findLocationPart2(maps, j));
            }
        }
        scanner.close();

        return minLocation;
    }

    private long findLocationPart2(List<long[][]> maps, long seed) {
        long soilNum = binarySearch(maps.get(0), seed);
        long fertilizerNum = binarySearch(maps.get(1), soilNum);
        long waterNum = binarySearch(maps.get(2), fertilizerNum);
        long lightNum = binarySearch(maps.get(3), waterNum);
        long tempNum = binarySearch(maps.get(4), lightNum);
        long humidityNum = binarySearch(maps.get(5), tempNum);

        return binarySearch(maps.get(6), humidityNum);
    }

    // Process the maps of destinationRangeStart, sourceRangeStart, rangeSize
    // Find the location of each individual seed from the input and return the minimum location.
    private long processMapsPart1(File input) throws FileNotFoundException {
        List<Long> seedList = new ArrayList<>();
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith("seeds")) {
                String seedString = line.substring(line.indexOf(" ") + 1);
                String[] seedTokens = seedString.split(" ");

                Arrays.stream(seedTokens).forEach(seedToken -> seedList.add(Long.parseLong(seedToken)));
                break;
            }
        }
        List<long[][]> maps = getMaps(scanner);
        scanner.close();

        return findMinLocation(seedList, maps);
    }

    private long findMinLocation(List<Long> seedList, List<long[][]> maps) {
        long minLocation = Long.MAX_VALUE;
        List<Long> soilNums = new ArrayList<>();
        List<Long> fertilizerNums = new ArrayList<>();
        List<Long> waterNums = new ArrayList<>();
        List<Long> lightNums = new ArrayList<>();
        List<Long> tempNums = new ArrayList<>();
        List<Long> humitityNums = new ArrayList<>();

        seedList.forEach(seed -> soilNums.add(binarySearch(maps.get(0), seed)));
        soilNums.forEach(soil -> fertilizerNums.add(binarySearch(maps.get(1), soil)));
        fertilizerNums.forEach(fertilizer -> waterNums.add(binarySearch(maps.get(2), fertilizer)));
        waterNums.forEach(water -> lightNums.add(binarySearch(maps.get(3), water)));
        lightNums.forEach(light -> tempNums.add(binarySearch(maps.get(4), light)));
        tempNums.forEach(temp -> humitityNums.add(binarySearch(maps.get(5), temp)));

        for (long humidityNum : humitityNums) {
            minLocation = Math.min(minLocation, binarySearch(maps.get(6), humidityNum));
        }
        return minLocation;
    }

    private long binarySearch(long[][] map, long target) {
        int low = 0;
        int high = map.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            long[] range = map[mid];
            long lowerBound = range[0];
            long upperBound = range[1];

            if (lowerBound <= target && target <= upperBound) {
                long resultLowerBound = range[2];
                long diff = target - lowerBound;

                return resultLowerBound + diff;
            } else if (target < lowerBound) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return target;
    }

    private List<long[][]> getMaps(Scanner scanner) {
        List<String> seedSoilLines = new ArrayList<>();
        List<String> soilFertilizerLines = new ArrayList<>();
        List<String> fertilizerWaterLines = new ArrayList<>();
        List<String> waterLightLines = new ArrayList<>();
        List<String> lightTempLines = new ArrayList<>();
        List<String> tempHumidityLines = new ArrayList<>();
        List<String> humidityLocationLines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            switch (line) {
                case "seed-to-soil map:" -> {
                    while (scanner.hasNextLine()) {
                        String mapLine = scanner.nextLine();

                        if (mapLine.isEmpty()) {
                            break;
                        }
                        seedSoilLines.add(mapLine);
                    }
                }
                case "soil-to-fertilizer map:" -> {
                    while (scanner.hasNextLine()) {
                        String mapLine = scanner.nextLine();

                        if (mapLine.isEmpty()) {
                            break;
                        }
                        soilFertilizerLines.add(mapLine);
                    }
                }
                case "fertilizer-to-water map:" -> {
                    while (scanner.hasNextLine()) {
                        String mapLine = scanner.nextLine();

                        if (mapLine.isEmpty()) {
                            break;
                        }
                        fertilizerWaterLines.add(mapLine);
                    }
                }
                case "water-to-light map:" -> {
                    while (scanner.hasNextLine()) {
                        String mapLine = scanner.nextLine();

                        if (mapLine.isEmpty()) {
                            break;
                        }
                        waterLightLines.add(mapLine);
                    }
                }
                case "light-to-temperature map:" -> {
                    while (scanner.hasNextLine()) {
                        String mapLine = scanner.nextLine();

                        if (mapLine.isEmpty()) {
                            break;
                        }
                        lightTempLines.add(mapLine);
                    }
                }
                case "temperature-to-humidity map:" -> {
                    while (scanner.hasNextLine()) {
                        String mapLine = scanner.nextLine();

                        if (mapLine.isEmpty()) {
                            break;
                        }
                        tempHumidityLines.add(mapLine);
                    }
                }
                case "humidity-to-location map:" -> {
                    while (scanner.hasNextLine()) {
                        String mapLine = scanner.nextLine();

                        if (mapLine.isEmpty()) {
                            break;
                        }
                        humidityLocationLines.add(mapLine);
                    }
                }
            }
        }

        List<long[][]> rangeList = new ArrayList<>();
        rangeList.add(getRanges(seedSoilLines));
        rangeList.add(getRanges(soilFertilizerLines));
        rangeList.add(getRanges(fertilizerWaterLines));
        rangeList.add(getRanges(waterLightLines));
        rangeList.add(getRanges(lightTempLines));
        rangeList.add(getRanges(tempHumidityLines));
        rangeList.add(getRanges(humidityLocationLines));

        return rangeList;
    }

    private long[][] getRanges(List<String> mapLines) {
        long[][] ranges = new long[mapLines.size()][4];

        for (int i = 0; i < mapLines.size(); i++) {
            String[] tokens = mapLines.get(i).split(" ");

            ranges[i] = new long[]
                    {Long.parseLong(tokens[1]), Long.parseLong(tokens[1]) + Long.parseLong(tokens[2]) - 1,
                            Long.parseLong(tokens[0]), Long.parseLong(tokens[0]) + Long.parseLong(tokens[2]) - 1};
        }

        Arrays.sort(ranges, Comparator.comparingLong(x -> x[0]));

        return ranges;
    }
}