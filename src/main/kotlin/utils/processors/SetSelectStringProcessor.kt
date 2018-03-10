package utils.processors

import models.StringTableEntry
import utils.byteList
import utils.bytesToInt
import java.util.*

abstract class SetSelectStringProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    abstract val choices: Int
    final override val size
        get() = 2 + (choices * 2)

    private var choiceIds = ArrayList<Int>()

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)

        val fmtStringBuilder = StringBuilder()
        fmtStringBuilder.append("%s:")

        for (i in 0 until choices) {
            val shortOffset = 2 + (i * 2)
            val choiceId = bytesToInt(bytes.slice(shortOffset..shortOffset+1))
            choiceIds.add(choiceId)
            // println("Choice %d = 0x%04x".format(i, choiceId))
            fmtStringBuilder.append("0x%04x")
            if (i != choices - 1) fmtStringBuilder.append(",")
        }

        return byteList(fmtStringBuilder.toString().format(name, *choiceIds.toArray()))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
