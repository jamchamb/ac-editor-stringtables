package utils.processors

import models.StringTableEntry
import utils.bytesToInt
import utils.decodeHexASCII
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
            if (i != choices - 1) fmtStringBuilder.append(P_DELIM)
        }
        return fmtStringBuilder.toString().format(*messageIds.toArray())
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        val result = ArrayList<Byte>(2 * textParts.size)
        for (part in textParts) {
            result.addAll(decodeHexASCII(part, 2))
        }
        return result
    }
}