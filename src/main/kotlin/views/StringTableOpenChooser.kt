package views

import tornadofx.FileChooserMode

class StringTableOpenChooser: StringTableChooser("Open") {
    override val fileChooserMode = FileChooserMode.Single
    override fun performAction() {
        controller.loadTable(chosenTableFile, chosenDataFile)
    }
}