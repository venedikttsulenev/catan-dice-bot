package catanDiceBot.bot

import mu.KotlinLogging
import org.slf4j.MarkerFactory
import java.io.File

fun main(args: Array<String>) {
    val log = KotlinLogging.logger("Main")
    val tokenFileName = args[0]
    val tokenFile = File(tokenFileName)
    val token = tokenFile.readText()
    val totalRolls = if (args.size > 1) args[1].toInt() else null
    log.info("Token loaded")
    BotFactory(token, totalRolls)
        .createBot()
        .start()
    log.info("Bot started")
}