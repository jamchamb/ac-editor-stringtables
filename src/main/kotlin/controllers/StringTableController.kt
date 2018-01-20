package controllers

import com.sun.javaws.exceptions.InvalidArgumentException
import models.StringTable
import models.StringTableEntry
import tornadofx.Controller
import java.io.*

class StringTableController: Controller() {

    fun loadTable (tablePath: String, dataPath: String): StringTable {
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

            // Get the string
            val length = endingPos - pos
            val byteArray = ByteArray(length)
            dataDataInputStream.read(byteArray)

            val strFromBytes = String(byteArray)
            result.add(StringTableEntry(index, strFromBytes))

            pos = endingPos
        }

        dataInStream.close()
        dataBufferedInStream.close()
        dataDataInputStream.close()

        return result
    }

}
