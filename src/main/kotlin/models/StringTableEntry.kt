package models

import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.property
import utils.processors.PROC_CODE
import utils.processors.processors
import utils.processors.processorsByTag
import utils.shiftJisDecode
import utils.specialCharacters
import kotlin.reflect.full.primaryConstructor

const val interpolatorLeft = "{{"
const val interpolatorRight = "}}"

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

    /**
     * Shift-JIS decode any plain text, add it, and clear the temporary buffer
     */
    private fun popPlainBytes(temporaryPlainBytes: ArrayList<Byte>, resultBuilder: StringBuilder) {
        if (temporaryPlainBytes.size > 0) {
            resultBuilder.append(shiftJisDecode(temporaryPlainBytes))
            temporaryPlainBytes.clear()
        }
    }

    private fun popPlainBytes(temporaryPlainBytes: ArrayList<Byte>, resultBuilder: ArrayList<Byte>) {

    }

    private fun decodeMessage(messageBytes: ByteArray): String {
        val resultBuilder = StringBuilder()
        val temporaryPlainBytes = ArrayList<Byte>()
        var skipTo = 0

        for ((index, byte) in messageBytes.withIndex()) {
            if (skipTo > index) continue

            // Check for processor directive before last position
            if (index < (messageBytes.size - 1) && byte == PROC_CODE) {
                // Shift-JIS decode any plain text, add it, and clear the temporary buffer
                popPlainBytes(temporaryPlainBytes, resultBuilder)

                // Get the message processor ID
                val code = messageBytes[index+1]

                if (processors.containsKey(code)) {
                    val proc = processors[code]!!.primaryConstructor!!.call(this)
                    val snippet = messageBytes.slice(index until index + proc.size)

                    // Advance position to end of this processed piece
                    skipTo = index + proc.size

                    resultBuilder.append(interpolatorLeft)
                    resultBuilder.append(String(proc.decode(snippet).toByteArray()))
                    resultBuilder.append(interpolatorRight)
                } else {
                    println("Unknown code 0x%02x in message #%d".format(byte, this.id))
                    resultBuilder.append("${interpolatorLeft}UNKNOWN${interpolatorRight}")
                }
            } else if (byte == 0xCD.toByte()) {
                // TODO Handle other relocated characters
                temporaryPlainBytes.add('\n'.toByte())
            } else if (byte == 0x80.toByte()) {
                popPlainBytes(temporaryPlainBytes, resultBuilder)
                println("special 0x80 0x${messageBytes[index+1].toString(16)}")
                skipTo = index + 2
            } else if (byte in specialCharacters) {
                // Check for relocated special Latin characters
                popPlainBytes(temporaryPlainBytes, resultBuilder)
                resultBuilder.append(specialCharacters[byte])
            } else {
                // Add plain text bytes to temporary buffer for Shift-JIS decoding
                temporaryPlainBytes.add(byte)
            }
        }

        // Process remaining plain text bytes
        popPlainBytes(temporaryPlainBytes, resultBuilder)

        return resultBuilder.toString()
    }

    fun encodeMessage(): ByteArray {
        val result = ArrayList<Byte>()
        var skipTo = 0

        println("Searching for interpolated statements")

        var interpolatorStart = content.indexOf(interpolatorLeft, 0, false)
        while (interpolatorStart != -1) {
            println("Interpolator start at $interpolatorStart")

            val interpolatorEnd = content.indexOf(interpolatorRight, interpolatorStart + interpolatorLeft.length, false)
            if (interpolatorEnd == -1) {
                println("Interpolator start with no end! Bailing out.")
                // No more valid interpolators possible.
                break
            }

            val statement = content.slice(interpolatorStart + interpolatorLeft.length until interpolatorEnd)
            println("Statement: $statement")

            val parts = statement.split(":")
            val tag = parts[0]

            // Get processor for this tag
            if (processorsByTag.containsKey(tag)) {
                val processorClass = processorsByTag[tag]!!
                val proc = processorClass.primaryConstructor!!.call(this)
                val rawBytes = proc.encodeImpl(content)
            } else {
                error("Processor $tag not found!")
            }

            // Get next interpolator position
            interpolatorStart = content.indexOf(interpolatorLeft, interpolatorEnd + interpolatorRight.length, false)
        }

        println("Finished processing interpolated statements")

        /*
        for ((index, char: Char) in content.withIndex()) {
            if (skipTo > index) continue

            val charByte = char.toByte()

            // Check for processor directive before last position
            if (index < (content.length - 1) && c) {
                // Get the message processor ID
                val code = messageBytes[index+1]

                if (processors.containsKey(code)) {
                    val proc = processors[code]!!.primaryConstructor!!.call(this)
                    val snippet = messageBytes.slice(index until index + proc.size)

                    // Advance position to end of this processed piece
                    skipTo = index + proc.size

                    resultBuilder.append(interpolatorLeft)
                    resultBuilder.append(String(proc.decode(snippet).toByteArray()))
                    resultBuilder.append(interpolatorRight)
                } else {
                    println("Unknown code 0x%02x in message #%d".format(byte, this.id))
                    resultBuilder.append("${interpolatorLeft}UNKNOWN${interpolatorRight}")
                }
            } else if (byte == 0xCD.toByte()) {
                // TODO Handle other relocated characters
                temporaryPlainBytes.add('\n'.toByte())
            } else if (byte == 0x80.toByte()) {
                popPlainBytes(temporaryPlainBytes, resultBuilder)
                println("special 0x80 0x${messageBytes[index+1].toString(16)}")
                skipTo = index + 2
            } else if (byte in specialCharacters) {
                // Check for relocated special Latin characters
                popPlainBytes(temporaryPlainBytes, resultBuilder)
                resultBuilder.append(specialCharacters[byte])
            } else {
                // Add plain text bytes to temporary buffer for Shift-JIS decoding
                temporaryPlainBytes.add(byte)
            }
        }
        */
        return result.toByteArray()
    }
}

class StringTableEntryModel : ItemViewModel<StringTableEntry>() {
    val id = bind { item?.idProperty() }
    val content = bind { item?.contentProperty() }
    val rawBytes = bind {item?.rawBytesProperty() }
}