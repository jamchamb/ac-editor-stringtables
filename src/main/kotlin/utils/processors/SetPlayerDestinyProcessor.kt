package utils.processors

import models.StringTableEntry

// TODO: Abstract destiny class to track destiny value
abstract class SetPlayerDestinyProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) 