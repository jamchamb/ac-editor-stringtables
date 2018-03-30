package utils.processors

import models.StringTableEntry
import kotlin.reflect.KClass

const val PROC_CODE: Byte = 0x7F
const val procSep = ':'

fun procCodeName(procClass: KClass<out MessageProcessor>): String {
    return procClass::simpleName.get()!!.removeSuffix("Processor").toUpperCase() + "_CODE"
}

abstract class MessageProcessor(val targetEntry: StringTableEntry) {

    abstract val code: Byte
    abstract val size: Int
    abstract val name: String

    /** Decode text from raw byte format */
    open fun decode(bytes: List<Byte>): String {
        if (bytes.size != this.size) {
            throw IllegalArgumentException("Invalid input length: got ${bytes.size} instead of ${this.size}")
        } else if (bytes[1] != this.code) {
            throw IllegalArgumentException("Invalid bytes: given code 0x%02x does not match 0x%02x".format(bytes[1], this.code))
        }

        val resultBuilder = StringBuilder(name)
        val data = decodeImpl(bytes)

        if (data.isNotEmpty()) {
            resultBuilder.append(procSep)
            resultBuilder.append(data)
        }

        return resultBuilder.toString()
    }

    /** Encode text to raw byte format */
    open fun encode(text: String): ByteArray {
        val header = listOf(PROC_CODE, code)
        val body = encodeImpl(text)

        if (body != null) {
            return (header + body.toList()).toByteArray()
        } else {
            return header.toByteArray()
        }
    }

    abstract fun decodeImpl(bytes: List<Byte>): String
    abstract fun encodeImpl(text: String): ByteArray?
}
