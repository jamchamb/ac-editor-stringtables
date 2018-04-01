package utils.processors

import models.StringTableEntry
import org.apache.commons.codec.binary.Hex

const val EIGHTY_TAG = "0X80"
class EightyProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code: Byte = 0x00
    override val name = EIGHTY_TAG
    override val size = 2

    var data: Byte = 0x00

    override fun makeHeader(): List<Byte> {
        return listOf(0x80.toByte())
    }

    override fun decodeImpl(bytes: List<Byte>): String {
        data = bytes[1]
        return "%02x".format(data)
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        return Hex.decodeHex(textParts[0]).toList()
    }
}