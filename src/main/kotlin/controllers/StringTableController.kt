package controllers

import com.sun.javaws.exceptions.InvalidArgumentException
import javafx.collections.FXCollections
import models.StringTable
import models.StringTableEntry
import tornadofx.Controller
import java.io.*

class StringTableController: Controller() {
    var stringTable = StringTable()
    val stringTableEntries = FXCollections.observableArrayList<StringTableEntry>()
    var tableChanged = false

    fun loadTable (tablePath: String, dataPath: String) {
        val result: StringTable = loadTableFromFiles(tablePath, dataPath)
        // TODO hacky
        stringTable.endingsTableSlots = result.endingsTableSlots
        stringTable.dataFileSize = result.dataFileSize
        stringTable.endingsEmptySlots = result.endingsEmptySlots
        stringTableEntries.clear()
        stringTableEntries.addAll(result)
    }

    fun saveTable (tablePath: String, dataPath: String) {
        saveTableToFiles(tablePath, dataPath)
    }

    fun closeTable () {
        if (tableChanged) {
            println("WARNING: Table has unsaved changes!")
        }
        stringTable = StringTable()
        stringTableEntries.clear()
    }

    private fun loadTableFromFiles (tablePath: String, dataPath: String): StringTable {
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

        // Set up a table object
        val result = StringTable()

        // Read in the string data
        val dataInStream = File(dataPath).inputStream()
        val dataBufferedInStream = BufferedInputStream(dataInStream)
        val dataDataInputStream = DataInputStream(dataBufferedInStream)

        // Get total size of data file. Warn the user if this is exceeded upon save.
        result.dataFileSize = dataInStream.channel.size()

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
            result.add(StringTableEntry(index, byteArray))

            pos = endingPos
        }

        dataInStream.close()
        dataBufferedInStream.close()
        dataDataInputStream.close()

        // Save total number of end position slots. Warn user if total number of slots or number of used slots increases.
        result.endingsTableSlots = endingsTable.size
        result.endingsEmptySlots = endingsTable.size - result.size

        return result
    }

    private fun saveTableToFiles(tablePath: String, dataPath: String) {
        // Collect data to write to files
        var endPosition = 0

        val endingsTable = ArrayList<Int>(stringTable.endingsTableSlots)
        val dataTable = ArrayList<Byte>(stringTableEntries.size)

        // Testing encoders
        for (entry in stringTableEntries) {
            val entryBytes = entry.encodeMessage()
            endPosition += entryBytes.size
            endingsTable.add(endPosition)
            dataTable.addAll(entryBytes.toList())
        }

        // Add filler data
        var i = 0
        while (i < stringTable.endingsEmptySlots) {
            endingsTable.add(0)
            i++
        }

        while (dataTable.size < stringTable.dataFileSize) {
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
