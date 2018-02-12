package views

import controllers.StringTableController
import models.StringTableEntry
import tornadofx.*

class StringTableList: View() {
    val controller: StringTableController by inject()

    override val root = tableview(controller.stringTableEntries) {
        isEditable = false
        column("ID", StringTableEntry::idProperty)
        column("Content", StringTableEntry::contentProperty)
        bindSelected(controller.selectedStringTableEntry)
        smartResize()
    }
}