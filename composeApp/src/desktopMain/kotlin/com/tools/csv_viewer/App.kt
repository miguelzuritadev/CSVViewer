package com.tools.csv_viewer

import CsvRecord
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import org.jetbrains.compose.ui.tooling.preview.Preview

import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.JOptionPane

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var records by remember { mutableStateOf(emptyList<CsvRecord>()) }
        var filterText by remember { mutableStateOf("") }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                showContent = !showContent
            }) {
                Text("Abrir archivo CSV")
            }
            AnimatedVisibility(showContent) {
                FileDialog(null) { filePath ->
                    println("Selected file: $filePath")
                    val fileContent = filePath?.let { file -> File(file).readText() }
                    println("File content: $fileContent")

                    filePath?.let {
                        val logCSVParser = LogCSVParser(it)
                        val csvRecords = logCSVParser.parse()
                        csvRecords.forEach { row ->
                            println(row)
                        }
                        records = csvRecords
                    }


                }
            }

            Row {
                Column(modifier = Modifier.weight(0.7f)) {
                    CsvDataGrid(records.filter { it.idTracking.contains(filterText, ignoreCase = true) })
                }

                Column(modifier = Modifier.weight(0.3f)) {
                    TextField(
                        value = filterText,
                        onValueChange = { filterText = it },
                        label = { Text("Filter by ID Tracking") },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }

            }

            /*JsonTree(
                json = "{ \"key\": \"value\" }",
                onLoading = { Text(text = "Loading...") }
            )*/


        }
    }
}

@Composable
fun CsvHeaderRow() {
    Row(Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Date Request", Modifier.weight(1f))
        Text("Date Response", Modifier.weight(1f))
        Text("Body Request", Modifier.weight(1f))
        Text("Body Response", Modifier.weight(1f))
        Text("Enterprise Code", Modifier.weight(1f))
        Text("Platform", Modifier.weight(1f))
        Text("ID Tracking", Modifier.weight(1f))
    }
}

@Composable
fun CsvRecordRow(record: CsvRecord) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Show alert with the record details
                val message = """
                Date Request: ${record.dateRequest}
                Date Response: ${record.dateResponse}
                Body Request: ${record.bodyRequest}
                Body Response: ${record.bodyResponse}
                Enterprise Code: ${record.enterpriseCode}
                Platform: ${record.platform}
                ID Tracking: ${record.idTracking}
            """.trimIndent()
                JOptionPane.showMessageDialog(null, message, "Record Details", JOptionPane.INFORMATION_MESSAGE)
            }
    ) {
        Text(record.dateRequest, Modifier.weight(1f))
        Text(record.dateResponse, Modifier.weight(1f))
        Text(record.bodyRequest, Modifier.weight(1f))
        Text(record.bodyResponse, Modifier.weight(1f))
        Text(record.enterpriseCode, Modifier.weight(1f))
        Text(record.platform, Modifier.weight(1f))
        Text(record.idTracking, Modifier.weight(1f))
    }
}

@Composable
fun CsvDataGrid(records: List<CsvRecord>) {
    LazyColumn {
        item {
            CsvHeaderRow()
        }
        items(records) { record ->
            CsvRecordRow(record)
        }
    }
}


@Composable
private fun FileDialog(parent: Frame? = null, onCloseRequest: (result: String?) -> Unit) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(file?.let { directory + it })
                }
            }
        }
    },
    dispose = FileDialog::dispose
)