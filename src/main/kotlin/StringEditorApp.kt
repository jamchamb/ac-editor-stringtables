import controllers.StringTableController
import javafx.scene.control.Label
import javafx.scene.control.SelectionMode
import javafx.stage.Stage
import models.StringTableEntry
import tornadofx.*
import views.StringTableChooser
import views.StringTableList
import kotlin.system.exitProcess


class StringEditorApp: App(MyView::class)

val forest1stDir = "D:\\ACHax\\forest_1st.d\\data"
val forest2ndDir = "D:\\ACHax\\forest_2nd.d\\data"

class MyView: View() {
    val controller: StringTableController by inject()

    override val root = borderpane {
        top(TopView::class)
        center = vbox {
            hbox {
                // String selection list
                val myList = add<StringTableList>()

                // String editing pane
                vbox {
                    label("Editor area")
                    val editorTextArea = textarea(controller.selectedStringTableEntry.content)
                }
            }
        }
        bottom(BottomView::class)
    }
}

class TopView: View() {
    override val root = menubar {
        menu ("File") {
            item("Open", "Shortcut+O") {
                action {
                    /*
                    val filters = arrayOf(
                        FileChooser.ExtensionFilter("String table", "*.bin")
                    )
                    val files: List<File> = chooseFile("String table", filters, FileChooserMode.Single)
                    println("User chose ${files[0]}")
                    */
                    find<StringTableChooser> {
                        openModal()
                    }
                }
            }
            item("Save", "Shortcut+S")
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
