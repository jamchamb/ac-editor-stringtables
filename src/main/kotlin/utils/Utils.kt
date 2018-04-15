package utils

import org.apache.commons.codec.binary.Hex
import java.nio.ByteBuffer

fun byteList(input: String): List<Byte> {
    return input.toByteArray().asList()
}

fun bytesToInt(bytes: ByteArray): Int {
    return bytesToInt(bytes.asList())
}

fun bytesToInt(bytes: List<Byte>): Int {
    val allBytes: List<Byte>

    if (bytes.size > 4) {
        throw IllegalArgumentException("Got more than 4 bytes; 32-bit integer is the max")
    } else if (bytes.size < 4) {
        val leftPad = List(4 - bytes.size, {_ -> 0.toByte()})
        allBytes = leftPad + bytes
    } else {
        allBytes = bytes
    }

    val byteBuffer = ByteBuffer.wrap(allBytes.toByteArray())
    return byteBuffer.int
}

fun decodeHexASCII (text: String, numBytes: Int, requirePrefix: Boolean = true): List<Byte> {
    val startIndex = when (requirePrefix) {
        true -> 2
        false -> 0
    }

    if (requirePrefix) {
        if (!text.startsWith("0x", true)) error("Hex string missing 0x")
    }

    var result = Hex.decodeHex(text.substring(startIndex)).toList()
    if (result.size < numBytes) TODO("Not enough bytes; left pad this")

    return result
}

fun shiftJisDecode(bytes: List<Byte>): String {
    return String(bytes.toByteArray(), charset("Shift-JIS"))
}