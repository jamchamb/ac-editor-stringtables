package views

import tornadofx.FileChooserMode

class StringTableOpenChooser: StringTableChooser() {
    override val fileChooserMode = FileChooserMode.Single
    override val action = "Open"
    override fun performAction() {
        controller.loadTable(chosenTableFile, chosenDataFile)
    }
}