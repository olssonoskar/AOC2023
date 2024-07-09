class Day02 {

    fun part1() {
        val lines = InputUtils.getLines("day02.txt")
        val limit = Bag(12, 14, 13)
        val sumOfValidGameId =
            lines.filter { maxBag(it.split(":")[1]).valid(limit) }
                .map { it.split(":")[0] }
                .sumOf { it.split(" ")[1].toInt() }
        println(sumOfValidGameId)
    }

    private fun maxBag(game: String): Bag {
        return game.split(";")
            .flatMap { it.split(",") }
            .map { it.trim()}
            .map { fromSet(it) }
            .reduce{first, second -> first.keepMax(second)}
    }

    fun part2() {
        val lines = InputUtils.getLines("day02.txt")
        println(lines.map{ maxBag(it.split(":")[1]) }
            .sumOf { it.powerOf() })
    }

    private fun fromSet(set: String): Bag {
        return when {
            set.contains("red") -> Bag(red = set.split(" ")[0].toInt())
            set.contains("blue") -> Bag(blue = set.split(" ")[0].toInt())
            set.contains("green") -> Bag(green = set.split(" ")[0].toInt())
            else -> throw IllegalArgumentException("Expected blue, green or red")
        }
    }

    data class Bag(val red: Int = 0, val blue: Int = 0, val green: Int = 0) {

        fun keepMax(bag: Bag): Bag {
            return Bag(
                maxOf(this.red, bag.red),
                maxOf(this.blue, bag.blue),
                maxOf(this.green, bag.green)
            )
        }

        fun valid(limit: Bag): Boolean {
            return this.red <= limit.red && this.blue <= limit.blue && this.green <= limit.green
        }

        fun powerOf(): Int {
            return this.red * this.blue * this.green
        }
    }
}

fun main() {
    Day02().part1()
    Day02().part2()
}