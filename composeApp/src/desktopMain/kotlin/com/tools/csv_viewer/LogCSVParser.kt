package com.tools.csv_viewer

import CsvRecord
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.FileReader

class LogCSVParser(filePath: String) {
    val bufferedReader = BufferedReader(FileReader(filePath))
    val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT);

    fun parse(): ArrayList<CsvRecord> {
        val records = ArrayList<CsvRecord>()
        for (csvRecord in csvParser) {
            try {
                val row = CsvRecord(
                    csvRecord.get(0),
                    csvRecord.get(1),
                    csvRecord.get(2),
                    csvRecord.get(3),
                    csvRecord.get(4),
                    csvRecord.get(5),
                    csvRecord.get(6)
                )
                records.add(row)
            } catch (e: IndexOutOfBoundsException) {
                println("Error: Not enough properties in CSV record: ${csvRecord}")
            } catch (e: IllegalArgumentException) {
                println("Error: Incorrect data type in CSV record: ${csvRecord}")
            }
        }
        return records
    }
}