package utils.processors

import models.StringTableEntry
import utils.byteList
import utils.bytesToInt

abstract class SetDemoOrderProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    enum class DemoOrderTarget {
        PLAYER, NPC0, NPC1, NPC2, QUEST
    }

    override val size = 5
    abstract val orderTarget: DemoOrderTarget

    private var animation: Int = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)

        animation = bytesToInt(bytes.slice(2..4))
        return byteList("%s:0x%02x".format(name, animation))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}