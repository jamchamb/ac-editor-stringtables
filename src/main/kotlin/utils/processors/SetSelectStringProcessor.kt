package utils.processors

import models.StringTableEntry
import utils.bytesToInt
import utils.decodeHexASCII
import java.util.*

abstract class SetSelectStringProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    abstract val choices: Int
    final override val size
        get() = 2 + (choices * 2)

    private var choiceIds = ArrayList<Int>()

    override fun decodeImpl(bytes: List<Byte>): String {
        val fmtStringBuilder = StringBuilder()
        for (i in 0 until choices) {
            val shortOffset = 2 + (i * 2)
            val choiceId = bytesToInt(bytes.slice(shortOffset..shortOffset+1))
            choiceIds.add(choiceId)
            // println("Choice %d = 0x%04x".format(i, choiceId))
            fmtStringBuilder.append("0x%04x")
            if (i != choices - 1) fmtStringBuilder.append(P_DELIM)
        }
        return fmtStringBuilder.toString().format(*choiceIds.toArray())
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        val result = ArrayList<Byte>()

        for (textPart in textParts) {
            val choiceIdBytes = decodeHexASCII(textPart, 2)
            result.addAll(choiceIdBytes)
        }

        return result
    }
}
