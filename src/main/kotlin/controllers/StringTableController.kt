package controllers

import javafx.collections.FXCollections
import models.StringTable
import models.StringTableEntry
import tornadofx.Controller
import tornadofx.FXTask
import tornadofx.getProperty
import tornadofx.property

class StringTableController: Controller() {
    var stringTable = StringTable()
    val stringTableEntries = FXCollections.observableArrayList<StringTableEntry>()
    var tableChanged = false

    var tableFilePath: String by property()
    fun tableFilePathProperty() = getProperty(StringTableController::tableFilePath)

    var dataFilePath: String by property()
    fun dataFilePathProperty() = getProperty(StringTableController::dataFilePath)

    init {
        tableFilePath = ""
        dataFilePath = ""
    }

    fun loadTable (tablePath: String, dataPath: String, fxTask: FXTask<*>) {
        stringTable.loadTableFromFiles(tablePath, dataPath, fxTask)
    }

    fun updateObservableEntries () {
        stringTableEntries.clear()
        stringTableEntries.addAll(stringTable.entries)
    }

    fun saveTable (tablePath: String, dataPath: String, fxTask: FXTask<*>) {
        stringTable.entries.clear()
        stringTable.entries.addAll(stringTableEntries)
        stringTable.saveTableToFiles(tablePath, dataPath, fxTask)
    }

    fun closeTable () {
        if (tableChanged) {
            // TODO: Display warning & confirm
            println("WARNING: Table has unsaved changes!")
        }
        stringTable = StringTable()
        stringTableEntries.clear()
        tableFilePath = ""
        dataFilePath = ""
    }
}
