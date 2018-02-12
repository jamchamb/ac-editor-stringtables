import controllers.StringTableController
import javafx.beans.property.StringProperty
import javafx.scene.control.Label
import javafx.scene.control.SelectionMode
import javafx.scene.layout.Priority
import javafx.stage.Stage
import models.StringTableEntry
import tornadofx.*
import views.EntryEditor
import views.StringTableChooser
import views.StringTableList
import kotlin.system.exitProcess


class StringEditorApp: App(MyView::class)

class MyView: View() {
    val controller: StringTableController by inject()

    override val root = borderpane {
        top(TopView::class)

        // String selection list
        left = hbox {
            hboxConstraints { marginRight = 10.0 }
            add<StringTableList>()
        }

        center = vbox {
            add<EntryEditor>()
        }

        bottom(BottomView::class)
    }

    init {
        title = "ACGC String Editor"
    }
}

class TopView: View() {
    val controller: StringTableController by inject()

    override val root = menubar {
        menu ("File") {
            item("Open", "Shortcut+O") {
                action {
                    find<StringTableChooser> {
                        openModal()
                    }
                }
            }
            item("Save", "Shortcut+S")
            item("Close") {
                action {
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
