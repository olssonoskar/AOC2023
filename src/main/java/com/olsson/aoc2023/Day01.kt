package com.olsson.aoc2023

class Day01 {

    // Convenience as Java cant see Kotlin default args
    fun part1(): String = part1kt()
    fun part2(): String = part2kt()

    fun part1kt(input: String = "day01.txt"): String {
        val lines = InputUtils().getLines(input)
        val sum = lines.sumOf { convertNumber(it) }
        return sum.toString()
    }

    private fun convertNumber(line: String): Int {
        val num = takeFirstDigit(line) + takeFirstDigit(line.reversed())
        return num.toInt()
    }

    private fun takeFirstDigit(it: String): String {
        return it.dropWhile { !it.isDigit() }.take(1)
    }

    private val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun part2kt(input: String = "day01.txt"): String {
        val lines = InputUtils().getLines(input)
        val sum = lines.sumOf { convertIncludingTextNumber(it) }
        return sum.toString()
    }

    private fun convertIncludingTextNumber(input: String): Int {
        val firstText = input.findAnyOf(numbers)
        val lastText = input.findLastAnyOf(numbers)

        val firstNum: String = takeFirstDigit(input)
        val indexFirst = input.indexOfFirst { it.toString() == firstNum }
        val lastNum = takeFirstDigit(input.reversed())
        val indexLast = input.indexOfLast { it.toString() == lastNum }

        // Take the num with first/last index
        val first = if (indexFirst != -1 && indexFirst < (firstText?.first ?: Int.MAX_VALUE))
            firstNum else (numbers.indexOf(firstText?.second ?: "OHNO") + 1).toString()
        val last = if (indexLast > (lastText?.first ?: Int.MIN_VALUE))
            lastNum else (numbers.indexOf(lastText?.second ?: "OHNO") + 1).toString()

        return (first + last).toInt()
    }
}