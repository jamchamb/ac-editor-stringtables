package views

import controllers.StringTableController
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

/** Get the filename of the complementary string table file.
 * For the table file (*_data_table.bin), return the data filename (*_data.bin)
 */
fun getPairFilename(file: File): String {
    val filename = file.canonicalPath.substringAfterLast(File.separatorChar)
    var pairName = ""

    if (filename.endsWith("_data_table.bin")) {
        pairName = filename.replace("_data_table.bin", "_data.bin")
    } else if (filename.endsWith("_data.bin")) {
        pairName = filename.replace("_data.bin", "_data_table.bin")
    } else {
        throw IllegalArgumentException("Filename must end with '_table.bin' or '.bin'")
    }

    return "${file.parentFile.canonicalPath}${File.separator}$pairName"
}

class StringTableChooser: View("Open String Table") {

    var chosenTableFile = ""
    var chosenDataFile = ""

    var tableFileField: TextField by singleAssign()
    var dataFileField: TextField by singleAssign()

    val controller: StringTableController by inject()

    override val root = form {
        vbox {

            // Pick string table file
            hbox {
                textfield() {
                    tableFileField = this
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

                            // Also set complementary data file automatically, if possible
                            try {
                                val dataFilename = getPairFilename(files[0])
                                dataFileField.text = dataFilename
                                chosenDataFile = dataFilename
                            } catch (iae: IllegalArgumentException) {
                                System.err.println("Could not get automatic complementary filename")
                            }
                        }
                    }
                }
            }

            // Pick string data file
            hbox {
                textfield {
                    dataFileField = this
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
