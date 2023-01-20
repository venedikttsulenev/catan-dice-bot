package catanDiceBot.bot

import com.elbekd.bot.Bot

class BotFactory(private val token: String) {

    fun createBot(): Bot {
        val bot = Bot.createPolling(token)
        val botWrapper = BotWrapper(bot)
        createCallbacks(botWrapper)
        createCommands(botWrapper)
        return bot
    }

    private fun createCallbacks(botWrapper: BotWrapper) {
        botWrapper.run {
            bot.onCallbackQuery("roll") {
                this.roll(it.message!!, it.from)
            }
            bot.onCallbackQuery("stats") {
                this.stats(it.message!!)
            }
            bot.onCallbackQuery("history") {
                this.history(it.message!!)
            }
        }
    }

    private fun createCommands(botWrapper: BotWrapper) {
        botWrapper.run {
            bot.onCommand("/reset") { (msg, opts) ->
                this.reset(msg, opts)
            }
            bot.onCommand("/roll") { (msg, _) ->
                this.roll(msg, msg.from)
            }
            bot.onCommand("/history") { (msg, _) ->
                this.history(msg)
            }
            bot.onCommand("/stats") { (msg, _) ->
                this.stats(msg)
            }
        }
    }

}