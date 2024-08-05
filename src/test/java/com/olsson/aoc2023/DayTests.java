package com.olsson.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DayTests {

    @Test
    void Day1() {
        assertEquals("142", new Day01().part1kt("day1-1.txt"));
        assertEquals("281", new Day01().part2kt("day1-2.txt"));
    }

    @Test
    void Day2() {
        assertEquals("8", new Day02().part1kt("day2.txt"));
        assertEquals("2286", new Day02().part2kt("day2.txt"));
    }

    @Test
    void Day3() {
        assertEquals("4361", new Day03().part1kt("day3.txt"));
        assertEquals("467835", new Day03().part2kt("day3.txt"));
    }

    @Test
    void Day4() {
        assertEquals(13, new Day04().part1("day4.txt"));
        assertEquals(30, new Day04().part2("day4.txt"));
    }

    @Test
    void Day5() {
        assertEquals(35L, new Day05().part1("day5.txt"));
        assertEquals(46L, new Day05().part2("day5.txt"));
    }

}
