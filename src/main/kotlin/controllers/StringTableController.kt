package controllers

import javafx.collections.FXCollections
import utils.loadTableFromFiles
import models.StringTable
import models.StringTableEntry
import models.StringTableEntryModel
import tornadofx.Controller

class StringTableController: Controller() {
    var stringTable = StringTable()
    val stringTableEntries = FXCollections.observableArrayList<StringTableEntry>()
    val selectedStringTableEntry = StringTableEntryModel()
    var tableChanged = false

    fun loadTable (tablePath: String, dataPath: String) {
        val result: StringTable = loadTableFromFiles(tablePath, dataPath)
        // TODO hacky
        stringTable.tableFileSize = result.tableFileSize
        stringTable.dataFileSize = result.dataFileSize
        stringTableEntries.clear()
        stringTableEntries.addAll(result)
    }

    fun closeTable () {
        if (tableChanged) {
            println("WARNING: Table has unsaved changes!")
        }
        stringTable = StringTable()
        stringTableEntries.clear()
    }

    init {

    }

}
