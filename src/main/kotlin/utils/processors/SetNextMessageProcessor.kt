package utils.processors

import models.StringTableEntry
import utils.byteList
import utils.bytesToInt

abstract class SetNextMessageProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {

    override val size = 4
    abstract val messageSlot: Int

    private var messageId: Int = 0

    override fun decode(bytes: List<Byte>): List<Byte> {
        super.decode(bytes)

        messageId = bytesToInt(bytes.slice(2..3))
        return byteList("%s:%04x".format(name, messageId))
    }

    override fun encode(bytes: List<Byte>): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}