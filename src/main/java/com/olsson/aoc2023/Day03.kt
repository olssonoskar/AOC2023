package com.olsson.aoc2023

class Day03 {

    private lateinit var initalInput: List<String>
    private val partNumbers = mutableListOf<PartNumber>()
    private val gearPositions = mutableSetOf<Point>()

    fun part1() = part1kt()
    fun part2() = part2kt()

    fun part1kt(input: String = "day03.txt"): String {
        initalInput = InputUtils().getLines(input)
        parse()
        return partNumbers.sumOf { it.actual() }.toString()
    }

    fun part2kt(input: String = "day03.txt"): String {
        initalInput = InputUtils().getLines(input)
        parse()
        val result = gearPositions.sumOf { gear ->
            val adjacent = partNumbers.filter { it.neighbors.contains(gear) }
            if (adjacent.size == 2)
                adjacent.first().actual() * adjacent.last().actual()
            else 0
        }
        return result.toString()
    }

    private fun parse() {
        initalInput.forEachIndexed {y, row ->
            var partNumber = PartNumber()
            row.forEachIndexed{x, someChar ->
                // While we work on a number, check neighbors at index and append digit
                if (someChar.isDigit()) {
                    if(hasSymbolNeighbor(Point(x, y))) {
                        partNumber.valid = true
                    }
                    partNumber.add(someChar, Point(x, y))
                }
                // If current is not a digit, but we have a valid number, add it and reset
                else if (!partNumber.isEmpty()) {
                    if (partNumber.valid) {
                        partNumbers.add(partNumber)
                    }
                    partNumber = PartNumber()
                }
                // If current is a gear, save position for part 2
                if (someChar == '*') {
                    gearPositions.add(Point(x, y))
                }
            }
            // Add current if valid at the end of row
            if (!partNumber.isEmpty() && partNumber.valid) {
                partNumbers.add(partNumber)
            }
        }
    }

    private fun hasSymbolNeighbor(point: Point) =
        point.neighbors()
            .map { getFromInput(it) }
            .any { it != '.' && !it.isDigit() }

    // Check input for position, return dot if out of bounds
    private fun getFromInput(point: Point): Char {
        return try {
            initalInput[point.y][point.x]
        } catch (ex: IndexOutOfBoundsException) {
            '.'
        }
    }

    class PartNumber {
        val numbers = mutableListOf<Char>()
        val neighbors = mutableSetOf<Point>()
        var valid = false

        fun add(number: Char, point: Point) {
            numbers.add(number)
            neighbors.addAll(point.neighbors())
        }

        fun isEmpty() = numbers.isEmpty()
        fun actual() = numbers.joinToString("").toInt()
    }

}

fun main() {
    Day03().part1()
    Day03().part2()
}