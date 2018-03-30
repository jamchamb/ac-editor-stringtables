package utils.processors

import models.StringTableEntry
import utils.bytesToInt

abstract class SetDemoOrderProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    enum class DemoOrderTarget {
        PLAYER, NPC0, NPC1, NPC2, QUEST
    }

    override val size = 5
    abstract val orderTarget: DemoOrderTarget

    private var animation: Int = 0

    override fun decodeImpl(bytes: List<Byte>): String {
        animation = bytesToInt(bytes.slice(2..4))
        return "0x%06x".format(animation)
    }

    override fun encodeImpl(text: String): ByteArray? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}