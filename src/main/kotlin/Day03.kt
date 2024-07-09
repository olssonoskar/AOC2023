class Day03 {

    private val input = InputUtils.getLines("day03.txt")
    private val partNumbers = mutableListOf<PartNumber>()
    private val gearPositions = mutableSetOf<Point>()

    fun part1() {
        parse()
        println(partNumbers.sumOf { it.actual() })
    }

    fun part2() {
        parse()
        val result = gearPositions.sumOf { gear ->
            val adjacent = partNumbers.filter { it.neighbors.contains(gear) }
            if (adjacent.size == 2)
                adjacent.first().actual() * adjacent.last().actual()
            else 0
        }
        println(result)
    }

    private fun parse() {
        input.forEachIndexed {y, row ->
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
            input[point.y][point.x]
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