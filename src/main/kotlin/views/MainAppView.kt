package views

import controllers.StringTableController
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import models.StringTableEntry
import models.StringTableEntryModel
import tornadofx.*

class SelectedEntryEvent(val selection: StringTableEntry): FXEvent()
class TableCloseEvent: FXEvent()

class MainAppView: View() {
    private val controller: StringTableController by inject()
    private var editorTabPane: TabPane by singleAssign()

    override val root = BorderPane()

    init {
        title = "ACGC String Editor"

        with (root) {
            prefWidth = 1300.0
            prefHeight = 700.0

            top(MenuBarView::class)

            // String selection list
            left = hbox {
                hboxConstraints { marginRight = 10.0 }
                add<StringTableList>()
            }

            center = vbox {
                minWidth = 700.0

                //add<EntryEditor>()
                editorTabPane = tabpane {
                    // Open an editor tab for a selected string table entry
                    subscribe<SelectedEntryEvent> { event ->
                        tab("Msg #${event.selection.id}") {
                            val newScope = Scope()
                            val model = StringTableEntryModel()
                            model.item = event.selection
                            setInScope(model, newScope)
                            add(find(EntryEditor::class, newScope))
                            select()
                        }
                    }

                    // Close tabs when file is closed
                    subscribe<TableCloseEvent> {
                        tabs.clear()
                    }
                }
            }

            bottom = Label("Bottom View")
        }
    }
}
