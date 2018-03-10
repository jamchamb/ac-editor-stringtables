package utils.processors

import models.StringTableEntry
import utils.byteList
import utils.bytesToInt
import java.util.*

abstract class SetNextMessageRandomProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    abstract val choices: Int
    final override val size
        get() = 2 + (choices * 2)

    private var messageIds = ArrayList<Int>()

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)

        // DEBUG
        //println("random message at ${targetEntry.id}")

        val fmtStringBuilder = StringBuilder()
        fmtStringBuilder.append("%s:")

        for (i in 0 until choices) {
            val shortOffset = 2 + (i * 2)
            val choiceId = bytesToInt(bytes.slice(shortOffset..shortOffset + 1))
            messageIds.add(choiceId)
            fmtStringBuilder.append("0x%04x")
            if (i != choices - 1) fmtStringBuilder.append(",")
        }

        return byteList(fmtStringBuilder.toString().format(name, *messageIds.toArray()))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}