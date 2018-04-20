package utils.processors

import models.StringTableEntry
import utils.bytesToInt
import utils.decodeHexASCII

abstract class SetDemoOrderProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    enum class DemoOrderActor {
        PLAYER, NPC0, NPC1, NPC2, QUEST
    }

    override val size = 5
    abstract val orderActor: DemoOrderActor

    private var target: Byte = 0
    private var animation: Int = 0

    override fun decodeImpl(bytes: List<Byte>): String {
        target = bytes[2]
        animation = bytesToInt(bytes.slice(3..4))
        return "0x%02x${P_DELIM}0x%04x".format(target, animation)
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        val target = decodeHexASCII(textParts[0], 1)
        val animBytes = decodeHexASCII(textParts[1], 2)
        return target + animBytes
    }
}