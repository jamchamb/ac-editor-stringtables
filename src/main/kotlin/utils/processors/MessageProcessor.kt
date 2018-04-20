package utils.processors

import models.StringTableEntry
import kotlin.reflect.KClass

const val PROC_CODE: Byte = 0x7F
const val INTERP_L = "{{"
const val INTERP_R = "}}"
const val P_DELIM = ':'

fun procCodeName(procClass: KClass<out MessageProcessor>): String {
    return procClass::simpleName.get()!!.removeSuffix("Processor").toUpperCase() + "_CODE"
}

abstract class MessageProcessor(val targetEntry: StringTableEntry) {

    abstract val code: Byte
    abstract val size: Int
    abstract val name: String

    open fun makeHeader(): List<Byte> {
        return listOf(PROC_CODE, code)
    }

    /** Decode text from raw byte format */
    open fun decode(bytes: List<Byte>): String {
        if (bytes.size != this.size) {
            throw IllegalArgumentException("Invalid input length: got ${bytes.size} instead of ${this.size}")
        } else if (bytes[1] != this.code) {
            throw IllegalArgumentException("Invalid bytes: given code 0x%02x does not match 0x%02x".format(bytes[1], this.code))
        }

        val resultBuilder = StringBuilder(INTERP_L)
        resultBuilder.append(name)

        val data = decodeImpl(bytes)
        if (data.isNotEmpty()) {
            resultBuilder.append(P_DELIM)
            resultBuilder.append(data)
        }

        resultBuilder.append(INTERP_R)

        return resultBuilder.toString()
    }

    /** Encode text to raw byte format */
    open fun encode(text: String): ByteArray {
        val header = makeHeader()

        val textParts = text.split(P_DELIM)
        val body = encodeImpl(textParts.subList(1, textParts.size))

        var result = header
        if (body != null) result += body

        if (result.size != size) {
            error("Re-encoded string has invalid length (${this.javaClass.name})")
        }

        return result.toByteArray()
    }

    abstract fun decodeImpl(bytes: List<Byte>): String
    abstract fun encodeImpl(textParts: List<String>): List<Byte>?
}
