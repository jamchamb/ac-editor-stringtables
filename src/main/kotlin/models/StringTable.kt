package models

import tornadofx.FXTask
import java.io.*

class StringTable {
    var endingsTableSlots: Int = 0
    var endingsEmptySlots: Int = 0
    var dataFileSize: Long = 0
    var entries = ArrayList<StringTableEntry>()

    fun loadTableFromFiles (tablePath: String, dataPath: String) {
        loadTableFromFiles(tablePath, dataPath, null)
    }

    fun loadTableFromFiles (tablePath: String, dataPath: String, task: FXTask<*>?) {
        entries.clear()

        // If running in a task, update the progress
        task?.updateTitle("Loading String Table")

        val tableInputStream = File(tablePath).inputStream()
        val tableBufferedInputStream = BufferedInputStream(tableInputStream)
        val tableDataInputStream = DataInputStream(tableBufferedInputStream)

        val endingsTable = ArrayList<Int>()

        val tableDataSize = tableInputStream.channel.size() / 4
        var workDone = 0

        // Read big endian ints from end position table
        while (tableDataInputStream.available() > 0) {
            val endingPos = tableDataInputStream.readInt()
            // TODO Make sure these read as unsigned 32 bit ints
            if (endingPos < 0) {
                error("Negative ending position: $endingPos")
            }
            endingsTable.add(endingPos)

            // Update task progress
            workDone += 1
            task?.updateProgress(workDone.toLong(), tableDataSize)
            task?.updateMessage("table: %.2f%%".format((workDone.toDouble() / tableDataSize) * 100))
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

        // New task progress
        workDone = 0

        var pos = 0
        for ((index, endingPos) in endingsTable.withIndex()) {
            if (pos > 0 && endingPos == 0) {
                // Zero values are used for empty entries at the end
                break
            } else if (endingPos < pos) {
                throw IllegalArgumentException("File string positions not in ascending order ($endingPos < $pos)")
            }

            // Get the string bytes
            val length = endingPos - pos
            val byteArray = ByteArray(length)
            dataDataInputStream.read(byteArray)

            // Create new entry with raw bytes
            entries.add(StringTableEntry(index, byteArray))

            pos = endingPos

            // Update task progress
            workDone += 1
            task?.updateProgress(workDone.toLong(), endingsTable.size.toLong())
            task?.updateMessage("messages: %.2f%%".format((workDone.toDouble() / endingsTable.size) * 100))
        }

        task?.updateProgress(1.0, 1.0)
        task?.updateMessage("complete")

        dataInStream.close()
        dataBufferedInStream.close()
        dataDataInputStream.close()

        // Save total number of end position slots. Warn user if total number of slots or number of used slots increases.
        endingsTableSlots = endingsTable.size
        endingsEmptySlots = endingsTable.size - entries.size
    }

    fun saveTableToFiles(tablePath: String, dataPath: String) {
        saveTableToFiles(tablePath, dataPath, null)
    }

    fun saveTableToFiles(tablePath: String, dataPath: String, task: FXTask<*>?) {
        task?.updateTitle("Saving String Table")

        // Collect data to write to files
        var endPosition = 0

        val endingsTable = ArrayList<Int>(endingsTableSlots)
        val dataTable = ArrayList<Byte>(entries.size)

        // Encode and add string entries
        for ((index, entry) in entries.withIndex()) {
            val entryBytes = entry.encodeMessage()
            endPosition += entryBytes.size
            endingsTable.add(endPosition)
            dataTable.addAll(entryBytes.toList())

            // Update progress bar
            task?.updateProgress(index.toLong(), entries.size.toLong() + endingsEmptySlots)
            task?.updateMessage("encoding message $index/${entries.size + endingsEmptySlots}")
        }

        // Add filler data
        var i = 0
        while (i < endingsEmptySlots) {
            endingsTable.add(0)
            i++
        }

        if (dataTable.size > dataFileSize) {
            println("WARNING: New data file size (${dataTable.size}) exceeds original ($dataFileSize)")
        } else {
            while (dataTable.size < dataFileSize) {
                dataTable.add(0x00.toByte())
            }
        }

        // Encoding progress complete
        task?.updateProgress(1.0, 1.0)
        task?.updateMessage("writing files")

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
