package com.tools.csv_viewer

import CsvRecord
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onFocusedBoundsChanged
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import com.sebastianneubauer.jsontree.JsonTree
import org.jetbrains.compose.ui.tooling.preview.Preview

import java.awt.FileDialog
import java.awt.Frame
import java.io.File


private val DarkColorPalette = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    onSurface = Color.White // Default color for Text in dark theme
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    onSurface = Color.Black // Default color for Text in light theme
)


@Composable
@Preview
fun App(darkTheme: Boolean = isSystemInDarkTheme()) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(colors = colors) {
        var showContent by remember { mutableStateOf(false) }
        var records by remember { mutableStateOf(emptyList<CsvRecord>()) }
        var filterText by remember { mutableStateOf("") }
        var requestJSON by remember { mutableStateOf("") }
        var responseJSON by remember { mutableStateOf("") }

        Scaffold {
            Column(
                Modifier.fillMaxSize().background(colors.background), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    showContent = !showContent
                }) {
                    Text("Abrir archivo CSV")
                }
                AnimatedVisibility(showContent) {
                    FileDialog(null) { filePath ->
//                        println("Selected file: $filePath")
                        val fileContent = filePath?.let { file -> File(file).readText() }
//                        println("File content: $fileContent")

                        filePath?.let {
                            val logCSVParser = LogCSVParser(it)
                            val csvRecords = logCSVParser.parse()
                            /*csvRecords.forEach { row ->
                                println(row)
                            }*/
                            records = csvRecords
                        }
                    }
                }

                Row {
                    Column(modifier = Modifier.weight(0.7f).fillMaxHeight(0.5f)) {
                        CsvDataGrid(records.filter { it.idTracking.contains(filterText, ignoreCase = true) }) { record ->
                            requestJSON = record.bodyRequest
                            responseJSON = record.bodyResponse

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
//                        JOptionPane.showMessageDialog(null, message, "Record Details", JOptionPane.INFORMATION_MESSAGE)
                        }
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

                Row {
                    Column(modifier = Modifier.weight(0.7f)) {
                        Text("Request JSON")
                        JsonTree(
                            json = requestJSON,
                            onLoading = { Text(text = "Loading...") },
                            onError = { throwable: Throwable ->
//                                println("Error: ${throwable.message}")
                            }
                        )
                    }
                    Column(modifier = Modifier.weight(0.3f)) {
                        Text("Response JSON")
                        JsonTree(
                            json = responseJSON,
                            onLoading = { Text(text = "Loading...") },
                            onError = { throwable: Throwable ->
//                                println("Error: ${throwable.message}")
                            }
                        )
                    }
                }
            }
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
fun CsvRecordRow(record: CsvRecord, onClick: (CsvRecord) -> Unit = {}, focusManager: FocusManager = LocalFocusManager.current) {
    val focusRequester = remember { FocusRequester() }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            focusRequester.requestFocus()
            onClick(record)
        }
        .focusRequester(focusRequester)
        .focusable()
        .onFocusChanged {
            println("onFocusChanged => it.isFocused: ${it.isFocused}")
            if (it.isFocused) {
                onClick(record)
            }
        }
        .onKeyEvent { keyEvent ->
//            println("keyEvent.type: ${keyEvent.type}")
            if (keyEvent.type == KeyEventType.KeyDown) {
                when (keyEvent.key) {
                    Key.DirectionDown -> {
                        println("==Key.DirectionDown==")
                        focusManager.moveFocus(FocusDirection.Down)
//                        val nextFocusedView = focusManager.focusedItem
//                        println("Next focused view: $nextFocusedView")
                        true
                    }

                    Key.DirectionUp -> {
                        println("==Key.DirectionUp==")
                        focusManager.moveFocus(FocusDirection.Up)
                        true
                    }

                    else -> false
                }
            } else {
                false
            }
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
fun CsvDataGrid(records: List<CsvRecord>, onClick: (CsvRecord) -> Unit = {}) {
    LazyColumn(modifier = Modifier.focusGroup()) {
        item {
            CsvHeaderRow()
        }
        items(records) { record ->
            CsvRecordRow(record, onClick)
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