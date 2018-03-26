package controllers

import javafx.collections.FXCollections
import models.StringTable
import models.StringTableEntry
import tornadofx.Controller
import utils.loadTableFromFiles

class StringTableController: Controller() {
    var stringTable = StringTable()
    val stringTableEntries = FXCollections.observableArrayList<StringTableEntry>()
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
