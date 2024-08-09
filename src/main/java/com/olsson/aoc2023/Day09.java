package com.olsson.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day09 {

    public int part1() {
        return part1("day09.txt");
    }

    public int part2() {
        return part2("day09.txt");
    }

    public int part1(String file) {
        var input = new InputUtils().getLines(file);
        return input.stream()
                .map(this::parseGeneration)
                .map(gen -> runGeneration(gen, List::getLast))
                .map(gen -> gen.stream().reduce(Integer::sum).orElse(0))
                .reduce(Integer::sum).orElse(-1);
    }

    public int part2(String file) {
        var input = new InputUtils().getLines(file);
        return input.stream()
                .map(this::parseGeneration)
                .map(gen -> runGeneration(gen, List::getFirst))
                .map(this::extrapolateBack)
                .reduce(Integer::sum).orElse(-1);
    }

    private List<Integer> parseGeneration(String in) {
        return Arrays.stream(in.split(" "))
                .map(Integer::parseInt)
                .toList();
    }

    private List<Integer> runGeneration(List<Integer> input, Selector selector) {
        List<Integer> currentGen = input;
        var selectedFromGen = new LinkedList<Integer>();
        while (!currentGen.stream().allMatch(num -> num == 0)) {
            selectedFromGen.add(selector.select(currentGen));
            currentGen = generate(currentGen);
        }
        return selectedFromGen;
    }

    private List<Integer> generate(List<Integer> current) {
        var next = new ArrayList<Integer>();
        for (int i = 1; i < current.size(); i++) {
            next.add(current.get(i) - current.get(i - 1));
        }
        return next;
    }

    // Subtract previous extrapolated value from current to get what the current extrapolated value should be
    private int extrapolateBack(List<Integer> elements) {
        var diff = new LinkedList<Integer>();
        var previous = 0;
        for (int i = elements.size() - 1; i >= 0; i--) {
            previous = elements.get(i) - previous;
            diff.add(previous);
        }
        return diff.getLast();
    }

    @FunctionalInterface
    private interface Selector {
        int select(List<Integer> a);
    }

}
