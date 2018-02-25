package models

import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.property
import utils.processors.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

const val interpolatorLeft = "{{"
const val interpolatorRight = "}}"

val processors: Map<Byte, KClass<out MessageProcessor>> =
        mapOf(
                Pair(LAST_CODE, LastProcessor::class),
                Pair(CONTINUE_CODE, ContinueProcessor::class),
                Pair(CLEAR_CODE, ClearProcessor::class),
                Pair(PAUSE_CODE, PauseProcessor::class),
                Pair(BUTTON_CODE, ButtonProcessor::class),
                Pair(COLOR_LINE_CODE, ColorLineProcessor::class),
                Pair(ABLE_CANCEL_CODE, AbleCancelProcessor::class),
                Pair(UNABLE_CANCEL_CODE, UnableCancelProcessor::class),
                Pair(SET_DEMO_ORDER_PLAYER_CODE, SetDemoOrderPlayerProcessor::class),
                Pair(SET_DEMO_ORDER_NPC0_CODE, SetDemoOrderNPC0Processor::class),
                Pair(SET_DEMO_ORDER_NPC1_CODE, SetDemoOrderNPC1Processor::class),
                Pair(SET_DEMO_ORDER_NPC2_CODE, SetDemoOrderNPC2Processor::class),
                Pair(SET_DEMO_ORDER_QUEST_CODE, SetDemoOrderQuestProcessor::class),
                Pair(SET_SELECT_WINDOW_CODE, SetSelectWindowProcessor::class),
                // MISSING SOME HERE!
                Pair(PLAYER_NAME_CODE, PlayerNameProcessor::class),
                Pair(TALK_NAME_CODE, TalkNameProcessor::class),
                Pair(TAIL_CODE, TailProcessor::class)
        )

class StringTableEntry (id: Int, rawBytes: ByteArray) {
    var id by property(id)
    fun idProperty() = getProperty(StringTableEntry::id)

    var content: String by property<String>()
    fun contentProperty() = getProperty(StringTableEntry::content)

    var rawBytes: ByteArray by property(rawBytes)
    fun rawBytesProperty() = getProperty(StringTableEntry::rawBytes)

    var cancellable by property<Boolean>()
    fun cancellableProperty() = getProperty(StringTableEntry::cancellable)

    init {
        // TODO: Process custom AC encodings before Shift-JIS
        val decodedBytes = decodeMessage(rawBytes)
        //contentProperty().set(String(decodedBytes, charset("Shift-JIS")))
        this.id = id
        this.content = decodedBytes
        this.rawBytes = rawBytes
    }

    override fun toString (): String {
        return "${id}: ${content}"
    }

    fun decodeMessage(messageBytes: ByteArray): String {
        val resultBuilder = StringBuilder()

        val temporaryPlainBytes = ArrayList<Byte>()

        for ((index, byte) in messageBytes.withIndex()) {
            // Check for processor directive before last position
            if (index < (messageBytes.size - 1) && byte == PROC_CODE) {
                // Shift-JIS decode any plain text, add it, and clear the temporary buffer
                if (temporaryPlainBytes.size > 0) {
                    val jisDecodedString = String(temporaryPlainBytes.toByteArray(), charset("Shift-JIS"))
                    resultBuilder.append(jisDecodedString)
                    temporaryPlainBytes.clear()
                }

                // Get the message processor ID
                val code = messageBytes[index+1]

                if (processors.containsKey(code)) {
                    val proc = processors[code]!!.primaryConstructor!!.call(this)
                    val snippet = messageBytes.slice(index until index + proc.size)

                    resultBuilder.append(interpolatorLeft)
                    resultBuilder.append(String(proc.decode(snippet).toByteArray()))
                    resultBuilder.append(interpolatorRight)
                } else {
                    resultBuilder.append("${interpolatorLeft}UNKNOWN${interpolatorRight}")
                }
            } else if (byte == 0xCD.toByte()) {
                temporaryPlainBytes.add('\n'.toByte())
            } else {
                // Add plain text bytes to temporary buffer for Shift-JIS decoding
                temporaryPlainBytes.add(byte)
            }
        }

        // Process remaining plain text bytes
        if (temporaryPlainBytes.size > 0) {
            val jisDecodedString = String(temporaryPlainBytes.toByteArray(), charset("Shift-JIS"))
            resultBuilder.append(jisDecodedString)
        }

        return resultBuilder.toString()
    }
}

class StringTableEntryModel : ItemViewModel<StringTableEntry>() {
    val id = bind { item?.idProperty() }
    val content = bind { item?.contentProperty() }
    val rawBytes = bind {item?.rawBytesProperty() }
}