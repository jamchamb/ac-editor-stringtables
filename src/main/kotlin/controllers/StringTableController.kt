package controllers

import javafx.collections.FXCollections
import models.StringTable
import models.StringTableEntry
import tornadofx.Controller

class StringTableController: Controller() {
    var stringTable = StringTable()
    val stringTableEntries = FXCollections.observableArrayList<StringTableEntry>()
    var tableChanged = false

    fun loadTable (tablePath: String, dataPath: String) {
        // TODO Hacky
        stringTable.loadTableFromFiles(tablePath, dataPath)
        stringTableEntries.clear()
        stringTableEntries.addAll(stringTable.entries)
    }

    fun saveTable (tablePath: String, dataPath: String) {
        // TODO Hacky
        stringTable.entries.clear()
        stringTable.entries.addAll(stringTableEntries)
        stringTable.saveTableToFiles(tablePath, dataPath)
    }

    fun closeTable () {
        if (tableChanged) {
            println("WARNING: Table has unsaved changes!")
        }
        stringTable = StringTable()
        stringTableEntries.clear()
    }
}
