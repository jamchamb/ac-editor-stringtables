package views

import controllers.StringTableController
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import models.StringTableEntry
import models.StringTableEntryModel
import tornadofx.*
import kotlin.system.exitProcess

class SelectedEntryEvent(val selection: StringTableEntry): FXEvent()
class TableCloseEvent: FXEvent()

class MainAppView: View() {
    private val controller: StringTableController by inject()
    private var editorTabPane by singleAssign<TabPane>()

    override val root = borderpane {
        top(TopView::class)

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
                    // TODO
                }
            }
        }

        bottom(BottomView::class)
    }

    init {
        title = "ACGC String Editor"

        with (root) {
            prefWidth = 1300.0
            prefHeight = 700.0
        }
    }
}

class TopView: View() {
    private val controller: StringTableController by inject()

    override val root = menubar {
        menu ("File") {
            item("Open", "Shortcut+O") {
                action {
                    find<StringTableOpenChooser> {
                        openModal()
                    }
                }
            }
            item("Save", "Shortcut+S") {
                action {
                    find<StringTableSaveChooser> {
                        openModal()
                    }
                }
            }
            item("Close") {
                action {
                    fire(TableCloseEvent())
                    controller.closeTable()
                }
            }
            item("Quit", "Shortcut+Q") {
                action {
                    exitProcess(0)
                }
            }
        }
    }
}

class BottomView: View() {
    override val root = Label("Bottom View")
}
