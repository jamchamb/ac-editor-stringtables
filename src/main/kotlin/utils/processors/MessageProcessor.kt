package utils.processors

import models.StringTableEntry
import utils.RGBColor
import utils.byteList
import utils.bytesToInt
import java.util.*
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

abstract class PlaceholderProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val size = 2

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        return name.toByteArray().asList()
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        return listOf(PROC_CODE, this.code).toByteArray()
    }
}

/*
 * Message processors
 */

const val LAST_CODE: Byte = 0x00
class LastProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = LAST_CODE
    override val name = "LAST"
}

const val CONTINUE_CODE: Byte = 0x01
class ContinueProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CONTINUE_CODE
    override val name = "CONTINUE"
}

const val CLEAR_CODE: Byte = 0x02
class ClearProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CLEAR_CODE
    override val name = "CLEAR"
}

const val PAUSE_CODE: Byte = 0x03
class PauseProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = PAUSE_CODE
    override val name = "PAUSE"
    override val size = 3

    var pauseAmount: Int = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        pauseAmount = bytes[2].toInt()
        return byteList("%s:0x%02x".format(name, pauseAmount))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented")
    }
}

const val BUTTON_CODE: Byte = 0x04
class ButtonProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = BUTTON_CODE
    override val name = "BUTTON"
}

const val COLOR_LINE_CODE: Byte = 0x05
class ColorLineProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = COLOR_LINE_CODE
    override val size = 5
    override val name = "COLOR_LINE"

    var color = RGBColor(0,0,0)

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        color = RGBColor(bytes[2], bytes[3], bytes[4])
        return byteList("%s:%02x%02x%02x".format(this.name, color.red, color.green, color.blue))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

const val ABLE_CANCEL_CODE: Byte = 0x06
class AbleCancelProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ABLE_CANCEL_CODE
    override val name = "ABLE_CANCEL"

    init {
        targetEntry.cancellable = true
    }
}

const val UNABLE_CANCEL_CODE: Byte = 0x07
class UnableCancelProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = UNABLE_CANCEL_CODE
    override val name: String = "UNABLE_CANCEL"

    init {
        targetEntry.cancellable = false
    }
}

/* Set demo order classes */
abstract class SetDemoOrderProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    enum class DemoOrderTarget {
        PLAYER, NPC0, NPC1, NPC2, QUEST
    }

    override val size = 5
    abstract val orderTarget: DemoOrderTarget

    var animation: Int = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)

        animation = bytesToInt(listOf(0.toByte()) + bytes.slice(2..4))
        return byteList("%s:%02x:%02x".format(name, bytes[1].toInt(), animation))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
const val SET_DEMO_ORDER_PLAYER_CODE: Byte = 0x08
class SetDemoOrderPlayerProcessor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_PLAYER_CODE
    override val name = "SET_DEMO_ORDER_PLAYER"
    override val orderTarget = DemoOrderTarget.PLAYER
}

const val SET_DEMO_ORDER_NPC0_CODE: Byte = 0x09
class SetDemoOrderNPC0Processor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_NPC0_CODE
    override val name = "SET_DEMO_ORDER_NPC0"
    override val orderTarget = DemoOrderTarget.NPC0
}

const val SET_DEMO_ORDER_NPC1_CODE: Byte = 0x0A
class SetDemoOrderNPC1Processor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_NPC1_CODE
    override val name = "SET_DEMO_ORDER_NPC1"
    override val orderTarget = DemoOrderTarget.NPC1
}

const val SET_DEMO_ORDER_NPC2_CODE: Byte = 0x0B
class SetDemoOrderNPC2Processor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_NPC2_CODE
    override val name = "SET_DEMO_ORDER_NPC2"
    override val orderTarget = DemoOrderTarget.NPC2
}

const val SET_DEMO_ORDER_QUEST_CODE: Byte = 0x0C
class SetDemoOrderQuestProcessor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_QUEST_CODE
    override val name = "SET_DEMO_ORDER_QUEST"
    override val orderTarget = DemoOrderTarget.QUEST
}

const val SET_SELECT_WINDOW_CODE: Byte = 0x0D
class SetSelectWindowProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SET_SELECT_WINDOW_CODE
    override val name = "SET_SELECT_WINDOW"
}

const val SET_NEXT_MESSAGE1_CODE: Byte = 0x10
const val SET_NEXT_MESSAGE2_CODE: Byte = 0x11
const val SET_NEXT_MESSAGEF_CODE: Byte = 0x0E
const val SET_NEXT_MESSAGE0_CODE: Byte = 0x0F
const val SET_NEXT_MESSAGE3_CODE: Byte = 0x12
const val SET_NEXT_MESSAGE_RANDOM2_CODE: Byte = 0x13
const val SET_NEXT_MESSAGE_RANDOM3_CODE: Byte = 0x14
const val SET_NEXT_MESSAGE_RANDOM4_CODE: Byte = 0x15
const val SET_SELECT_STRING2_CODE: Byte = 0x16
const val SET_SELECT_STRING3_CODE: Byte = 0x17
const val SET_SELECT_STRING4_CODE: Byte = 0x18
const val SET_FORCE_NEXT_CODE: Byte = 0x19

/*
'\x1a': 'PLAYER_NAME',
'\x1b': 'TALK_NAME',
'\x1c': 'PHRASE',  # aka "tail"
'\x1d': 'YEAR',
'\x1e': 'MONTH',
'\x1f': 'WEEKDAY',
'\x20': 'DAY',
'\x21': 'HOUR',
'\x22': 'MINUTE',
'\x23': 'SECOND',
# Free string inserts
'\x24': 'FREE0',
'\x25': 'FREE1',
'\x26': 'FREE2',
'\x27': 'FREE3',
'\x28': 'FREE4',
'\x29': 'FREE5',
'\x2a': 'FREE6',
'\x2b': 'FREE7',
'\x2c': 'FREE8',
'\x2d': 'FREE9',
'\x2e': 'CHOICE',  # aka Determination (thing you just chose)
'\x2f': 'TOWN',  # aka CountryName
'\x30': 'RAND_NUM2',  # "RamdomNumber2"
# Items (0x31 - 0x35)
'\x31': 'ITEM0',
'\x32': 'ITEM1',
'\x33': 'ITEM2',
'\x34': 'ITEM3',
'\x35': 'ITEM4',
# More free string inserts
'\x36': 'FREE10',
'\x37': 'FREE11',
'\x38': 'FREE12',
'\x39': 'FREE13',
'\x3a': 'FREE14',
'\x3b': 'FREE15',
'\x3c': 'FREE16',
'\x3d': 'FREE17',
'\x3e': 'FREE18',
'\x3f': 'FREE19',
'\x40': 'MAIL0',
# "Set Player Destiny" 0 - 9 (0x41 - 0x4A)
'\x41': 'DESTINY0',
'\x42': 'DESTINY1',
'\x43': 'DESTINY2',
'\x44': 'DESTINY3',
'\x45': 'DESTINY4',
'\x46': 'DESTINY5',
'\x47': 'DESTINY6',
'\x48': 'DESTINY7',
'\x49': 'DESTINY8',
'\x4a': 'DESTINY9',
# Set Message Contents <emotion> (0x4B - 0x4F)
'\x4b': 'NORMAL',
'\x4c': 'ANGRY',
'\x4d': 'SAD',
'\x4e': 'FUN',
'\x4f': 'SLEEPY',
'\x50': ColorCharProc(),  # 0x50 COLOR aka SetColorChar
'\x51': 'SOUND',
'\x52': 'LINE_OFFSET',
'\x53': 'LINE_TYPE',
'\x54': CharScaleProc(),
'\x55': 'BUTTON2',
'\x56': 'BGM_MAKE',
'\x57': 'BGM_DELETE',
'\x58': 'MSG_TIME_END',
'\x59': SoundTrigProc(),
'\x5a': LineScaleProc(),
'\x5b': 'SOUND_NO_PAGE',
'\x5c': 'VOICE_TRUE',
'\x5d': 'VOICE_FALSE',
'\x5e': 'SELECT_NO_B',
'\x5f': 'GIVE_OPEN',
'\x60': 'GIVE_CLOSE',
'\x61': 'GLOOMY',  # SetMessageContentsGloomy
'\x62': 'SELECT_NO_B_CLOSE',
'\x63': 'NEXT_MSG_RANDOM_SECTION',
'\x64': 'AGB_DUMMY1',
'\x65': 'AGB_DUMMY2',
'\x66': 'AGB_DUMMY3',
'\x67': 'SPACE',
'\x68': 'AGB_DUMMY4',
'\x69': 'AGB_DUMMY5',
'\x6a': 'GENDER_CHECK',
'\x6b': 'AGB_DUMMY6',
'\x6c': 'AGB_DUMMY7',
'\x6d': 'AGB_DUMMY8',
'\x6e': 'AGB_DUMMY9',
'\x6f': 'AGB_DUMMY10',
'\x70': 'AGB_DUMMY11',
'\x71': 'ISLAND_NAME',
'\x72': 'SET_CURSOL_JUST',
'\x73': 'CLR_CUSROL_JUST',
'\x74': 'CUT_ARTICLE',
'\x75': 'CAPITAL',
'\x76': 'AMPM',
'\x77': NextMsgProc(4),
'\x78': NextMsgProc(5),
'\x79': SelectStringProc(5),
'\x7a': SelectStringProc(6),
*/
