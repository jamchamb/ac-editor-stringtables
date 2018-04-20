package models

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

object StringTableSpec: Spek({
    given("a string table") {
        val stringTable = StringTable()
        on("loading a table") {
            val forest2ndDir = "D:\\ACHax\\forest_2nd.d\\data"

            stringTable.loadTableFromFiles("$forest2ndDir\\message_data_table.bin",
                    "$forest2ndDir\\message_data.bin")

            it("should load the table") {
                assertNotEquals(0, stringTable.entries.size)
            }
        }

        on("encoding a table without modifications") {
            it("should re-encode to the same raw bytes") {
                var failed = false
                for (entry in stringTable.entries) {
                    val encoded = entry.encodeMessage()
                    if(!encoded.contentEquals(entry.rawBytes)) {
                        failed = true
                        break
                    }
                }
                assertFalse(failed)
            }
        }
    }
})