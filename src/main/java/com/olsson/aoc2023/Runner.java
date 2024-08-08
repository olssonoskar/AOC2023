package com.olsson.aoc2023;

import java.util.List;
import java.util.Scanner;

public class Runner {

    public void run() {
        var running = true;
        while(running) {
            System.out.println("Enter day to run");
            var scanner = new Scanner(System.in);
            var result = switch (scanner.next()) {
                case "1" -> List.of(new Day01().part1(), new Day01().part2());
                case "2" -> List.of(new Day02().part1(), new Day02().part2());
                case "3" -> List.of(new Day03().part1(), new Day03().part2());
                case "4" -> List.of(new Day04().part1(), new Day04().part2());
                case "5" -> List.of(new Day05().part1(), new Day05().part2());
                case "6" -> List.of(new Day06().part1(), new Day06().part2());
                case "7" -> List.of(new Day07().part1(), new Day07().part2());
                case "8" -> List.of(new Day08().part1(), new Day08().part2());
                case "stop", "exit" -> {
                    running = false;
                    yield "Stopping";
                }
                default -> List.of("No result");
            };
            System.out.println(result);
        }
    }


    public static void main(String[] args) {
        new Runner().run();
    }
}

