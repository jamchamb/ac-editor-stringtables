package models

import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.property
import utils.processors.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

val interpolatorLeft = "{{"
val interpolatorRight = "}}"

val processors: Map<Int, KClass<out MessageProcessor>> =
        mapOf(
                Pair(LAST_CODE, LastProcessor::class),
                Pair(CONTINUE_CODE, ContinueProcessor::class),
                Pair(CLEAR_CODE, ClearProcessor::class),
                Pair(PAUSE_CODE, PauseProcessor::class),
                Pair(BUTTON_CODE, ButtonProcessor::class)
        )

class StringTableEntry (id: Int, content: String, rawBytes: ByteArray) {
    var id by property(id)
    fun idProperty() = getProperty(StringTableEntry::id)

    var content: String by property(content)
    fun contentProperty() = getProperty(StringTableEntry::content)

    var rawBytes: ByteArray by property(rawBytes)
    fun rawBytesProperty() = getProperty(StringTableEntry::rawBytes)

    init {
        // TODO: Process custom AC encodings before Shift-JIS
        val decodedBytes = decodeMessage(rawBytes)
        //contentProperty().set(String(decodedBytes, charset("Shift-JIS")))
        this.content = String(decodedBytes, charset("Shift-JIS"))
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
                val code = messageBytes[index+1].toInt()

                if (processors.containsKey(code)) {
                    val proc = processors[code]!!.primaryConstructor!!.call(this)
                    val snippet = messageBytes.slice(index until index + proc.size)

                    resultList.addAll(interpolatorLeft.toByteArray().asList())
                    resultList.addAll(proc.decode(snippet))
                    resultList.addAll(interpolatorRight.toByteArray().asList())
                } else {
                    resultList.addAll("{{SPECIAL}}".toByteArray().asList())
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