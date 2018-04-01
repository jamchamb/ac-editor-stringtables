package utils.processors

import models.StringTableEntry
import utils.bytesToInt
import utils.decodeHexASCII

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

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        val animBytes = decodeHexASCII(textParts[0], 3)
        println("animBytes: $animBytes")
        return animBytes
    }
}