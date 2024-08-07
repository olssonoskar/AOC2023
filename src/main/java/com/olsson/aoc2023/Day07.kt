package com.olsson.aoc2023

class Day07 {

    fun part1() = part1kt("day07.txt")
    fun part2() = part2kt("day07.txt")

    fun part1kt(file: String): Long {
        val input = InputUtils().getLines(file)
        return input.asSequence()
            .map { it.split(" ") }
            .map { Hand(it[0], it[1].toLong()) }
            .sortedWith(handComparator)
            .mapIndexed { idx, hand -> (idx + 1) * hand.bet }
            .sum()
    }

    fun part2kt(file: String): Long {
        val input = InputUtils().getLines(file)
        return input.asSequence()
            .map { it.split(" ") }
            .map { Hand(it[0], it[1].toLong()) }
            .sortedWith(handComparatorPart2)
            .mapIndexed { idx, hand -> (idx + 1) * hand.bet }
            .sum()
    }

    private fun rankHand(hand: String): CardRank {
        val cards = hand.groupingBy { it }.eachCount().values
        val maxOfAKind = cards.max()
        return when {
            cards.filter { it == 2 }.size == 2 -> CardRank.TWO_PAIR
            cards.contains(2) && cards.contains(3) -> CardRank.HOUSE
            else -> CardRank.from(maxOfAKind)
        }
    }

    /**
     * Determine the hand without Jokers and then upgrade it based on the amount of Jokers
     * if only Jokers, we just return 5Kind
     */
    private fun rankHandWithUpgrade(hand: String): CardRank {
        if (hand == "JJJJJ") {
            return CardRank.FIVE_KIND
        }
        var result = rankHand(hand.replace("J", ""))
        for (times in 0 until hand.count { it == 'J' }) {
            result = CardRank.upgrade(result)
        }
        return result
    }

    enum class Card(val sign: Char) {
        TWO('2'), THREE('3'), FOUR('4'), FIVE('5'),
        SIX('6'), SEVEN('7'), EIGHT('8'), NINE('9'),
        T('T'), J('J'), Q('Q'), K('K'), A('A');

        companion object {
            fun fromChar(c: Char): Card {
                return entries.first { it.sign == c }
            }
        }
    }

    enum class CardRank {
        HIGH_CARD, PAIR, TWO_PAIR, THREE_KIND, HOUSE, FOUR_KIND, FIVE_KIND;

        companion object {
            fun from(maxOfAKind: Int): CardRank = when (maxOfAKind) {
                1 -> HIGH_CARD
                2 -> PAIR
                3 -> THREE_KIND
                4 -> FOUR_KIND
                else -> FIVE_KIND
            }

            // Given a hand and a Joker, this is the best way to upgrade the hand
            fun upgrade(current: CardRank): CardRank =
                when (current) {
                    HIGH_CARD -> PAIR
                    PAIR -> THREE_KIND
                    TWO_PAIR -> HOUSE
                    THREE_KIND -> FOUR_KIND
                    FOUR_KIND -> FIVE_KIND
                    else -> FIVE_KIND
                }
        }

    }

    private data class Hand(val value: String, val bet: Long)

    /**
     * Sort by type of hand
     * If equal type, sort by strongest card in hand, left to right
     */
    private val handComparator = object : Comparator<Hand> {
        override fun compare(hand: Hand, otherHand: Hand): Int {
            val result = rankHand(hand.value).ordinal.compareTo(rankHand(otherHand.value).ordinal)
            if (result != 0) {
                return result
            }
            for (idx in 0 until 5) {
                val card = Card.fromChar(hand.value[idx])
                val otherCard = Card.fromChar(otherHand.value[idx])
                when (card.ordinal.compareTo(otherCard.ordinal)) {
                    -1 -> return -1
                    1 -> return 1
                    else -> continue
                }
            }
            return 0
        }

    }

    /**
     * Sort by type of hand
     * If equal type, sort by strongest card in hand, left to right
     *
     * Additionally, since Jokers are upgraded now they are also individually the weakest Card
     * So as a exception to ordinal usage, if Card is a Joker we use -1 instead of its ordinal value
     */
    private val handComparatorPart2 = object : Comparator<Hand> {
        override fun compare(hand: Hand, otherHand: Hand): Int {
            val result = rankHandWithUpgrade(hand.value).ordinal.compareTo(rankHandWithUpgrade(otherHand.value).ordinal)
            if (result != 0) {
                return result
            }
            for (idx in 0 until 5) {
                val card = Card.fromChar(hand.value[idx])
                val otherCard = Card.fromChar(otherHand.value[idx])
                when (ordinalUnlessJoker(card).compareTo(ordinalUnlessJoker(otherCard))) {
                    -1 -> return -1
                    1 -> return 1
                    else -> continue
                }
            }
            return 0
        }

        private fun ordinalUnlessJoker(card: Card): Int {
            return if (card == Card.J) -1 else card.ordinal
        }
    }

}