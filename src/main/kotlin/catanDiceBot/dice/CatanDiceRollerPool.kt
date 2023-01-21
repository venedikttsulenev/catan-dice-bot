package catanDiceBot.dice

import catanDiceBot.bot.Defaults
import mu.KotlinLogging

class CatanDiceRollerPool {
    private val log = KotlinLogging.logger("DiceRollerPool")
    private val rollers = mutableMapOf<String, CatanDiceRoller>()

    operator fun get(chatId: String) =
        rollers[chatId] ?: createDiceRoller(chatId, Defaults.totalRolls)

    fun resetRoller(chatId: String, totalRolls: Int) {
        rollers[chatId] = createDiceRoller(chatId, totalRolls)
    }

    private fun createDiceRoller(chatId: String, totalRolls: Int): CatanDiceRoller {
        val roller = CatanDiceRoller(totalRolls)
        rollers[chatId] = roller
        log.info("Created roller. Pool size now: ${rollers.size}")
        return roller
    }
}