package utils.processors

import models.StringTableEntry
import utils.bytesToInt
import utils.decodeHexASCII

abstract class SetDemoOrderProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    enum class DemoOrderActor(val code: Byte) {
        PLAYER(0),
        NPC0(4),
        NPC1(5),
        NPC2(6),
        QUEST(9)
    }

    private val actorMap: HashMap<String, Byte> = HashMap()
    private val reverseActorMap: HashMap<Byte, String> = HashMap()
    init {
        for (value in DemoOrderActor.values()) {
            actorMap[value.name] = value.code
            reverseActorMap[value.code] = value.name
        }
    }

    private val animationMap: Map<Int, String> = mapOf(
            Pair(0x01, "SHOCK1"),
            Pair(0x02, "SURPRISE_HALO"),
            Pair(0x03, "LAUGH"),
            Pair(0x04, "SURPRISE_LINES"),
            Pair(0x05, "ANGRY_STEAM"),
            Pair(0x06, "SURPRISED"),
            Pair(0x07, "IMPATIENT"),
            Pair(0x08, "SHIVER"),
            Pair(0x09, "TEARS"),
            Pair(0x0A, "HAPPY_FLOWERS"),
            Pair(0x0B, "QUESTION"),
            Pair(0x0C, "IDEA"),
            Pair(0x0D, "GUST"),
            Pair(0x0E, "GIDDY"),
            Pair(0x0F, "THINK"),
            Pair(0x10, "RAIN_CLOUD"),
            Pair(0x11, "HEARTBREAK"),
            Pair(0x12, "WARUDAKUMI"),
            Pair(0x13, "SNORE"),
            Pair(0x14, "HEART"),
            Pair(0x15, "HAPPY"),
            Pair(0x16, "ANGRY"),
            Pair(0x17, "WORRY"),
            Pair(0xFF, "DEFAULT")
    )
    private val reverseAnimationMap: Map<String, Int> = animationMap.entries.associateBy({ it.value }) { it.key }

    override val size = 5
    abstract val orderActor: DemoOrderActor

    private var target: Byte = 0
    private var animation: Int = 0

    override fun decodeImpl(bytes: List<Byte>): String {
        target = bytes[2]
        animation = bytesToInt(bytes.slice(3..4))

        val targetString = when {
            reverseActorMap.containsKey(target) -> reverseActorMap[target]
            else -> "0x%02x".format(target)
        }

        val animString = when {
            animationMap.containsKey(animation) -> animationMap[animation]
            else -> "0x%04x".format(animation)
        }

        return "$targetString$P_DELIM$animString"
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        val target = when {
            actorMap.containsKey(textParts[0]) -> listOf(actorMap[textParts[0]]!!)
            else -> decodeHexASCII(textParts[0], 1)
        }

        val animBytes = when {
            reverseAnimationMap.contains(textParts[1]) -> decodeHexASCII("0x%04x".format(reverseAnimationMap[textParts[1]]), 2)
            else -> decodeHexASCII(textParts[1], 2)
        }

        return target + animBytes
    }
}