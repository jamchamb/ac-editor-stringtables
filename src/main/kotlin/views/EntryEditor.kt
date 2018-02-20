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
                field("ID") {
                    textfield().bind(controller.selectedStringTableEntry.id)
                }

                field("Text") {
                    textarea(controller.selectedStringTableEntry.content) {
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