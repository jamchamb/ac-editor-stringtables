package views

import tornadofx.FXTask
import tornadofx.FileChooserMode

class StringTableSaveChooser: StringTableChooser("Save") {
    override val fileChooserMode = FileChooserMode.Save
    override fun performAction(task: FXTask<*>) {
        controller.saveTable(chosenTableFile, chosenDataFile, task)
    }
}