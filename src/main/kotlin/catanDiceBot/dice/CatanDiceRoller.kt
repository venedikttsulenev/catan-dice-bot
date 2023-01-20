package catanDiceBot.dice

import catanDiceBot.collection.HistoryList
import mu.KotlinLogging
import kotlin.math.roundToInt

class CatanDiceRoller(totalRolls: Int) {

    private val log = KotlinLogging.logger("DiceRoller")
    private val totalRolls = if (totalRolls <= 0 || totalRolls > 1000) 100 else totalRolls
    private val chance = IntArray(13) { index ->
        if (index <= 7) index - 1
        else 13 - index
    }
    private val combinations = 36
    private val valueRange = 2..12
    private val values = IntArray(this.totalRolls)
    private val history = HistoryList<Pair<String?, Int>>(this.totalRolls)
    private var rollIndex = 0

    init {
        val fullCombinationSets = this.totalRolls.toFloat() / combinations
        val totalRollsByValue = chance.map { fullCombinationSets.times(it).roundToInt() }.toIntArray()
        totalRollsByValue[7] += this.totalRolls - totalRollsByValue.slice(valueRange).sum()
        log.info("totalRolls = $totalRolls")
        log.info("totalRollsByValue = ${totalRollsByValue.slice(valueRange).joinToString()} (${totalRollsByValue.slice(valueRange).sum()})")
        var index = 0
        valueRange.forEach { value ->
            repeat(totalRollsByValue[value]) {
                values[index++] = (value)
            }
        }
        values.shuffle()
    }

    fun roll(username: String?): Int {
        if (rollIndex == values.size) {
            return 0
        }
        val value = values[rollIndex++]
        history.add(Pair(username, value))
        return value
    }

    fun history(): String {
        return if (history.isEmpty()) "Пусто" else history.joinToString("\n") {
            "${it.first}: ${it.second}"
        }
    }

    fun stats(): CatanDiceRollerStats {
        val counts = IntArray(13)
        (0 until rollIndex).forEach {
            ++counts[values[it]]
        }
        val totalRolls = rollIndex
        val rollsByValue = counts.slice(valueRange).mapIndexed { index, count ->
            val percentage =
                if (totalRolls > 0) count.toFloat().div(totalRolls).times(100).roundToInt()
                else 0
            Pair(index+2, CatanDiceRollerStatsPerValue(count, percentage))
        }.toMap()
        return CatanDiceRollerStats(totalRolls, rollsByValue)
    }
}

/*
 2 -  1 (1,1)
 3 -  2 (1,2) (2,1)
 4 -  3 (1,3) (2,2) (3,1)
 5 -  4 (1,4) (2,3) (3,2) (4,1)
 6 -  5 (1,5) (2,4) (3,3) (4,2) (5,1)
 7 -  6 (1,6) (2,5) (3,4) (4,3) (5,2) (6,1)
 8 -  5 (2,6) (3,5) (4,4) (5,3) (6,2)
 9 -  4 (3,6) (4,5) (5,4) (6,3)
 10 - 3 (4,6) (5,5) (6,4)
 11 - 2 (5,6) (6,5)
 12 - 1 (6,6)

 total = 36

 */