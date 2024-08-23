package com.olsson.aoc2023

import kotlin.math.round

class Day10 {

    fun part1() = part1kt("day10.txt")
    fun part2() = part2kt("day10.txt")

    fun part1kt(file: String): Double {
        val pipe2DMap = Pipe2DMap.from(InputUtils().getLines(file))
        val start = findStartConnector(pipe2DMap)
        var current = start
        var count = 1 // Since we go to the first connector from the start
        while (pipe2DMap.get(current.first) != 'S') {
            current = nextPipe(current, pipe2DMap)
            count += 1
        }
        
        return round(count.toDouble() / 2)
    }

    /**
     * Was unable to solve Part2 without looking for other solutions
     * Tried to BSF search from all edges but since pipes can be next to each other but still allow passage, this would not work
     * Looked into walking the flow and marking/filling next to the pipes but proved cumbersome to know if
     * you were looking outside/inside based on what direction that we moved along the pipes
     *
     * Instead, I used Ray-Casting Algorithm as it seemed to work nicely based on others discussions
     * Shooting a ray from our point towards the edge and counting the intersections of the loop will result in
     * even number of intersections -> outside the polygon
     * odd number of intersections -> inside the polygon
     * We shoot diagonally to avoid collinear casts where two lines intersect (since pipes are only flowing horizontally or vertically)
     * Only edge case is to avoid counting '7' and 'L' as intersection, as we always shoot NW in the plane and these are not actual intersections, just close calls
     */

    fun part2kt(file: String): Int {
        val pipe2DMap = Pipe2DMap.from(InputUtils().getLines(file))
        val start = findStartConnector(pipe2DMap)
        var current = start
        while (pipe2DMap.get(current.first) != 'S') {
            pipe2DMap.loop.add(current.first)
            current = nextPipe(current, pipe2DMap)
        }
        pipe2DMap.loop.add(current.first) // Add start

        return rayCastAll(pipe2DMap).count()
    }

    // Iterate all points not in the loop and check if they are inside
    private fun rayCastAll(pipe2DMap: Pipe2DMap): Set<Point> {
        val inside = mutableSetOf<Point>()
        for(i in 0..<pipe2DMap.map.size) {
            for(j in 0 ..< pipe2DMap.map.first().size) {
                val p = Point(j, i)
                if (pipe2DMap.loop.contains(p)) {
                    continue
                }
                if (rayCastInsideCheck(p, pipe2DMap)) {
                    inside.add(p)
                }
            }
        }
        return inside
    }

    // Check if point is inside of polygon by casting a ray diagonally towards NE edge and counting intersections
    // We do not count 7 and L since we 'miss' these corners when shooting NE
    private fun rayCastInsideCheck(p: Point, map: Pipe2DMap): Boolean {
        if (map.loop.contains(p)) {
            return false
        }
        var current = p.move(-1, -1)
        var intersections = 0
        while (map.exists(current)) {
            if (map.loop.contains(current) && map.get(current) != '7' && map.get(current) != 'L') {
                intersections += 1
            }
            current = current.move(-1, -1)
        }
        return intersections % 2 != 0
    }

    private fun findStartConnector(map: Pipe2DMap): Pair<Point, Direction> {
        val startPoint = map.map.asSequence()
            .mapIndexed { idx, row -> idx to row }
            .filter { idxAndRow -> idxAndRow.second.contains('S') }
            .map { rowWithStart -> rowWithStart.first to rowWithStart.second.indexOf('S') }
            .map { Point(it.second, it.first) }
            .first()
        val possibleStarts = listOf(
            startPoint.move(y = -1) to Direction.N,
            startPoint.move(x = 1) to Direction.E,
            startPoint.move(y = 1) to Direction.S,
            startPoint.move(x = -1) to Direction.W
        )
        return possibleStarts
            .filter { map.exists(it.first) }
            .find { Pipe.connectedToStart(map.get(it.first), it.second) }
            ?: throw IllegalArgumentException("Unable to find a connector from startnode")
    }

    private fun nextPipe(current: Pair<Point, Direction>, pipe2DMap: Pipe2DMap): Pair<Point, Direction> {
        val pipe = Pipe.fromPipe(pipe2DMap.get(current.first))
        return pipe.follow(current.first, current.second)
    }

    private enum class Pipe(val pipe: Char, val end: Direction, val otherEnd: Direction) {
        PIPE('|', Direction.N, Direction.S),
        BAR('-', Direction.E, Direction.W),
        NE('L', Direction.N, Direction.E),
        NW('J', Direction.N, Direction.W),
        SW('7', Direction.S, Direction.W),
        SE('F', Direction.S, Direction.E),
        START('S', Direction.N, Direction.N),
        NONE('S', Direction.N, Direction.N);

        // Move to the next pipe based on where we entered the current one
        fun follow(point: Point, source: Direction): Pair<Point, Direction> {
            return when (val flowDirection = connectionFlow(source)) {
                Direction.N -> point.move(y = -1) to flowDirection
                Direction.S -> point.move(y = 1) to flowDirection
                Direction.E -> point.move(x = 1) to flowDirection
                Direction.W -> point.move(x = -1) to flowDirection
                else -> throw IllegalArgumentException("Connection failed: flow returned Fault")
            }
        }

        // Decide where we flow based on the input
        // For example if we flow N, then we are connecting in the south of the next pipe and exit through its second direction
        private fun connectionFlow(input: Direction): Direction {
            return when {
                input == Direction.N && this.end == Direction.S -> this.otherEnd
                input == Direction.N && this.otherEnd == Direction.S -> this.end
                input == Direction.S && this.end == Direction.N -> this.otherEnd
                input == Direction.S && this.otherEnd == Direction.N -> this.end
                input == Direction.E && this.end == Direction.W -> this.otherEnd
                input == Direction.E && this.otherEnd == Direction.W -> this.end
                input == Direction.W && this.end == Direction.E -> this.otherEnd
                input == Direction.W && this.otherEnd == Direction.E -> this.end
                else -> Direction.FAULT
            }
        }

        companion object {
            fun fromPipe(pipe: Char): Pipe =
                Pipe.entries.find { it.pipe == pipe } ?: NONE

            fun connectedToStart(pipe: Char, dir: Direction): Boolean {
                val actual = Pipe.fromPipe(pipe)
                if (actual == NONE || pipe == 'S') {
                    return false
                }
                return actual.connectionFlow(dir) != Direction.FAULT
            }
        }
    }

    private enum class Direction {
        N, S, E, W, FAULT;

    }

    private data class Pipe2DMap(val map: List<CharArray>) {
        fun get(point: Point): Char = map[point.y][point.x]

        val loop = mutableSetOf<Point>()

        companion object {
            fun from(input: List<String>): Pipe2DMap {
                return Pipe2DMap(input.map { it.toCharArray() })
            }
        }

        fun exists(p: Point): Boolean {
            return p.x < map.first().size && p.x >= 0 && p.y < map.size && p.y >= 0
        }
    }
}