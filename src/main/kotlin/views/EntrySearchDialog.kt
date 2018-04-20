package views

import controllers.StringTableController
import javafx.collections.FXCollections
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import models.StringTableEntry
import tornadofx.*

class EntrySearchDialog: View() {
    private val controller: StringTableController by inject()

    private val searchResults = FXCollections.observableArrayList<StringTableEntry>()
    private var searchField: TextField by singleAssign()
    private var regexCheckbox: CheckBox by singleAssign()

    private fun doSearch() {
        val query = searchField.text
        val asRegex = regexCheckbox.isSelected
        var regex: Regex? = if (asRegex) Regex(query) else null

        searchResults.clear()

        if (query.isEmpty()) return

        for (entry in controller.stringTableEntries) {
            if ((asRegex && entry.content.contains(regex!!)) || (entry.content.contains(query, true))) {
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
                            doSearch()
                        }
                    }

                    regexCheckbox = checkbox ("Regex")

                    button("Search") {
                        action {
                            doSearch()
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