package models

import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.property

class StringTableEntry (id: Int, content: String) {
    var id by property(id)
    fun idProperty() = getProperty(StringTableEntry::id)

    var content by property(content)
    fun contentProperty() = getProperty(StringTableEntry::content)

    override fun toString (): String {
        return "${id}: ${content}"
    }
}

class StringTableEntryModel(): ItemViewModel<StringTableEntry>() {
    val id = bind { item?.idProperty() }
    val content = bind { item?.contentProperty() }
}