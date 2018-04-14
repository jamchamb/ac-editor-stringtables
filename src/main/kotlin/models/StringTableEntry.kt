package models

import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.property
import utils.processors.*
import utils.reverseSpecialCharacters
import utils.specialCharacters
import kotlin.reflect.full.primaryConstructor

class StringTableEntry (id: Int, rawBytes: ByteArray) {
    var id by property(id)
    fun idProperty() = getProperty(StringTableEntry::id)

    var content: String by property<String>()
    fun contentProperty() = getProperty(StringTableEntry::content)

    var rawBytes: ByteArray by property(rawBytes)
    fun rawBytesProperty() = getProperty(StringTableEntry::rawBytes)

    /* Properties set by message decoder */

    var cancellable by property<Boolean>()
    fun cancellableProperty() = getProperty(StringTableEntry::cancellable)

    init {
        val decodedBytes = decodeMessage(rawBytes)

        this.id = id
        this.content = decodedBytes
        this.rawBytes = rawBytes
    }

    override fun toString (): String {
        return "${id}: ${content}"
    }

    /**
     * Decode plain text string with game's custom character set
     */
    private fun decodeSpecialChars (bytes: List<Byte>): String {
        val resultBuilder = StringBuilder()

        for (curByte in bytes) {
            if (curByte in specialCharacters) {
                resultBuilder.append(specialCharacters[curByte])
            } else {
                // (curByte.toInt() and 0xff).toChar()
                resultBuilder.append(curByte.toChar())
            }
        }

        return resultBuilder.toString()
    }

    /** Encode plain text string with game's custom charset */
    private fun encodeSpecialChars (text: String): List<Byte> {
        val result = ArrayList<Byte>()

        var position = 0
        while (position < text.length) {
            val codepointSize = Character.charCount(text.codePointAt(position))

            if (position + codepointSize <= text.length) {
                val charS: String = text.substring(position until position + codepointSize)
                if (charS in reverseSpecialCharacters) {
                    val specialByte = reverseSpecialCharacters[charS]!!
                    result.add(specialByte)
                    position += codepointSize
                    continue
                }
            }

            result.add(text[position++].toByte())
        }

        return result
    }

    private fun decodeMessage(messageBytes: ByteArray): String {

        fun popPlainBytes(temporaryPlainBytes: ArrayList<Byte>, resultBuilder: StringBuilder) {
            if (temporaryPlainBytes.size > 0) {
                resultBuilder.append(decodeSpecialChars(temporaryPlainBytes))
                temporaryPlainBytes.clear()
            }
        }

        val resultBuilder = StringBuilder()
        val temporaryPlainBytes = ArrayList<Byte>()
        var skipTo = 0

        for ((index, byte) in messageBytes.withIndex()) {
            if (skipTo > index) continue

            // Check for processor directive before last position
            if (index < (messageBytes.size - 1) && (byte == PROC_CODE || byte == 0x80.toByte())) {
                // Decode any plain text, add it, and clear the temporary buffer
                popPlainBytes(temporaryPlainBytes, resultBuilder)

                var proc: MessageProcessor

                if (byte == PROC_CODE) {
                    // Get the message processor ID
                    val code = messageBytes[index + 1]

                    if (!processors.containsKey(code)) {
                        println("Unknown code 0x%02x in message #%d".format(byte, this.id))
                        resultBuilder.append("${INTERP_L}UNKNOWN${INTERP_R}")
                        continue
                    }

                    proc = processors[code]!!.primaryConstructor!!.call(this)
                } else {
                    println("!!! special 0x80 0x${messageBytes[index+1].toString(16)}")
                    proc = EightyProcessor(this)
                }

                val snippet = messageBytes.slice(index until index + proc.size)

                // Advance position to end of this processed piece
                skipTo = index + proc.size

                resultBuilder.append(proc.decode(snippet))
            } else {
                // Add plain text bytes to temporary buffer for special charset translation
                temporaryPlainBytes.add(byte)
            }
        }

        // Process remaining plain text bytes
        popPlainBytes(temporaryPlainBytes, resultBuilder)

        return resultBuilder.toString()
    }

    fun encodeMessage(): ByteArray {
        val result = ArrayList<Byte>()
        var lastPlaintextBegin = 0

        println("Searching for interpolated statements")

        var interpolatorStart = content.indexOf(INTERP_L, 0, false)
        while (interpolatorStart != -1) {
            println("Interpolator start at $interpolatorStart")

            val interpolatorEnd = content.indexOf(INTERP_R, interpolatorStart + INTERP_L.length, false)
            if (interpolatorEnd == -1) {
                println("Interpolator start with no end! Bailing out.")
                // No more valid interpolators possible.
                break
            }

            // Dump out any in-between plaintext
            if (lastPlaintextBegin < interpolatorStart) {
                val plainText = content.slice(lastPlaintextBegin until interpolatorStart)
                result.addAll(encodeSpecialChars(plainText))
            }
            // Update next possible plain text position
            lastPlaintextBegin = interpolatorEnd + INTERP_R.length

            val statement = content.slice(interpolatorStart + INTERP_L.length until interpolatorEnd)
            println("Statement: $statement")

            // Get name of the processor
            val firstDelimPos = statement.indexOf(P_DELIM)
            val tag = when (firstDelimPos) {
                -1 -> statement
                else -> statement.slice(0 until firstDelimPos)
            }

            // Get processor for this tag
            if (processorsByTag.containsKey(tag)) {
                val processorClass = processorsByTag[tag]!!
                val proc = processorClass.primaryConstructor!!.call(this)
                val reencodedRawBytes = proc.encode(statement)
                result.addAll(reencodedRawBytes.toList())
            } else {
                error("Processor $tag not found!")
            }

            // Get next interpolator position
            interpolatorStart = content.indexOf(INTERP_L, interpolatorEnd + INTERP_R.length, false)
        }

        // Dump remaining plain text
        val plainText = content.slice(lastPlaintextBegin .. content.lastIndex)
        result.addAll(encodeSpecialChars(plainText))

        println("Finished processing interpolated statements")

        println("Resulting buffer: ${String(result.toByteArray())}")

        if (rawBytes.toList() == result) {
            println("RESULT BUFFER MATCHES ORIGINAL!")
        } else {
            for ((index, byte) in rawBytes.withIndex()) {
                if (result[index] != byte) {
                    println("Msg #$id: mismatch at $index: 0x%02x vs 0x%02x".format(byte, result[index]))
                    error("Resulting buffer did not match original")
                }
            }
        }

        return result.toByteArray()
    }
}

class StringTableEntryModel : ItemViewModel<StringTableEntry>() {
    val id = bind { item?.idProperty() }
    val content = bind { item?.contentProperty() }
    val rawBytes = bind {item?.rawBytesProperty() }
}