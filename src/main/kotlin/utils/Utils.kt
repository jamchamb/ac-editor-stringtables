package utils

import com.sun.javaws.exceptions.InvalidArgumentException
import models.StringTable
import models.StringTableEntry
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
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

fun loadTableFromFiles (tablePath: String, dataPath: String): StringTable {
    val inputStream = File(tablePath).inputStream()
    val bufferedInputStream = BufferedInputStream(inputStream)
    val tableDataInputStream = DataInputStream(bufferedInputStream)

    val endingsTable = ArrayList<Int>()
    var numTableEntries = 0

    // Read big endian ints from end position table
    while (tableDataInputStream.available() > 0) {
        endingsTable.add(tableDataInputStream.readInt())
        numTableEntries++
    }

    inputStream.close()
    bufferedInputStream.close()
    tableDataInputStream.close()

    // Set up a table object
    val result = StringTable()
    result.tableFileSize = numTableEntries * 4

    // Read in the string data
    val dataInStream = File(dataPath).inputStream()
    val dataBufferedInStream = BufferedInputStream(dataInStream)
    val dataDataInputStream = DataInputStream(dataBufferedInStream)

    var pos = 0
    for ((index, endingPos) in endingsTable.withIndex()) {
        if (pos > 0 && endingPos == 0) break
        else if (endingPos < pos) {
            throw InvalidArgumentException(arrayOf("File string positions not in ascending order"))
        }

        // Get the string bytes
        val length = endingPos - pos
        val byteArray = ByteArray(length)
        dataDataInputStream.read(byteArray)

        // Create new entry with raw bytes
        result.add(StringTableEntry(index, byteArray))

        pos = endingPos
    }

    dataInStream.close()
    dataBufferedInStream.close()
    dataDataInputStream.close()

    return result
}