package catanDiceBot.bot

import mu.KotlinLogging
import java.io.File

fun main(args: Array<String>) {
    App(args).start()
}

class App(private val args: Array<String>) {
    private val log = KotlinLogging.logger("Main")
    fun start() {
        val token = loadToken(args[0])
        val version = loadVersion()
        BotFactory(token)
            .createBot()
            .start()
        log.info("Catan dice roller Telegram bot v$version")
    }
    private fun loadToken(filename: String) = File(filename).readText()
    private fun loadVersion() = javaClass.getResource("/version.txt")?.readText()
}
