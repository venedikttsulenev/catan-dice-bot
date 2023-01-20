package catanDiceBot.bot

import catanDiceBot.dice.CatanDiceRollerPool
import com.elbekd.bot.Bot
import com.elbekd.bot.model.toChatId
import com.elbekd.bot.types.*

class BotWrapper(val bot: Bot) {
    private val msgReplyMarkup = InlineKeyboardMarkup(
        listOf(listOf(
            InlineKeyboardButton("Зароллить", callbackData = "roll"),
            InlineKeyboardButton("Статистика", callbackData = "stats"),
            InlineKeyboardButton("История", callbackData = "history")
        ))
    )

    private var rollerPool = CatanDiceRollerPool()

    suspend fun reset(msg: Message, opts: String?) {
        val totalRolls = try {
            opts?.toInt()
        } catch (e: NumberFormatException) {
            100
        }
        rollerPool.resetRoller(getChatIdString(msg), totalRolls)
        sendReply(msg, "Ресетнулся на $totalRolls бросков")
    }

    suspend fun roll(msg: Message, user: User?) {
        val username = "${user?.first_name ?: ""} ${user?.lastName ?: ""}"
        val rollResult  = getRollerForMsg(msg).roll(username)
        sendReply(msg, "$username: $rollResult")
    }

    suspend fun history(msg: Message) {
        val messageText = "История бросков:\n${getRollerForMsg(msg).history()}"
        sendReply(msg, messageText)
    }

    suspend fun stats(msg: Message) {
        val stats = getRollerForMsg(msg).stats()
        val statsByValueText = stats.rollsByValue.asIterable()
            .joinToString(separator = "\n") { "${it.key}${if (it.key < 10) " " else ""}: ${it.value.rolls} (${it.value.percentage}%)" }
        val messageText = "```\nВсего бросков: ${stats.totalRolls}\n${statsByValueText}```"
        sendReply(msg, messageText)
    }

    private fun getRollerForMsg(msg: Message) =
        rollerPool[getChatIdString(msg)]

    private suspend fun sendReply(to: Message, replyText: String) {
        bot.sendMessage(
            chatId = to.chat.id.toChatId(),
            text = replyText,
            replyMarkup = msgReplyMarkup,
            parseMode = ParseMode.Markdown
        )
    }

    private fun getChatIdString(msg: Message) = msg.chat.id.toChatId().toString()
}