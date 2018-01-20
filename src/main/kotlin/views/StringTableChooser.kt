package views

import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class StringTableChooser: View("Open string table") {

    override val root = form {
        vbox {

            // Pick string table file
            hbox {
                val tableFileField = textfield { "String table file" }
                button("Select file") {
                    action {
                        val filters = arrayOf(FileChooser.ExtensionFilter("String table file", "*.bin"))
                        var files = chooseFile("Select string table file", filters, FileChooserMode.Single)
                        println(files[0])
                        tableFileField.text = files[0].canonicalPath
                    }
                }
            }

            // Pick string data file
            hbox {
                var dataFileField = textfield("String data file")
                button("Select file") {
                    action {
                        val filters = arrayOf(FileChooser.ExtensionFilter("String data file", "*.bin"))
                        var files = chooseFile("Select string data file", filters, FileChooserMode.Single)
                        println(files[0])
                        dataFileField.text = files[0].canonicalPath
                    }
                }
            }

        }
    }

}
