package com.olsson.aoc2023

class Day06 {

    fun part1() = part1kt("day06.txt")
    fun part2() = part2kt(60_947_882, 475_213_810_151_650)

    fun part1kt(fileName: String): Long {
        // Times on first row, distance record on second
        val lines = InputUtils().getLines(fileName).map { line ->
            line.replace(Regex(" +"), " ").split(" ")
        }.toList()
        return IntRange(1, lines[0].size - 1).map { round ->
            scanValidTimes(lines[0][round].toLong(), lines[1][round].toLong())
        }.reduce(Long::times)
    }

    fun part2kt(time: Long, record: Long): Long {
        return scanValidTimes(time, record)
    }

    // Initially went through all elements but would take way to long for part 2
    // Instead we need to find the min and max of the valid time range
    // No need to check values between since those are all valid times
    private fun scanValidTimes(time: Long, record: Long): Long {
        val startAt = (1..time).first { held -> held * (time - held) > record } - 1 // Exclude the element that actually beats record
        val endAt = (time downTo 1).first { held -> held * (time - held) > record   }
        return endAt - startAt
    }
}