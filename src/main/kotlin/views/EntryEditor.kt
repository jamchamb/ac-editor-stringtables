package views

import controllers.StringTableController
import javafx.scene.layout.Priority
import tornadofx.*

class EntryEditor: View() {

    private val controller: StringTableController by inject()

    override val root = vbox {
        hboxConstraints {
            hGrow = Priority.ALWAYS
        }

        form {
            fieldset("String table entry") {
                field("Text") {
                    val editorTextArea = textarea(controller.selectedStringTableEntry.content) {
                        vboxConstraints {
                            vGrow = Priority.ALWAYS
                        }
                        hboxConstraints {
                            hGrow = Priority.ALWAYS
                        }
                    }
                }
            }
        }
    }
}