package utils.processors

import models.StringTableEntry
import kotlin.reflect.KClass

fun procCodeName(procClass: KClass<out MessageProcessor>): String {
    return procClass::simpleName.get()!!.removeSuffix("Processor").toUpperCase() + "_CODE"
}

abstract class MessageProcessor(val targetEntry: StringTableEntry) {

    abstract val code: Int
    abstract val size: Int
    abstract val name: String

    /** Decode text */
    open fun decode(bytes: List<Byte>): List<Byte> {
        if (bytes.size != this.size) {
            throw IllegalArgumentException("Invalid number of input bytes: got ${bytes.size} instead of ${this.size}")
        } else if (bytes[1].toInt() != this.code) {
            throw IllegalArgumentException("Invalid bytes: given code ${bytes[1].toInt()} does not match ${this.code}")
        }

        return bytes
    }

    //abstract fun encode(bytes: List<Byte>)
}

abstract class PlaceholderProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val size = 2

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        return name.toByteArray().asList()
    }
}

/*
 * Message processors
 */

val LAST_CODE = 0x00
class LastProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = LAST_CODE
    override val name = "LAST"
}

val CONTINUE_CODE = 0x01
class ContinueProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CONTINUE_CODE
    override val name = "CONTINUE"
}

val CLEAR_CODE = 0x02
class ClearProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CLEAR_CODE
    override val name = "CLEAR"
}

val PAUSE_CODE = 0x03
class PauseProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = PAUSE_CODE
    override val name = "PAUSE"
    override val size = 3

    var pauseAmount: Int = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        pauseAmount = bytes[2].toInt()
        return "$name:$pauseAmount".toByteArray().asList()
    }
}

val BUTTON_CODE = 0x04
class ButtonProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = BUTTON_CODE
    override val name = "BUTTON"
}
