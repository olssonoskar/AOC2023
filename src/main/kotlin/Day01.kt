class Day01 {

    fun part1(): String {
        val lines = InputUtils().getLines("day01.txt")
        val sum = lines.sumOf { convertNumber(it) }
        return sum.toString()
    }
    fun convertNumber(line: String): Int {
        val num = takeFirstDigit(line) + takeFirstDigit(line.reversed())
        return num.toInt()
    }

    private fun takeFirstDigit(it: String): String {
        return it.dropWhile { !it.isDigit() }.take(1)
    }

    private val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun part2(): String {
        val lines = InputUtils().getLines("day01.txt")
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

fun main() {
    Day01().part1()
    Day01().part2()
}