package controllers

import models.StringTable
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertNotNull

object StringTableControllerSpec: Spek({
    given("a string table") {
        val stringTable = StringTable()
        on("loading a table") {
            val forest2ndDir = "D:\\ACHax\\forest_2nd.d\\data"

            stringTable.loadTableFromFiles("$forest2ndDir\\message_data_table.bin",
                    "$forest2ndDir\\message_data.bin")

            it("should load the table") {
                for (entry in stringTable.entries) {
                    println("${entry.id}: ${entry.content}")
                }

                assertNotNull(stringTable)
            }
        }
    }
})