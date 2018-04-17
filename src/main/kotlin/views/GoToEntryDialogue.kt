package views

import controllers.StringTableController
import javafx.scene.control.TextField
import tornadofx.*

class GoToEntryDialogue: View("Go To Entry") {
    private val controller: StringTableController by inject()
    private var idField: TextField by singleAssign()

    private fun openEntry() {
        val selectionId = when {
            idField.text.startsWith("0x", true) -> idField.text.substring(2).toInt(16)
            else -> idField.text.toInt()
        }

        if (controller.stringTableEntries.size > selectionId) {
            fire(SelectedEntryEvent(controller.stringTableEntries[selectionId]))
            idField.clear()
            close()
        }
    }

    override val root = vbox (4.0) {
        idField = textfield {
            promptText = "Entry ID"
            action {
                openEntry()
            }
            requestFocus()
        }

        button ("Go to entry") {
            action {
                openEntry()
            }
        }
    }
}