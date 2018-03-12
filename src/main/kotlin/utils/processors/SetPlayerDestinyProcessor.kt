package utils.processors

import models.StringTableEntry

abstract class SetPlayerDestinyProcessor(targetEntry: StringTableEntry): PlaceholderProcessor(targetEntry) {
    abstract val destiny: Int
}