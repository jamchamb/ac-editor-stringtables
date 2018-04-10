package utils.processors

import models.StringTableEntry
import utils.bytesToInt
import utils.decodeHexASCII

class SetNextMessageRandomSectionProcessor(targetEntry: StringTableEntry): MessageProcessor(targetEntry) {
    override val code = NEXT_MSG_RANDOM_SECTION_CODE
    override val name = NEXT_MSG_RANDOM_SECTION_TAG
    override val size = 6

    // Message IDs for start and end of section to choose from
    var sectionStartId = 0
    var sectionEndId = 0

    override fun decodeImpl(bytes: List<Byte>): String {
        sectionStartId = bytesToInt(bytes.slice(2 until 4))
        sectionEndId = bytesToInt(bytes.slice(4 until 6))
        return "0x%04x:0x%04x".format(sectionStartId, sectionEndId)
    }

    override fun encodeImpl(textParts: List<String>): List<Byte>? {
        val unhexStartId = decodeHexASCII(textParts[0], 2)
        val unhexEndId = decodeHexASCII(textParts[1], 2)
        return unhexStartId + unhexEndId
    }
}