import com.tools.csv_viewer.LogCSVParser
import org.junit.Assert.*
import org.junit.Test

class LogCSVParserTest {

    @Test
    fun parse() {
        val logCSVParser = LogCSVParser("src/desktopTest/resources/example.csv")
        val records = logCSVParser.parse()
        assertEquals(2, records.size)
        assertEquals("android", records[0].platform)
        assertEquals("ENT123", records[0].enterpriseCode)
    }

    /**
     * Handle error in parsing file
     */
    @Test
    fun parseError() {
        val logCSVParser = LogCSVParser("src/desktopTest/resources/example_error.csv")
        val records = logCSVParser.parse()
        assertEquals(0, records.size)
    }
}