package controllers

import javafx.collections.FXCollections
import models.StringTable
import models.StringTableEntry
import tornadofx.Controller
import tornadofx.FXTask

class StringTableController: Controller() {
    var stringTable = StringTable()
    val stringTableEntries = FXCollections.observableArrayList<StringTableEntry>()
    var tableChanged = false

    fun loadTable (tablePath: String, dataPath: String, fxTask: FXTask<*>) {
        // TODO Hacky
        stringTable.loadTableFromFiles(tablePath, dataPath, fxTask)
    }

    fun updateObservableEntries () {
        stringTableEntries.clear()
        stringTableEntries.addAll(stringTable.entries)
    }

    fun saveTable (tablePath: String, dataPath: String, fxTask: FXTask<*>) {
        // TODO Hacky
        stringTable.entries.clear()
        stringTable.entries.addAll(stringTableEntries)
        stringTable.saveTableToFiles(tablePath, dataPath, fxTask)
    }

    fun closeTable () {
        if (tableChanged) {
            println("WARNING: Table has unsaved changes!")
        }
        stringTable = StringTable()
        stringTableEntries.clear()
    }
}
