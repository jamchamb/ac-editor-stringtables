package utils.processors

import models.StringTableEntry

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