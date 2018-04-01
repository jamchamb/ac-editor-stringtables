package utils.processors

import models.StringTableEntry
import utils.bytesToInt
import utils.decodeHexASCII

abstract class SetNextMessageProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {

    override val size = 4
    abstract val messageSlot: Int

    private var messageId: Int = 0

    override fun decodeImpl(bytes: List<Byte>): String {
        messageId = bytesToInt(bytes.slice(2..3))
        return "0x%04x".format(messageId)
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        return decodeHexASCII(textParts[0], 2)
    }
}