package models

import tornadofx.ItemViewModel

class StringTableEntryModel: ItemViewModel<StringTableEntry>() {
    val id = bind { item?.idProperty() }
    val content = bind { item?.contentProperty() }
    val rawBytes = bind { item?.rawBytesProperty() }
    val cancellable = bind { item?.cancellableProperty() }
}