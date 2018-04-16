package views

import tornadofx.FileChooserMode

class StringTableSaveChooser: StringTableChooser("Save") {
    override val fileChooserMode = FileChooserMode.Save
    override fun performAction() {
        controller.saveTable(chosenTableFile, chosenDataFile)
    }
}