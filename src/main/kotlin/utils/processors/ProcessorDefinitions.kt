package utils.processors

import models.StringTableEntry
import utils.RGBColor
import utils.byteList
import utils.bytesToInt

/*
 * Message processors
 */

const val LAST_CODE: Byte = 0x00
const val LAST_NAME = "LAST"
class LastProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = LAST_CODE
    override val name = LAST_NAME
}

const val CONTINUE_CODE: Byte = 0x01
const val CONTINUE_TAG = "CONTINUE"
class ContinueProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CONTINUE_CODE
    override val name = CONTINUE_TAG
}

const val CLEAR_CODE: Byte = 0x02
const val CLEAR_TAG = "CLEAR"
class ClearProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CLEAR_CODE
    override val name = CLEAR_TAG
}

const val PAUSE_CODE: Byte = 0x03
const val PAUSE_TAG = "PAUSE"
class PauseProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = PAUSE_CODE
    override val name = PAUSE_TAG
    override val size = 3

    var pauseAmount: Int = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        pauseAmount = bytesToInt(listOf(bytes[2]))
        return byteList("%s:0x%02x".format(name, pauseAmount))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented")
    }
}

const val BUTTON_CODE: Byte = 0x04
const val BUTTON_TAG = "BUTTON"
class ButtonProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = BUTTON_CODE
    override val name = BUTTON_TAG
}

const val COLOR_LINE_CODE: Byte = 0x05
const val COLOR_LINE_TAG = "COLOR_LINE"
class ColorLineProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = COLOR_LINE_CODE
    override val name = COLOR_LINE_TAG
    override val size = 5

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
const val ABLE_CANCEL_TAG = "ABLE_CANCEL"
class AbleCancelProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ABLE_CANCEL_CODE
    override val name = ABLE_CANCEL_TAG

    init {
        targetEntry.cancellable = true
    }
}

const val UNABLE_CANCEL_CODE: Byte = 0x07
const val UNABLE_CANCEL_TAG = "UNABLE_CANCEL"
class UnableCancelProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = UNABLE_CANCEL_CODE
    override val name: String = UNABLE_CANCEL_TAG

    init {
        targetEntry.cancellable = false
    }
}

/* Set demo order classes */

const val SET_DEMO_ORDER_PLAYER_CODE: Byte = 0x08
const val SET_DEMO_ORDER_PLAYER_TAG = "SET_DEMO_ORDER_PLAYER"
class SetDemoOrderPlayerProcessor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_PLAYER_CODE
    override val name = SET_DEMO_ORDER_PLAYER_TAG
    override val orderTarget = DemoOrderTarget.PLAYER
}

const val SET_DEMO_ORDER_NPC0_CODE: Byte = 0x09
const val SET_DEMO_ORDER_NPC0_TAG = "SET_DEMO_ORDER_NPC0"
class SetDemoOrderNPC0Processor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_NPC0_CODE
    override val name = SET_DEMO_ORDER_NPC0_TAG
    override val orderTarget = DemoOrderTarget.NPC0
}

const val SET_DEMO_ORDER_NPC1_CODE: Byte = 0x0A
const val SET_DEMO_ORDER_NPC1_TAG = "SET_DEMO_ORDER_NPC1"
class SetDemoOrderNPC1Processor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_NPC1_CODE
    override val name = SET_DEMO_ORDER_NPC1_TAG
    override val orderTarget = DemoOrderTarget.NPC1
}

const val SET_DEMO_ORDER_NPC2_CODE: Byte = 0x0B
const val SET_DEMO_ORDER_NPC2_TAG = "SET_DEMO_ORDER_NPC2"
class SetDemoOrderNPC2Processor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_NPC2_CODE
    override val name = SET_DEMO_ORDER_NPC2_TAG
    override val orderTarget = DemoOrderTarget.NPC2
}

const val SET_DEMO_ORDER_QUEST_CODE: Byte = 0x0C
const val SET_DEMO_ORDER_QUEST_TAG = "SET_DEMO_ORDER_QUEST"
class SetDemoOrderQuestProcessor(targetEntry: StringTableEntry): SetDemoOrderProcessor(targetEntry) {
    override val code = SET_DEMO_ORDER_QUEST_CODE
    override val name = SET_DEMO_ORDER_QUEST_TAG
    override val orderTarget = DemoOrderTarget.QUEST
}

const val SET_SELECT_WINDOW_CODE: Byte = 0x0D
const val SET_SELECT_WINDOW_TAG = "SET_SELECT_WINDOW"
class SetSelectWindowProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SET_SELECT_WINDOW_CODE
    override val name = SET_SELECT_WINDOW_TAG
}

/* Set next message option strings */

const val SET_NEXT_MESSAGEF_CODE: Byte = 0x0E
const val SET_NEXT_MESSAGEF_TAG = "SET_NEXT_MESSAGE_F"
class SetNextMessageFProcessor(targetEntry: StringTableEntry): SetNextMessageProcessor(targetEntry) {
    override val code = SET_NEXT_MESSAGEF_CODE
    override val name = SET_NEXT_MESSAGEF_TAG

    // NextMessageF specifies the mandatory next message (no choices)
    override val messageSlot = -1
}

const val SET_NEXT_MESSAGE0_CODE: Byte = 0x0F
const val SET_NEXT_MESSAGE0_TAG = "SET_NEXT_MESSAGE_0"
class SetNextMessage0Processor(targetEntry: StringTableEntry): SetNextMessageProcessor(targetEntry) {
    override val code = SET_NEXT_MESSAGE0_CODE
    override val name = SET_NEXT_MESSAGE0_TAG
    override val messageSlot = 0
}


const val SET_NEXT_MESSAGE1_CODE: Byte = 0x10
const val SET_NEXT_MESSAGE1_TAG = "SET_NEXT_MESSAGE_1"
class SetNextMessage1Processor(targetEntry: StringTableEntry): SetNextMessageProcessor(targetEntry) {
    override val code = SET_NEXT_MESSAGE1_CODE
    override val name = SET_NEXT_MESSAGE1_TAG
    override val messageSlot = 1
}

const val SET_NEXT_MESSAGE2_CODE: Byte = 0x11
const val SET_NEXT_MESSAGE2_TAG = "SET_NEXT_MESSAGE_2"
class SetNextMessage2Processor(targetEntry: StringTableEntry): SetNextMessageProcessor(targetEntry) {
    override val code = SET_NEXT_MESSAGE2_CODE
    override val name = SET_NEXT_MESSAGE2_TAG
    override val messageSlot = 2
}

const val SET_NEXT_MESSAGE3_CODE: Byte = 0x12
const val SET_NEXT_MESSAGE3_TAG = "SET_NEXT_MESSAGE_3"
class SetNextMessage3Processor(targetEntry: StringTableEntry): SetNextMessageProcessor(targetEntry) {
    override val code = SET_NEXT_MESSAGE3_CODE
    override val name = SET_NEXT_MESSAGE3_TAG
    override val messageSlot = 3
}

/*
 * Set next message random processors
 */

const val SET_NEXT_MESSAGE_RANDOM2_CODE: Byte = 0x13
const val SET_NEXT_MESSAGE_RANDOM2_TAG = "SET_NEXT_MESSAGE_RANDOM_2"
class SetNextMessageRandom2Processor(targetEntry: StringTableEntry): SetNextMessageRandomProcessor(targetEntry) {
    override val code = SET_NEXT_MESSAGE_RANDOM2_CODE
    override val name = SET_NEXT_MESSAGE_RANDOM2_TAG
    override val choices = 2
}

const val SET_NEXT_MESSAGE_RANDOM3_CODE: Byte = 0x14
const val SET_NEXT_MESSAGE_RANDOM3_TAG = "SET_NEXT_MESSAGE_RANDOM_3"
class SetNextMessageRandom3Processor(targetEntry: StringTableEntry): SetNextMessageRandomProcessor(targetEntry) {
    override val code = SET_NEXT_MESSAGE_RANDOM3_CODE
    override val name = SET_NEXT_MESSAGE_RANDOM3_TAG
    override val choices = 3
}

const val SET_NEXT_MESSAGE_RANDOM4_CODE: Byte = 0x15
const val SET_NEXT_MESSAGE_RANDOM4_TAG = "SET_NEXT_MESSAGE_RANDOM_4"
class SetNextMessageRandom4Processor(targetEntry: StringTableEntry): SetNextMessageRandomProcessor(targetEntry) {
    override val code = SET_NEXT_MESSAGE_RANDOM4_CODE
    override val name = SET_NEXT_MESSAGE_RANDOM4_TAG
    override val choices = 4
}

/* Set select strings (choices in choice pop-up). The number corresponds
 * to the number of choices.
 */
const val SET_SELECT_STRING2_CODE: Byte = 0x16
const val SET_SELECT_STRING2_TAG = "SET_SELECT_STRING_2"
class SetSelectString2Processor(targetEntry: StringTableEntry): SetSelectStringProcessor(targetEntry) {
    override val code = SET_SELECT_STRING2_CODE
    override val name = SET_SELECT_STRING2_TAG
    override val choices = 2
}

const val SET_SELECT_STRING3_CODE: Byte = 0x17
const val SET_SELECT_STRING3_TAG = "SET_SELECT_STRING_3"
class SetSelectString3Processor(targetEntry: StringTableEntry): SetSelectStringProcessor(targetEntry) {
    override val code = SET_SELECT_STRING3_CODE
    override val name = SET_SELECT_STRING3_TAG
    override val choices = 3
}

const val SET_SELECT_STRING4_CODE: Byte = 0x18
const val SET_SELECT_STRING4_TAG = "SET_SELECT_STRING_4"
class SetSelectString4Processor(targetEntry: StringTableEntry): SetSelectStringProcessor(targetEntry) {
    override val code = SET_SELECT_STRING4_CODE
    override val name = SET_SELECT_STRING4_TAG
    override val choices = 4
}

const val SET_FORCE_NEXT_CODE: Byte = 0x19
const val SET_FORCE_NEXT_TAG = "SET_FORCE_NEXT"
class SetForceNextProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SET_FORCE_NEXT_CODE
    override val name = SET_FORCE_NEXT_TAG
}

const val PLAYER_NAME_CODE: Byte = 0x1A
const val PLAYER_NAME_TAG = "PLAYER_NAME"
class PlayerNameProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = PLAYER_NAME_CODE
    override val name = PLAYER_NAME_TAG
}

const val TALK_NAME_CODE: Byte = 0x1B
const val TALK_NAME_TAG = "TALK_NAME"
class TalkNameProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = TALK_NAME_CODE
    override val name = TALK_NAME_TAG
}

const val TAIL_CODE: Byte = 0x1C
const val TAIL_TAG = "TAIL"
class TailProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = TAIL_CODE
    override val name = TAIL_TAG
}

const val YEAR_CODE: Byte = 0x1D
const val YEAR_TAG = "YEAR"
class YearProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = YEAR_CODE
    override val name = YEAR_TAG
}

const val MONTH_CODE: Byte = 0x1E
const val MONTH_TAG = "MONTH"
class MonthProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = MONTH_CODE
    override val name = MONTH_TAG
}

const val WEEKDAY_CODE: Byte = 0x1F
const val WEEKDAY_TAG = "WEEKDAY"
class WeekdayProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = WEEKDAY_CODE
    override val name = WEEKDAY_TAG
}

const val DAY_CODE: Byte = 0x20
const val DAY_TAG = "DAY"
class DayProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DAY_CODE
    override val name = DAY_TAG
}

const val HOUR_CODE: Byte = 0x21
const val HOUR_TAG = "HOUR"
class HourProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = HOUR_CODE
    override val name = HOUR_TAG
}

const val MINUTE_CODE: Byte = 0x22
const val MINUTE_TAG = "MINUTE"
class MinuteProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = MINUTE_CODE
    override val name = MINUTE_TAG
}

const val SECOND_CODE: Byte = 0x23
const val SECOND_TAG = "SECOND"
class SecondProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SECOND_CODE
    override val name = SECOND_TAG
}

const val FREE0_CODE: Byte = 0x24
const val FREE0_TAG = "FREE0"
class Free0Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE0_CODE
    override val name = FREE0_TAG
}

const val FREE1_CODE: Byte = 0x25
const val FREE1_TAG = "FREE1"
class Free1Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE1_CODE
    override val name = FREE1_TAG
}

const val FREE2_CODE: Byte = 0x26
const val FREE2_TAG = "FREE2"
class Free2Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE2_CODE
    override val name = FREE2_TAG
}

const val FREE3_CODE: Byte = 0x27
const val FREE3_TAG = "FREE3"
class Free3Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE3_CODE
    override val name = FREE3_TAG
}

const val FREE4_CODE: Byte = 0x28
const val FREE4_TAG = "FREE4"
class Free4Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE4_CODE
    override val name = FREE4_TAG
}

const val FREE5_CODE: Byte = 0x29
const val FREE5_TAG = "FREE5"
class Free5Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE5_CODE
    override val name = FREE5_TAG
}

const val FREE6_CODE: Byte = 0x2A
const val FREE6_TAG = "FREE6"
class Free6Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE6_CODE
    override val name = FREE6_TAG
}

const val FREE7_CODE: Byte = 0x2B
const val FREE7_TAG = "FREE7"
class Free7Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE7_CODE
    override val name = FREE7_TAG
}

const val FREE8_CODE: Byte = 0x2C
const val FREE8_TAG = "FREE8"
class Free8Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE8_CODE
    override val name = FREE8_TAG
}

const val FREE9_CODE: Byte = 0x2D
const val FREE9_TAG = "FREE9"
class Free9Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE9_CODE
    override val name = FREE9_TAG
}

const val DETERMINATION_CODE: Byte = 0x2E
const val DETERMINATION_TAG = "DETERMINATION"
class DeterminationProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DETERMINATION_CODE
    override val name = DETERMINATION_TAG
}

const val TOWN_CODE: Byte = 0x2F
const val TOWN_TAG = "TOWN"
class TownProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = TOWN_CODE
    override val name = TOWN_TAG
}

const val RANDOM_NUMBER2_CODE: Byte = 0x30
const val RANDOM_NUMBER2_TAG = "RANDOM_NUMBER2"
class RandomNumber2Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = RANDOM_NUMBER2_CODE
    override val name = RANDOM_NUMBER2_TAG
}

const val ITEM0_CODE: Byte = 0x31
const val ITEM0_TAG = "ITEM0"
class Item0Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ITEM0_CODE
    override val name = ITEM0_TAG
}

const val ITEM1_CODE: Byte = 0x32
const val ITEM1_TAG = "ITEM1"
class Item1Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ITEM1_CODE
    override val name = ITEM1_TAG
}

const val ITEM2_CODE: Byte = 0x33
const val ITEM2_TAG = "ITEM2"
class Item2Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ITEM2_CODE
    override val name = ITEM2_TAG
}

const val ITEM3_CODE: Byte = 0x34
const val ITEM3_TAG = "ITEM3"
class Item3Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ITEM3_CODE
    override val name = ITEM3_TAG
}

const val ITEM4_CODE: Byte = 0x35
const val ITEM4_TAG = "ITEM4"
class Item4Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ITEM4_CODE
    override val name = ITEM4_TAG
}

const val FREE10_CODE: Byte = 0x36
const val FREE10_TAG = "FREE10"
class Free10Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE10_CODE
    override val name = FREE10_TAG
}

const val FREE11_CODE: Byte = 0x37
const val FREE11_TAG = "FREE11"
class Free11Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE11_CODE
    override val name = FREE11_TAG
}

const val FREE12_CODE: Byte = 0x38
const val FREE12_TAG = "FREE12"
class Free12Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE12_CODE
    override val name = FREE12_TAG
}

const val FREE13_CODE: Byte = 0x39
const val FREE13_TAG = "FREE13"
class Free13Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE13_CODE
    override val name = FREE13_TAG
}

const val FREE14_CODE: Byte = 0x3A
const val FREE14_TAG = "FREE14"
class Free14Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE14_CODE
    override val name = FREE14_TAG
}

const val FREE15_CODE: Byte = 0x3B
const val FREE15_TAG = "FREE15"
class Free15Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE15_CODE
    override val name = FREE15_TAG
}

const val FREE16_CODE: Byte = 0x3C
const val FREE16_TAG = "FREE16"
class Free16Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE16_CODE
    override val name = FREE16_TAG
}

const val FREE17_CODE: Byte = 0x3D
const val FREE17_TAG = "FREE17"
class Free17Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE17_CODE
    override val name = FREE17_TAG
}

const val FREE18_CODE: Byte = 0x3E
const val FREE18_TAG = "FREE18"
class Free18Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE18_CODE
    override val name = FREE18_TAG
}

const val FREE19_CODE: Byte = 0x3F
const val FREE19_TAG = "FREE19"
class Free19Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FREE19_CODE
    override val name = FREE19_TAG
}

const val MAIL0_CODE: Byte = 0x40
const val MAIL0_TAG = "MAIL0"
class Mail0Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = MAIL0_CODE
    override val name = MAIL0_TAG
}

/*
 * Set player destiny processors
 */


const val DESTINY0_CODE: Byte = 0x41
const val DESTINY0_TAG = "DESTINY0"
class Destiny0Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY0_CODE
    override val name = DESTINY0_TAG
}

const val DESTINY1_CODE: Byte = 0x42
const val DESTINY1_TAG = "DESTINY1"
class Destiny1Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY1_CODE
    override val name = DESTINY1_TAG
}

const val DESTINY2_CODE: Byte = 0x43
const val DESTINY2_TAG = "DESTINY2"
class Destiny2Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY2_CODE
    override val name = DESTINY2_TAG
}

const val DESTINY3_CODE: Byte = 0x44
const val DESTINY3_TAG = "DESTINY3"
class Destiny3Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY3_CODE
    override val name = DESTINY3_TAG
}

const val DESTINY4_CODE: Byte = 0x45
const val DESTINY4_TAG = "DESTINY4"
class Destiny4Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY4_CODE
    override val name = DESTINY4_TAG
}

const val DESTINY5_CODE: Byte = 0x46
const val DESTINY5_TAG = "DESTINY5"
class Destiny5Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY5_CODE
    override val name = DESTINY5_TAG
}

const val DESTINY6_CODE: Byte = 0x47
const val DESTINY6_TAG = "DESTINY6"
class Destiny6Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY6_CODE
    override val name = DESTINY6_TAG
}

const val DESTINY7_CODE: Byte = 0x48
const val DESTINY7_TAG = "DESTINY7"
class Destiny7Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY7_CODE
    override val name = DESTINY7_TAG
}

const val DESTINY8_CODE: Byte = 0x49
const val DESTINY8_TAG = "DESTINY8"
class Destiny8Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY8_CODE
    override val name = DESTINY8_TAG
}

const val DESTINY9_CODE: Byte = 0x4A
const val DESTINY9_TAG = "DESTINY9"
class Destiny9Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = DESTINY9_CODE
    override val name = DESTINY9_TAG
}

const val NORMAL_CODE: Byte = 0x4B
const val NORMAL_TAG = "NORMAL"
class NormalProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = NORMAL_CODE
    override val name = NORMAL_TAG
}

const val ANGRY_CODE: Byte = 0x4C
const val ANGRY_TAG = "ANGRY"
class AngryProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ANGRY_CODE
    override val name = ANGRY_TAG
}

const val SAD_CODE: Byte = 0x4D
const val SAD_TAG = "SAD"
class SadProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SAD_CODE
    override val name = SAD_TAG
}

const val FUN_CODE: Byte = 0x4E
const val FUN_TAG = "FUN"
class FunProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = FUN_CODE
    override val name = FUN_TAG
}

const val SLEEPY_CODE: Byte = 0x4F
const val SLEEPY_TAG = "SLEEPY"
class SleepyProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SLEEPY_CODE
    override val name = SLEEPY_TAG
}

const val SET_COLOR_CHAR_CODE: Byte = 0x50
const val SET_COLOR_CHAR_TAG = "SET_COLOR_CHAR"
class SetColorCharProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = SET_COLOR_CHAR_CODE
    override val name = SET_COLOR_CHAR_TAG
    override val size = 2 + 3 + 1

    var color = RGBColor(0, 0, 0)
    var amount = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)

        color.red = bytes[2]
        color.green = bytes[3]
        color.blue = bytes[4]
        amount = bytesToInt(listOf(bytes[5]))

        //println("color char in message ${targetEntry.id}")

        return byteList("$name:%02x%02x%02x:0x%02x".format(color.red, color.green, color.blue, amount))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

const val SOUND_CUT_CODE: Byte = 0x51
const val SOUND_CUT_TAG = "SOUND_CUT"
class SoundCutProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = SOUND_CUT_CODE
    override val name = SOUND_CUT_TAG
    override val size = 3

    // Always 0 or 1
    var soundCut = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        soundCut = bytesToInt(listOf(bytes[2]))

        return byteList("$name:$soundCut")
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

const val SET_LINE_OFFSET_CODE: Byte = 0x52
const val SET_LINE_OFFSET_TAG = "SET_LINE_OFFSET"
class SetLineOffsetProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = SET_LINE_OFFSET_CODE
    override val name = SET_LINE_OFFSET_TAG
    override val size = 3

    private var lineOffset = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        lineOffset = bytesToInt(listOf(bytes[2]))
        return byteList("$name:0x%02x".format(lineOffset))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

const val SET_LINE_TYPE_CODE: Byte = 0x53
const val SET_LINE_TYPE_TAG = "SET_LINE_TYPE"
class SetLineTypeProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = SET_LINE_TYPE_CODE
    override val name = SET_LINE_TYPE_TAG
    override val size = 3

    private var type: Byte = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)
        type = bytes[2]
        return byteList("$name:0x%02x".format(type))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

/* TODO: Placeholder for 0x54: CharScaleProc */

const val BUTTON2_CODE: Byte = 0x55
const val BUTTON2_TAG = "BUTTON2"
class Button2Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = BUTTON2_CODE
    override val name = BUTTON2_TAG
}

const val BGM_MAKE_CODE: Byte = 0x56
const val BGM_MAKE_TAG = "BGM_MAKE"
class Bgm_MakeProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = BGM_MAKE_CODE
    override val name = BGM_MAKE_TAG
}

const val BGM_DELETE_CODE: Byte = 0x57
const val BGM_DELETE_TAG = "BGM_DELETE"
class Bgm_DeleteProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = BGM_DELETE_CODE
    override val name = BGM_DELETE_TAG
}

const val MSG_TIME_END_CODE: Byte = 0x58
const val MSG_TIME_END_TAG = "MSG_TIME_END"
class Msg_Time_EndProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = MSG_TIME_END_CODE
    override val name = MSG_TIME_END_TAG
}
/* TODO: Placeholder for 0x59: SoundTrigProc */
/* TODO: Placeholder for 0x5a: LineScaleProc */

const val SOUND_NO_PAGE_CODE: Byte = 0x5B
const val SOUND_NO_PAGE_TAG = "SOUND_NO_PAGE"
class Sound_No_PageProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SOUND_NO_PAGE_CODE
    override val name = SOUND_NO_PAGE_TAG
}

const val VOICE_TRUE_CODE: Byte = 0x5C
const val VOICE_TRUE_TAG = "VOICE_TRUE"
class Voice_TrueProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = VOICE_TRUE_CODE
    override val name = VOICE_TRUE_TAG
}

const val VOICE_FALSE_CODE: Byte = 0x5D
const val VOICE_FALSE_TAG = "VOICE_FALSE"
class Voice_FalseProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = VOICE_FALSE_CODE
    override val name = VOICE_FALSE_TAG
}

const val SELECT_NO_B_CODE: Byte = 0x5E
const val SELECT_NO_B_TAG = "SELECT_NO_B"
class Select_No_BProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SELECT_NO_B_CODE
    override val name = SELECT_NO_B_TAG
}

const val GIVE_OPEN_CODE: Byte = 0x5F
const val GIVE_OPEN_TAG = "GIVE_OPEN"
class Give_OpenProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = GIVE_OPEN_CODE
    override val name = GIVE_OPEN_TAG
}

const val GIVE_CLOSE_CODE: Byte = 0x60
const val GIVE_CLOSE_TAG = "GIVE_CLOSE"
class Give_CloseProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = GIVE_CLOSE_CODE
    override val name = GIVE_CLOSE_TAG
}

const val GLOOMY_CODE: Byte = 0x61
const val GLOOMY_TAG = "GLOOMY"
class GloomyProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = GLOOMY_CODE
    override val name = GLOOMY_TAG
}

const val SELECT_NO_B_CLOSE_CODE: Byte = 0x62
const val SELECT_NO_B_CLOSE_TAG = "SELECT_NO_B_CLOSE"
class Select_No_B_CloseProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SELECT_NO_B_CLOSE_CODE
    override val name = SELECT_NO_B_CLOSE_TAG
}

const val NEXT_MSG_RANDOM_SECTION_CODE: Byte = 0x63
const val NEXT_MSG_RANDOM_SECTION_TAG = "NEXT_MSG_RANDOM_SECTION"
class Next_Msg_Random_SectionProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = NEXT_MSG_RANDOM_SECTION_CODE
    override val name = NEXT_MSG_RANDOM_SECTION_TAG
}

const val AGB_DUMMY1_CODE: Byte = 0x64
const val AGB_DUMMY1_TAG = "AGB_DUMMY1"
class Agb_Dummy1Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY1_CODE
    override val name = AGB_DUMMY1_TAG
}

const val AGB_DUMMY2_CODE: Byte = 0x65
const val AGB_DUMMY2_TAG = "AGB_DUMMY2"
class Agb_Dummy2Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY2_CODE
    override val name = AGB_DUMMY2_TAG
}

const val AGB_DUMMY3_CODE: Byte = 0x66
const val AGB_DUMMY3_TAG = "AGB_DUMMY3"
class Agb_Dummy3Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY3_CODE
    override val name = AGB_DUMMY3_TAG
}

const val SPACE_CODE: Byte = 0x67
const val SPACE_TAG = "SPACE"
class SpaceProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SPACE_CODE
    override val name = SPACE_TAG
}

const val AGB_DUMMY4_CODE: Byte = 0x68
const val AGB_DUMMY4_TAG = "AGB_DUMMY4"
class Agb_Dummy4Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY4_CODE
    override val name = AGB_DUMMY4_TAG
}

const val AGB_DUMMY5_CODE: Byte = 0x69
const val AGB_DUMMY5_TAG = "AGB_DUMMY5"
class Agb_Dummy5Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY5_CODE
    override val name = AGB_DUMMY5_TAG
}

const val GENDER_CHECK_CODE: Byte = 0x6A
const val GENDER_CHECK_TAG = "GENDER_CHECK"
class Gender_CheckProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = GENDER_CHECK_CODE
    override val name = GENDER_CHECK_TAG
}

const val AGB_DUMMY6_CODE: Byte = 0x6B
const val AGB_DUMMY6_TAG = "AGB_DUMMY6"
class Agb_Dummy6Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY6_CODE
    override val name = AGB_DUMMY6_TAG
}

const val AGB_DUMMY7_CODE: Byte = 0x6C
const val AGB_DUMMY7_TAG = "AGB_DUMMY7"
class Agb_Dummy7Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY7_CODE
    override val name = AGB_DUMMY7_TAG
}

const val AGB_DUMMY8_CODE: Byte = 0x6D
const val AGB_DUMMY8_TAG = "AGB_DUMMY8"
class Agb_Dummy8Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY8_CODE
    override val name = AGB_DUMMY8_TAG
}

const val AGB_DUMMY9_CODE: Byte = 0x6E
const val AGB_DUMMY9_TAG = "AGB_DUMMY9"
class Agb_Dummy9Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY9_CODE
    override val name = AGB_DUMMY9_TAG
}

const val AGB_DUMMY10_CODE: Byte = 0x6F
const val AGB_DUMMY10_TAG = "AGB_DUMMY10"
class Agb_Dummy10Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY10_CODE
    override val name = AGB_DUMMY10_TAG
}

const val AGB_DUMMY11_CODE: Byte = 0x70
const val AGB_DUMMY11_TAG = "AGB_DUMMY11"
class Agb_Dummy11Processor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AGB_DUMMY11_CODE
    override val name = AGB_DUMMY11_TAG
}

const val ISLAND_NAME_CODE: Byte = 0x71
const val ISLAND_NAME_TAG = "ISLAND_NAME"
class Island_NameProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = ISLAND_NAME_CODE
    override val name = ISLAND_NAME_TAG
}

const val SET_CURSOL_JUST_CODE: Byte = 0x72
const val SET_CURSOL_JUST_TAG = "SET_CURSOL_JUST"
class Set_Cursol_JustProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = SET_CURSOL_JUST_CODE
    override val name = SET_CURSOL_JUST_TAG
}

const val CLR_CUSROL_JUST_CODE: Byte = 0x73
const val CLR_CUSROL_JUST_TAG = "CLR_CUSROL_JUST"
class Clr_Cusrol_JustProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CLR_CUSROL_JUST_CODE
    override val name = CLR_CUSROL_JUST_TAG
}

const val CUT_ARTICLE_CODE: Byte = 0x74
const val CUT_ARTICLE_TAG = "CUT_ARTICLE"
class Cut_ArticleProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CUT_ARTICLE_CODE
    override val name = CUT_ARTICLE_TAG
}

const val CAPITAL_CODE: Byte = 0x75
const val CAPITAL_TAG = "CAPITAL"
class CapitalProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = CAPITAL_CODE
    override val name = CAPITAL_TAG
}

const val AMPM_CODE: Byte = 0x76
const val AMPM_TAG = "AMPM"
class AmpmProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    override val code = AMPM_CODE
    override val name = AMPM_TAG
}
/* TODO: Placeholder for 0x77: NextMsgProc */
/* TODO: Placeholder for 0x78: NextMsgProc */
/* TODO: Placeholder for 0x79: SelectStringProc */
/* TODO: Placeholder for 0x7a: SelectStringProc */


/*
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
