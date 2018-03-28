package views

import controllers.StringTableController
import javafx.scene.layout.Priority
import models.StringTableEntryModel
import tornadofx.*

class EntryEditor: View() {

    private val controller: StringTableController by inject(DefaultScope)
    private val model: StringTableEntryModel by inject()

    override val root = vbox {
        hboxConstraints {
            hGrow = Priority.ALWAYS
        }
        vboxConstraints {
            vGrow = Priority.ALWAYS
        }

        form {
            fieldset("String table entry") {
                field("ID") {
                    textfield().bind(model.id)
                }

                field("Text") {
                    textarea(model.content)
                }

                button("Save") {
                    enableWhen(model.dirty)
                    action {
                        model.commit()
                    }
                }
                
                button("Reset") {
                    action {
                        model.rollback()
                    }
                }
            }
        }
    }
}