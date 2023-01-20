package catanDiceBot.bot

import mu.KotlinLogging
import java.io.File

fun main(args: Array<String>) {
    val log = KotlinLogging.logger("Main")
    val tokenFileName = args[0]
    val tokenFile = File(tokenFileName)
    val token = tokenFile.readText()
    log.info("Token loaded")
    BotFactory(token)
        .createBot()
        .start()
    log.info("Bot started")
}