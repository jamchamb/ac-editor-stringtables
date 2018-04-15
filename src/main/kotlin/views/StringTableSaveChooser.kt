package views

import tornadofx.FileChooserMode

class StringTableSaveChooser: StringTableChooser() {
    override val fileChooserMode = FileChooserMode.Save
    override val action = "Save"
    override fun performAction() {
        controller.saveTable(chosenTableFile, chosenDataFile)
    }
}