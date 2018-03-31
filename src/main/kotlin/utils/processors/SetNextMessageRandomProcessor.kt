package utils.processors

import models.StringTableEntry
import utils.bytesToInt
import java.util.*

abstract class SetNextMessageRandomProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    abstract val choices: Int
    final override val size
        get() = 2 + (choices * 2)

    private var messageIds = ArrayList<Int>()

    override fun decodeImpl(bytes: List<Byte>): String {
        // DEBUG
        //println("random message at ${targetEntry.id}")

        val fmtStringBuilder = StringBuilder()
        for (i in 0 until choices) {
            val shortOffset = 2 + (i * 2)
            val choiceId = bytesToInt(bytes.slice(shortOffset..shortOffset + 1))
            messageIds.add(choiceId)
            fmtStringBuilder.append("0x%04x")
            if (i != choices - 1) fmtStringBuilder.append(",")
        }
        return fmtStringBuilder.toString().format(*messageIds.toArray())
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}