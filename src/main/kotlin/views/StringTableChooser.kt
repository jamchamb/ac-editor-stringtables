package views

import controllers.StringTableController
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class StringTableChooser: View("Open String Table") {

    var chosenTableFile = ""
    var chosenDataFile = ""

    val controller: StringTableController by inject()

    override val root = form {
        vbox {

            // Pick string table file
            hbox {
                val tableFileField = textfield() {
                    title = "String table file"
                    isEditable = false
                }
                button("Select file") {
                    action {
                        val filters = arrayOf(FileChooser.ExtensionFilter("String table file", "*.bin"))
                        val files = chooseFile("Select string table file", filters, FileChooserMode.Single)
                        if (files.size > 0) {
                            println("Selected table file $files[0]")
                            chosenTableFile = files[0].canonicalPath
                            tableFileField.text = chosenTableFile
                        }
                    }
                }
            }

            // Pick string data file
            hbox {
                val dataFileField = textfield {
                    title = "String data file"
                    isEditable = false
                }
                button("Select file") {
                    action {
                        val filters = arrayOf(FileChooser.ExtensionFilter("String data file", "*.bin"))
                        val files = chooseFile("Select string data file", filters, FileChooserMode.Single)
                        if (files.size > 0) {
                            println("Selected string data file $files[0]")
                            chosenDataFile = files[0].canonicalPath
                            dataFileField.text = chosenDataFile
                        }
                    }
                }
            }

            button("Load string table") {
                action {
                    println("Load string table...")

                    if (chosenTableFile.isBlank() or chosenDataFile.isBlank()) {
                        alert(Alert.AlertType.ERROR, "Error loading table", "Both file paths must be set", ButtonType.OK)
                        return@action
                    }

                    // Load up the files woohoo
                    controller.loadTable(chosenTableFile, chosenDataFile)
                    close()
                }
            }

        }
    }

}
