package com.olsson.aoc2023;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Day11 {

    private final static char GALAXY = '#';

    public long part1() {
        return part1("day11.txt");
    }

    public long part2() {
        return part2("day11.txt", 1_000_000);
    }

    public long part1(String fileName) {
        var input = new InputUtils().getLines(fileName);
        setup(input);
        return countPaths(2);
    }

    public long part2(String fileName, int expansionSize) {
        var input = new InputUtils().getLines(fileName);
        setup(input);
        return countPaths(expansionSize);
    }

    private long countPaths(int expansionSize) {
        var examined = new HashSet<Point>();
        long steps = 0;
        for (Point p : galaxies) {
            var totalDistanceToAll = galaxies.stream()
                    .filter(galaxy -> !(examined.contains(galaxy) || p == galaxy))
                    .map(galaxy -> distanceBetween(p, galaxy, expansionSize))
                    .reduce(Long::sum)
                    .orElse(0L);
            steps += totalDistanceToAll;
            examined.add(p);
        }
        return steps;
    }

    private long distanceBetween(Point a, Point b, int expansionSize) {
        var rangeRow = getRangeX(a, b);
        var rangeCol = getRangeY(a, b);

        var stepsX = rangeRow.map(x -> {
            if (expansions.contains(new Point(x, 0).colName())) {
                return expansionSize;
            }
            return 1;
        }).sum();
        var stepsY = rangeCol.map(y -> {
            if (expansions.contains(new Point(0, y).rowName())) {
                return expansionSize;
            }
            return 1;
        }).sum();
        return stepsX + stepsY;
    }

    private IntStream getRangeX(Point a, Point b) {
        if (a.x < b.x) {
            return IntStream.range(a.x + 1, b.x + 1);
        } else if (a.x > b.x) {
            return IntStream.range(b.x, a.x);
        }
        return IntStream.empty();
    }

    private IntStream getRangeY(Point a, Point b) {
        if (a.y < b.y) {
            return IntStream.range(a.y + 1, b.y + 1);
        } else if (a.y > b.y) {
            return IntStream.range(b.y, a.y);
        }
        return IntStream.empty();
    }

    // Parse the input and mark any rows or columns that do not contain Galaxies (GALAXY)
    private void parseExpansions(List<String> input) {
        for (int row = 0; row < input.size(); row++) {
            if (!input.get(row).contains("#")) {
                expansions.add("Row" + row);
            }
        }
        for (int col = 0; col < input.getFirst().length(); col++) {
            var current = col;
            var hasGalaxy = input.stream()
                    .map(row -> row.charAt(current))
                    .anyMatch(c -> c == GALAXY);
            if(!hasGalaxy) {
                expansions.add("Col" + col);
            }
        }
    }

    // Parse input, mark pos of galaxies and then parse expansions as well
    private void setup(List<String> in) {
        if (!galaxies.isEmpty()) {
            return;
        }
        for (int y = 0; y <= in.size() - 1; y++) {
            for (int x = 0; x <= in.getFirst().length() - 1; x++) {
                if (in.get(y).charAt(x) == GALAXY) {
                    galaxies.add(new Point(x, y));
                }
            }
        }
        parseExpansions(in);
    }

    private final Set<Point> galaxies = new HashSet<>();
    private final Set<String> expansions = new HashSet<>();
    private record Point(int x, int y) {
        String rowName() {
            return "Row" + y;
        }

        String colName() {
            return "Col" + x;
        }
    }
    private record Distance(Point a, Point b, long length) { }

}
