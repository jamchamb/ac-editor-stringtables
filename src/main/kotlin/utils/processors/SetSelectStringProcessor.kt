package utils.processors

import models.StringTableEntry
import utils.bytesToInt
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
            if (i != choices - 1) fmtStringBuilder.append(",")
        }
        return fmtStringBuilder.toString().format(*choiceIds.toArray())
    }

    override fun encodeImpl(text: String): ByteArray? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
