package views

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import views.getPairFilename
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

object StringTableChooser: Spek({
    given("a string table file") {
        on("getting a table filename") {
            val tableFile: File = File("D:\\ACHax\\forest_1st.d\\data\\message_data_table.bin")

            it("should return the data file filename") {
                assertEquals("D:\\ACHax\\forest_1st.d\\data\\message_data.bin", getPairFilename(tableFile))
            }
        }

        on("getting a data file filename") {
            val dataFile: File = File("D:\\ACHax\\forest_1st.d\\data\\message_data.bin")

            it("should return the table filename") {
                assertEquals("D:\\ACHax\\forest_1st.d\\data\\message_data_table.bin", getPairFilename(dataFile))
            }
        }

        on("getting a filename with suffix inside parent path") {
            val dataFile: File = File("D:\\ACHax\\forest_1st.d\\_data.bin\\message_data.bin")

            it("should return the table filename") {
                assertEquals("D:\\ACHax\\forest_1st.d\\_data.bin\\message_data_table.bin", getPairFilename(dataFile))
            }
        }

        on("getting an unrecognized filename") {
            val watFile: File = File("D:\\what.bin")
            it("should throw an illegal argument exception") {
                assertFailsWith<IllegalArgumentException> {
                    getPairFilename(watFile)
                }
            }
        }
    }
})