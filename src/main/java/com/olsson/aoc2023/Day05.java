package com.olsson.aoc2023;

import java.util.*;
import java.util.function.Predicate;

public class Day05 {

    public String part1() {
        return part1("day05_test.txt").toString();
    }

    public String part2() {
        return part2("day05.txt").toString();
    }

    // Run through each seed and convert it through all "maps", return min
    public Long part1(String fileName) {
        var input = new InputUtils().getLines(fileName);
        var almanac = parseInput(input);
        return almanac.seeds.stream()
                .map(seed -> convertAll(seed, almanac, order))
                .min(Long::compare)
                .orElse(-1L);
    }

    /** Instead of testing a huge amount of numbers as in part 1,
     * we calculate the result backwards (Possible for part 1 as well?)
     * Take the minimum value and convert it through the "maps" backwards
     * If we find a match in a 'seed range' then we know we have found the min
     **/
    public Long part2(String fileName) {
        var input = new InputUtils().getLines(fileName);
        var almanac = parseInput(input);
        var matched = false;
        long lastResult;
        for(long attempt = 1; attempt <= 30_000_000 && !matched; attempt++) {
            lastResult = convertAllReversed(attempt, almanac, order.reversed());
            if (almanac.hasMatchingSeedRange(lastResult)) {
                return attempt;
            }
        }
        return -1L;
    }

    private SeedAlmanac parseInput(List<String> input) {
        var seeds = Arrays.stream(input.getFirst().split(" "))
                .skip(1)
                .map(Long::parseLong)
                .toList();
        SeedAlmanac almanac = new SeedAlmanac(seeds, new HashMap<>());
        String currentName = "";
        List<Converter> current = new ArrayList<>();
        for (String line : input.subList(2, input.size() - 1)) {
            if (line.contains(":")) {
                currentName = line;
            } else if (line.isBlank()) {
                almanac.converters.put(currentName, current);
                currentName = "";
                current = new ArrayList<>();
            } else {
                var converterValues = Arrays.stream(line.split(" "))
                        .map(Long::parseLong)
                        .toList();
                current.add(new Converter(
                        converterValues.get(0),
                        converterValues.get(1),
                        converterValues.get(2)
                ));
            }
        }
        almanac.converters.put(currentName, current);
        return almanac;
    }

    private Long convertAllReversed(long seed, SeedAlmanac almanac, List<String> order) {
        long modifiedSeed = seed;
        for (String name : order) {
            var match = almanac.converters.getOrDefault(name, Collections.emptyList()).stream()
                    .filter(matchesRangeReverse(modifiedSeed))
                    .findFirst();
            if (match.isPresent()) {
                var actual = match.get();
                modifiedSeed = modifiedSeed - actual.destination + actual.source;
            }
        }
        return modifiedSeed;
    }

    private Long convertAll(long seed, SeedAlmanac almanac, List<String> order) {
        long modifiedSeed = seed;
        for (String name : order) {
            var match = almanac.converters.getOrDefault(name, Collections.emptyList()).stream()
                    .filter(matchesRange(modifiedSeed))
                    .findFirst();
            if (match.isPresent()) {
                var actual = match.get();
                modifiedSeed = modifiedSeed - actual.source + actual.destination;
            }
        }
        return modifiedSeed;
    }

    private record SeedAlmanac(List<Long> seeds, Map<String, List<Converter>> converters) {

        boolean hasMatchingSeedRange(long value) {
            for (int i = 0; i < seeds.size() - 1; i += 2) {
                if (value >= seeds.get(i) && value < (seeds.get(i) + seeds.get(i + 1))) {
                    return true;
                }
            }
            return false;
        }

    }

    private record Converter(long destination, long source, long range) {
    }

    Predicate<Converter> matchesRange(long seed) {
        return conv -> seed >= conv.source && seed < (conv.source + conv.range);
    }

    Predicate<Converter> matchesRangeReverse(long seed) {
        return conv -> seed >= conv.destination && seed < (conv.destination + conv.range);
    }

    List<String> order = List.of(
            "seed-to-soil map:",
            "soil-to-fertilizer map:",
            "fertilizer-to-water map:",
            "water-to-light map:",
            "light-to-temperature map:",
            "temperature-to-humidity map:",
            "humidity-to-location map:"
    );
}
