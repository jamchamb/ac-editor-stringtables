package models

class StringTable: ArrayList<StringTableEntry>() {
    var endingsTableSlots: Int = 0
    var endingsEmptySlots: Int = 0
    var dataFileSize: Long = 0
}
