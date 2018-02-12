package controllers

import com.sun.javaws.exceptions.InvalidArgumentException
import javafx.collections.FXCollections
import loadTableFromFiles
import models.StringTable
import models.StringTableEntry
import models.StringTableEntryModel
import tornadofx.Controller
import java.io.*

class StringTableController: Controller() {
    val stringTable = StringTable()
    val stringTableEntries = FXCollections.observableArrayList<StringTableEntry>()
    val selectedStringTableEntry = StringTableEntryModel()

    fun loadTable (tablePath: String, dataPath: String) {
        val result: StringTable = loadTableFromFiles(tablePath, dataPath)
        // TODO hacky
        stringTable.tableFileSize = result.tableFileSize
        stringTable.dataFileSize = result.dataFileSize
        stringTableEntries.clear()
        stringTableEntries.addAll(result)
    }

    init {

    }

}
