package utils.processors

import models.StringTableEntry

abstract class PlaceholderProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val size = 2

    override fun decodeImpl(bytes: List<Byte>): String {
        return ""
    }

    override fun encode(text: String): ByteArray {
        return listOf(PROC_CODE, this.code).toByteArray()
    }
}