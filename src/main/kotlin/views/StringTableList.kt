package views

import controllers.StringTableController
import models.StringTableEntry
import tornadofx.*

class StringTableList: Fragment() {
    private val controller: StringTableController by inject()

    override val root = tableview(controller.stringTableEntries) {
        isEditable = false
        column("ID", StringTableEntry::idProperty)
        column("Content", StringTableEntry::contentProperty)
        onDoubleClick {
            if (selectedItem != null) {
                fire(SelectedEntryEvent(selectedItem!!))
            }
        }
        smartResize()
    }
}
