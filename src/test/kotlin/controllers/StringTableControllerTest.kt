package controllers

import models.StringTableEntry
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object StringTableControllerSpec: Spek({
    given("a string table controller") {
        val controller = StringTableController()
        on("loading a table") {
            val forest1stDir = "D:\\ACHax\\forest_1st.d\\data"
            val forest2ndDir = "D:\\ACHax\\forest_2nd.d\\data"

            val stringTable = controller.loadTable("$forest2ndDir\\message_data_table.bin",
                    "$forest2ndDir\\message_data.bin")
            it("should load the table") {
                for (entry in stringTable) {
                    println("${entry.id}: ${entry.content}")
                }
                assert(stringTable != null)
            }
        }
    }
})