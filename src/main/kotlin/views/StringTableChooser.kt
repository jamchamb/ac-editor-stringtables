package views

import controllers.StringTableController
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

const val TABLE_END = "_data_table.bin"
const val DATA_END = "_data.bin"

abstract class StringTableChooser (private val action: String): View() {

    /** Get the filename of the complementary string table file.
     * For the table file (*_data_table.bin), return the data filename (*_data.bin)
     */
    private fun getPairFilename(file: File): String {
        val filename = file.canonicalPath.substringAfterLast(File.separatorChar)
        val pairName: String

        pairName = when {
            filename.endsWith(TABLE_END) -> filename.replace(TABLE_END, DATA_END)
            filename.endsWith(DATA_END) -> filename.replace(DATA_END, TABLE_END)
            else -> throw IllegalArgumentException("Filename must end with '$TABLE_END' or '$DATA_END'")
        }

        return "${file.parentFile.canonicalPath}${File.separator}$pairName"
    }

    protected val controller: StringTableController by inject()

    var chosenTableFile = ""
    var chosenDataFile = ""

    var tableFileField: TextField by singleAssign()
    var dataFileField: TextField by singleAssign()

    abstract fun performAction()
    abstract val fileChooserMode: FileChooserMode

    init {
        title = "$action String Table"
    }

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
                        val files = chooseFile("Select string table file", filters, fileChooserMode)
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
                        val files = chooseFile("Select string data file", filters, fileChooserMode)
                        if (files.size > 0) {
                            println("Selected string data file $files[0]")
                            chosenDataFile = files[0].canonicalPath
                            dataFileField.text = chosenDataFile
                        }
                    }
                }
            }


            button("$action string table") {
                action {
                    println("$action string table...")

                    if (chosenTableFile.isBlank() or chosenDataFile.isBlank()) {
                        alert(Alert.AlertType.ERROR, "Error in choosing table", "Both file paths must be set", ButtonType.OK)
                        return@action
                    }

                    // Perform the action
                    runAsync {
                        performAction()
                    } ui {
                        close()
                    }
                }
            }
        }
    }

}
