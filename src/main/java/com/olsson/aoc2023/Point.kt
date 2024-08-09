package com.olsson.aoc2023

data class Point(val x: Int, val y: Int) {

    fun neighbors(): Set<Point> =
        setOf(
            Point(x - 1, y - 1),
            Point(x, y - 1),
            Point(x + 1, y - 1),
            Point(x - 1, y),
            Point(x, y),
            Point(x + 1, y),
            Point(x - 1, y + 1),
            Point(x, y + 1),
            Point(x + 1, y + 1),
        )

    fun move(x: Int = 0, y: Int = 0): Point {
        return Point(this.x + x, this.y + y)
    }
}