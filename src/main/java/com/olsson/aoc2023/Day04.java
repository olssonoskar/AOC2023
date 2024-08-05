package com.olsson.aoc2023;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day04 {

    private final HashMap<Integer, Integer> scratchCards = new HashMap<>();

    public String part1() {
        return part1("day04.txt").toString();
    }

    public String part2() {
        return part2("day04.txt").toString();
    }

    public Integer part1(String fileName) {
        var input = new InputUtils().getLines(fileName);
        var cards = format(input);
        return cards.stream()
                .map(ScratchCard::matches)
                .map(score -> (int) Math.pow(2, score - 1))
                .reduce(Integer::sum)
                .orElse(0);
    }

    public Integer part2(String fileName) {
        var input = new InputUtils().getLines(fileName);
        var cards = format(input);
        cards.forEach(card -> {
            var currentCardCopies = scratchCards.getOrDefault(card.card, 1);
            // Add held copies for current card to the count as each has the same matches
            card.cardWinnings().forEach(wonCard -> {
                scratchCards.put(wonCard, scratchCards.getOrDefault(wonCard, 1) + currentCardCopies);
            });
        });
        return IntStream.range(1, input.size() + 1)
                .map(index -> scratchCards.getOrDefault(index, 1))
                .reduce(Integer::sum).orElse(-1);
    }

    private List<ScratchCard> format(List<String> input) {
        return input.stream().map(line -> {
            var parts = line.split("[:|]");
            return new ScratchCard(cardNumber(parts[0]), formatNumbers(parts[1]), formatNumbers(parts[2]));
        }).toList();
    }

    private int cardNumber(String card) {
        return Integer.parseInt(card.split(" +")[1]);
    }

    private Set<Integer> formatNumbers(String numbers) {
        return Arrays.stream(numbers.trim().split(" "))
                .filter(num -> !num.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private record ScratchCard(
            int card,
            Set<Integer> numbers,
            Set<Integer> winners) {
        long matches() {
            return numbers.stream()
                    .filter(winners::contains)
                    .count();
        }

        IntStream cardWinnings() {
            var matches = matches();
            return IntStream.range(card + 1, card + (int) matches + 1);
        }
    }
}
