package views

import controllers.StringTableController
import tornadofx.*
import kotlin.system.exitProcess

class MenuBarView: View() {
    private val controller: StringTableController by inject()

    override val root = menubar {
        menu ("File") {
            item("Open", "Shortcut+O") {
                action {
                    find<StringTableOpenChooser> {
                        openModal()
                    }
                }
            }
            item("Save", "Shortcut+S") {
                action {
                    find<StringTableSaveChooser> {
                        openModal()
                    }
                }
            }
            item("Close") {
                action {
                    fire(TableCloseEvent())
                    controller.closeTable()
                }
            }
            item("Quit", "Shortcut+Q") {
                action {
                    exitProcess(0)
                }
            }
        }

        menu ("Edit") {
            item ("Go To Entry", "Shortcut+G") {
                action {
                    find<GoToEntryDialog> {
                        openModal()
                    }
                }
            }

            item ("Search", "Shortcut+R") {
                action {
                    find<EntrySearchDialog> {
                        openModal()
                    }
                }
            }
        }
    }
}