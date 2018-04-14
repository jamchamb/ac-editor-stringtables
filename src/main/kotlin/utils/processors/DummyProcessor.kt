package utils.processors

import models.StringTableEntry
import utils.decodeHexASCII

abstract class DummyProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {

    override fun decodeImpl(bytes: List<Byte>): String {
        if (size == 2) return ""

        return "0x%02x".format(bytes.slice(2 until bytes.size))
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        if (size == 2) return null

        return decodeHexASCII(textParts[0], this.size - 2)
    }
}
