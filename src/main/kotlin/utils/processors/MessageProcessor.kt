package utils.processors

import models.StringTableEntry
import kotlin.reflect.KClass

const val PROC_CODE: Byte = 0x7F

fun procCodeName(procClass: KClass<out MessageProcessor>): String {
    return procClass::simpleName.get()!!.removeSuffix("Processor").toUpperCase() + "_CODE"
}

abstract class MessageProcessor(val targetEntry: StringTableEntry) {

    abstract val code: Byte
    abstract val size: Int
    abstract val name: String

    /** Decode text */
    open fun decode(bytes: List<Byte>): List<Byte> {
        if (bytes.size != this.size) {
            throw IllegalArgumentException("Invalid input length: got ${bytes.size} instead of ${this.size}")
        } else if (bytes[1] != this.code) {
            throw IllegalArgumentException("Invalid bytes: given code 0x%02x does not match 0x%02x".format(bytes[1], this.code))
        }

        return bytes
    }

    abstract fun encode(bytes: List<Byte>): ByteArray
}
