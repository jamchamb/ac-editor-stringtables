package models

import javafx.beans.property.SimpleBooleanProperty
import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.property
import utils.byteList
import utils.processors.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

val interpolatorLeft = "{{"
val interpolatorRight = "}}"

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
                Pair(SET_SELECT_WINDOW_CODE, SetSelectWindowProcessor::class)
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
        this.content = String(decodedBytes, charset("Shift-JIS"))
        this.rawBytes = rawBytes
    }

    override fun toString (): String {
        return "${id}: ${content}"
    }

    fun decodeMessage(messageBytes: ByteArray): ByteArray {
        if (messageBytes.size < 2) return messageBytes

        val resultList = ArrayList<Byte>()

        for ((index, byte) in messageBytes.withIndex()) {
            val byteVal = byte.toInt()

            // Check for processor directive before last position
            if (index < (messageBytes.size - 1) && byteVal == 0x7F) {
                val code = messageBytes[index+1]

                if (processors.containsKey(code)) {
                    val proc = processors[code]!!.primaryConstructor!!.call(this)
                    val snippet = messageBytes.slice(index until index + proc.size)

                    resultList.addAll(byteList(interpolatorLeft))
                    resultList.addAll(proc.decode(snippet))
                    resultList.addAll(byteList(interpolatorRight))
                } else {
                    resultList.addAll(byteList("{{SPECIAL}}"))
                }
            } else if (byteVal == 0xCD) {
                //resultList.add('\r'.toByte())
                resultList.add('\n'.toByte())
            } else {
                resultList.add(byte)
            }
        }

        return ByteArray(resultList.size, {resultList[it]})
    }
}

class StringTableEntryModel : ItemViewModel<StringTableEntry>() {
    val id = bind { item?.idProperty() }
    val content = bind { item?.contentProperty() }
    val rawBytes = bind {item?.rawBytesProperty() }
}