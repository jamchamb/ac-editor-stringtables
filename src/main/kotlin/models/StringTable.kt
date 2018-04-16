package models

import com.sun.javaws.exceptions.InvalidArgumentException
import java.io.*

class StringTable {
    var endingsTableSlots: Int = 0
    var endingsEmptySlots: Int = 0
    var dataFileSize: Long = 0
    var entries = ArrayList<StringTableEntry>()

    fun loadTableFromFiles (tablePath: String, dataPath: String) {
        entries.clear()

        val tableInputStream = File(tablePath).inputStream()
        val tableBufferedInputStream = BufferedInputStream(tableInputStream)
        val tableDataInputStream = DataInputStream(tableBufferedInputStream)

        val endingsTable = ArrayList<Int>()

        // Read big endian ints from end position table
        while (tableDataInputStream.available() > 0) {
            val endingPos = tableDataInputStream.readInt()
            // TODO Make sure these read as unsigned 32 bit ints
            if (endingPos < 0) {
                error("Negative ending position: $endingPos")
            }
            endingsTable.add(endingPos)
        }

        tableInputStream.close()
        tableBufferedInputStream.close()
        tableDataInputStream.close()

        // Read in the string data
        val dataInStream = File(dataPath).inputStream()
        val dataBufferedInStream = BufferedInputStream(dataInStream)
        val dataDataInputStream = DataInputStream(dataBufferedInStream)

        // Get total size of data file. Warn the user if this is exceeded upon save.
        dataFileSize = dataInStream.channel.size()

        var pos = 0
        for ((index, endingPos) in endingsTable.withIndex()) {
            if (pos > 0 && endingPos == 0) {
                // Zero values are used for empty entries at the end
                break
            } else if (endingPos < pos) {
                throw InvalidArgumentException(arrayOf("File string positions not in ascending order ($endingPos < $pos)"))
            }

            // Get the string bytes
            val length = endingPos - pos
            val byteArray = ByteArray(length)
            dataDataInputStream.read(byteArray)

            // Create new entry with raw bytes
            entries.add(StringTableEntry(index, byteArray))

            pos = endingPos
        }

        dataInStream.close()
        dataBufferedInStream.close()
        dataDataInputStream.close()

        // Save total number of end position slots. Warn user if total number of slots or number of used slots increases.
        endingsTableSlots = endingsTable.size
        endingsEmptySlots = endingsTable.size - entries.size
    }

    fun saveTableToFiles(tablePath: String, dataPath: String) {
        // Collect data to write to files
        var endPosition = 0

        val endingsTable = ArrayList<Int>(endingsTableSlots)
        val dataTable = ArrayList<Byte>(entries.size)

        // Testing encoders
        for (entry in entries) {
            val entryBytes = entry.encodeMessage()
            endPosition += entryBytes.size
            endingsTable.add(endPosition)
            dataTable.addAll(entryBytes.toList())
        }

        // Add filler data
        var i = 0
        while (i < endingsEmptySlots) {
            endingsTable.add(0)
            i++
        }

        while (dataTable.size < dataFileSize) {
            dataTable.add(0x00.toByte())
        }

        // Table output stream
        val tableOutputStream = File(tablePath).outputStream()
        val tableBufferedOutputStream = BufferedOutputStream(tableOutputStream)
        val tableDataOutputStream = DataOutputStream(tableBufferedOutputStream)

        // Data output stream
        val dataOutputStream = File(dataPath).outputStream()
        val dataBufferedOutputStream = BufferedOutputStream(dataOutputStream)

        // Write the table
        for (ending in endingsTable) {
            tableDataOutputStream.writeInt(ending)
        }

        // Write the data
        dataBufferedOutputStream.write(dataTable.toByteArray())

        // Close data file
        dataBufferedOutputStream.close()
        dataOutputStream.close()

        // Close table file
        tableDataOutputStream.close()
        tableBufferedOutputStream.close()
        tableOutputStream.close()
    }
}
