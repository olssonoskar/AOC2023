package com.olsson.aoc2023;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day08 {

    public Long part1() {
        return part1("day08.txt");
    }
    public Long part2() {
        return part2("day08.txt");
    }

    public Long part1(String file) {
        var input = new InputUtils().getLines(file);
        var pattern = input.getFirst();
        var nodes = collectNodes(input);
        return walkNodes(pattern, "AAA", nodes, this::isNotGoal);
    }

    /**
     * Had to look up the 'answer' as I didn't quite get the reasoning behind this at first, but the point is to 're-walk' shorter
     * paths until they all end simultaneously, so this means the paths are actually 'looping' (as you just start over from the start)
     * If we find the length of each path and find the least common multiple between all the paths, that is the answer
     * since this will be the first time they all end up on the goal simultaneously
     */
    public Long part2(String file) {
        var input = new InputUtils().getLines(file);
        var pattern = input.getFirst();
        var nodes = collectNodes(input);
        var nodesToTread = nodes.keySet().stream().filter(it -> it.charAt(it.length() -1) == 'A').toList();
        return nodesToTread.stream()
                .map(it -> walkNodes(pattern, it, nodes, this::isNotGoalPart2))
                .reduce(this::lcm)
                .orElse(-1L);
    }

    private long walkNodes(String pattern, String node, Map<String, Node> nodeMap, GoalCheck notReached) {
        int steps = 0;
        String current = node;
        while (notReached.evaluate(current)) {
            var direction = pattern.charAt(steps % pattern.length());
            current = step(current, direction, nodeMap);
            steps++;
        }
        return steps;
    }

    private String step(String current, char direction, Map<String, Node> nodeMap) {
        var node = nodeMap.get(current);
        return switch (direction) {
            case 'R' -> node.right;
            case 'L' -> node.left;
            default -> throw new IllegalArgumentException("Expected L/R, got " + direction);
        };
    }

    private boolean isNotGoal(String node) {
        return !node.equals("ZZZ");
    }

    private boolean isNotGoalPart2(String node) {
        return node.charAt(node.length() - 1) != 'Z';
    }

    private record Node(String current, String left, String right) {
    }

    @FunctionalInterface
    private interface GoalCheck {
        boolean evaluate(String nodes);
    }

    // Least common multiple
    private long lcm(long number, long otherNumber) {
        return (number * otherNumber) / gcd(number, otherNumber);
    }

    // Euclid's alg for Greatest common divisor
    private long gcd(long number, long otherNumber) {
        if (otherNumber == 0) {
            return number;
        }
        return gcd(otherNumber, number % otherNumber);
    }

    private Map<String, Node> collectNodes(List<String> input) {
        return input.stream()
                .skip(2)
                .map(line -> line.split(" "))
                .map(parts -> new Node(parts[0], parts[2].substring(1, 4), parts[3].substring(0, 3)))
                .collect(Collectors.toMap(node -> node.current, node -> node));
    }
}
