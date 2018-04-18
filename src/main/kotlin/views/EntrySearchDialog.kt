package views

import controllers.StringTableController
import javafx.collections.FXCollections
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import models.StringTableEntry
import tornadofx.*

class EntrySearchDialog: Fragment() {
    private val controller: StringTableController by inject()

    private val searchResults = FXCollections.observableArrayList<StringTableEntry>()
    private var searchField: TextField by singleAssign()

    private fun doSearch(query: String) {
        searchResults.clear()

        for (entry in controller.stringTableEntries) {
            if (entry.content.contains(query, true)) {
                searchResults.add(entry)
            }
        }
    }

    override val root = BorderPane()

    init {
        title = "Search entries"

        with(root) {
            prefWidth = 600.0
            prefHeight = 400.0

            top {
                form {
                    searchField = textfield {
                        action {
                            doSearch(text)
                        }
                    }

                    button("Search") {
                        action {
                            doSearch(searchField.text)
                        }
                    }
                }
            }

            center {
                tableview(searchResults) {
                    isEditable = false
                    column("ID", StringTableEntry::idProperty)
                    column("Content", StringTableEntry::contentProperty)
                    onDoubleClick {
                        if (selectedItem != null) {
                            fire(SelectedEntryEvent(selectedItem!!))
                        }
                    }
                    smartResize()
                }
            }
        }
    }
}