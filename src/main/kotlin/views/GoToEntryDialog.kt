package views

import controllers.StringTableController
import javafx.scene.control.TextField
import tornadofx.*

class GoToEntryDialog: Fragment("Go To Entry") {
    private val controller: StringTableController by inject()
    private var idField: TextField by singleAssign()

    private fun openEntry() {
        try {
            val selectionId = when {
                idField.text.startsWith("0x", true) -> idField.text.substring(2).toInt(16)
                else -> idField.text.toInt()
            }

            if (controller.stringTableEntries.size > selectionId) {
                fire(SelectedEntryEvent(controller.stringTableEntries[selectionId]))
                idField.clear()
                close()
            } else {
                error("No such entry")
            }
        } catch (e: NumberFormatException) {
            error("Invalid number (use 0x prefix for hexadecimal)")
            println(e.message)
        }
    }

    override val root = borderpane  {
        paddingAll = 8.0
        center {
            hbox(4.0) {
                label("ID:")

                idField = textfield {
                    promptText = "Entry ID"
                    action {
                        openEntry()
                    }
                    requestFocus()
                }

                button("Go to entry") {
                    action {
                        openEntry()
                    }
                }
            }
        }
    }
}