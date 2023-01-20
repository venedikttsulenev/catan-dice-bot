package catanDiceBot.dice

data class CatanDiceRollerStats(
    val totalRolls: Int,
    val rollsByValue: Map<Int, CatanDiceRollerStatsPerValue>
)
