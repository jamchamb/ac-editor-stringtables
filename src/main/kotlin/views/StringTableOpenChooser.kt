package views

import tornadofx.FXTask
import tornadofx.FileChooserMode

class StringTableOpenChooser: StringTableChooser("Open") {
    override val fileChooserMode = FileChooserMode.Single

    override fun performAction(task: FXTask<*>) {
        controller.loadTable(chosenTableFile, chosenDataFile, task)
    }

    override fun postAction() {
        controller.updateObservableEntries()
        controller.tableFilePath = chosenTableFile
        controller.dataFilePath = chosenDataFile
    }
}