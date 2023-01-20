package catanDiceBot.collection

import java.util.LinkedList

class HistoryList<E>(private val capacity: Int) : LinkedList<E>() {
    override fun add(element: E): Boolean {
        if (size == capacity) {
            super.removeFirst()
        }
        super.add(0, element)
        return true
    }
}