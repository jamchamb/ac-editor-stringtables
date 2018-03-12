package utils.processors

import models.StringTableEntry

enum class MessageContentsSetting(val id: Int) {
    NORMAL(0),
    ANGRY(1),
    SAD(2),
    FUN(3),
    SLEEPY(4),
    GLOOMY(5)
}

abstract class SetMessageContentsProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    abstract val contentsSound: MessageContentsSetting
}
