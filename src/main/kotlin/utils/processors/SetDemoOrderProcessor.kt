package utils.processors

import models.StringTableEntry
import org.apache.commons.codec.binary.Hex
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

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        val animHex = textParts[0]

        if (!animHex.startsWith("0x")) error("Hex string missing 0x")
        var animBytes = Hex.decodeHex(animHex.substring(2)).toList()
        if (animBytes.size < 3) TODO("Not enough bytes; left pad this")

        println("animBytes: $animBytes")

        return animBytes.toList()
    }
}