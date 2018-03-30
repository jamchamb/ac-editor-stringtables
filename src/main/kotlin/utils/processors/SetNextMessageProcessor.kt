package utils.processors

import models.StringTableEntry
import utils.bytesToInt

abstract class SetNextMessageProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {

    override val size = 4
    abstract val messageSlot: Int

    private var messageId: Int = 0

    override fun decodeImpl(bytes: List<Byte>): String {
        messageId = bytesToInt(bytes.slice(2..3))
        return "%04x".format(messageId)
    }

    override fun encode(text: String): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}