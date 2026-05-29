package com.tools.csv_viewer

import CsvRecord
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import com.sebastianneubauer.jsontree.JsonTree
import com.sebastianneubauer.jsontree.TreeState
import csvviewer.composeapp.generated.resources.Res
import csvviewer.composeapp.generated.resources.compose_multiplatform
import csvviewer.composeapp.generated.resources.ic_android_logo
import csvviewer.composeapp.generated.resources.ic_apple_logo
import csvviewer.composeapp.generated.resources.ic_arrow_drop_down
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import java.awt.FileDialog
import java.awt.Frame


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

    val filterOptions = listOf("All", "android", "ios", "web")
    var selectedFilter by remember { mutableStateOf("All") }

    MaterialTheme(colors = colors) {
        var showContent by remember { mutableStateOf(false) }
        var records by remember { mutableStateOf(emptyList<CsvRecord>()) }
        var filterText by remember { mutableStateOf("") }
        var currentItem: CsvRecord by remember { mutableStateOf(CsvRecord("", "", "", "", "", "", "", "")) }
        val filterURLOptions = remember(records) { records.map { it.url }.distinct().toCollection(arrayListOf("All")) }
        var selectedURLFilter by remember { mutableStateOf("All") }

        Scaffold {
            LaunchedEffect(Unit) {
//                println("==LaunchedEffect==")

                val logCSVParser = LogCSVParser("C:\\projects\\kmp\\CSVViewer\\logs.csv")
                records = logCSVParser.parse()
            }
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
                        println("filePath: $filePath")
                        filePath?.let { path ->
                            val logCSVParser = LogCSVParser(path)
                            records = logCSVParser.parse()
                        }
                    }
                }

                val focusManager = LocalFocusManager.current
                Row {
                    Column(modifier = Modifier.weight(0.7f).fillMaxHeight(0.3f)) {
                        CsvDataGrid(records.filter {
                            (selectedFilter == "All" || it.platform == selectedFilter) &&
                            (selectedURLFilter == "All" || it.url.contains(selectedURLFilter, ignoreCase = true) ) &&
                                    (it.idTracking.contains(filterText, ignoreCase = true) ||
                                            it.bodyResponse.contains(filterText, ignoreCase = true) ||
                                            it.bodyRequest.contains(filterText, ignoreCase = true))
                        }, focusManager) { record ->
                            currentItem = record
                        }
                    }

                    Column(modifier = Modifier.weight(0.3f)) {
                        TextField(
                            value = filterText,
                            onValueChange = { filterText = it },
                            label = { Text("Filter by ID Tracking") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )

                        Text("Filter by Platform", modifier = Modifier.padding(start = 8.dp, top = 8.dp), style = MaterialTheme.typography.caption)
                        var platformExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Button(onClick = { platformExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                                Text(selectedFilter)
                                Icon(
                                    painter = painterResource(Res.drawable.ic_arrow_drop_down),
                                    contentDescription = "Dropdown Arrow"
                                )
                            }
                            DropdownMenu(
                                expanded = platformExpanded,
                                onDismissRequest = { platformExpanded = false }
                            ) {
                                filterOptions.forEach { option ->
                                    DropdownMenuItem(onClick = {
                                        selectedFilter = option
                                        focusManager.moveFocus(FocusDirection.Up)
                                        focusManager.moveFocus(FocusDirection.Left)
                                        platformExpanded = false
                                    }) {
                                        Text(option)
                                    }
                                }
                            }
                        }

                        Text("Filter by URL", modifier = Modifier.padding(start = 8.dp, top = 8.dp), style = MaterialTheme.typography.caption)
                        var urlExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Button(onClick = { urlExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                                Text(selectedURLFilter)
                                Icon(
                                    painter = painterResource(Res.drawable.ic_arrow_drop_down),
                                    contentDescription = "Dropdown Arrow"
                                )
                            }
                            DropdownMenu(
                                expanded = urlExpanded,
                                onDismissRequest = { urlExpanded = false }
                            ) {
                                filterURLOptions.forEach { option ->
                                    DropdownMenuItem(onClick = {
                                        selectedURLFilter = option
                                        focusManager.moveFocus(FocusDirection.Up)
                                        focusManager.moveFocus(FocusDirection.Up)
                                        focusManager.moveFocus(FocusDirection.Left)
                                        urlExpanded = false
                                    }) {
                                        Text(option)
                                    }
                                }
                            }
                        }
                        Button(
                            onClick = {
                                selectedFilter = "All"
                                selectedURLFilter = "All"
                                filterText = ""
                                focusManager.moveFocus(FocusDirection.Up)
                                focusManager.moveFocus(FocusDirection.Up)
                                focusManager.moveFocus(FocusDirection.Up)
                                focusManager.moveFocus(FocusDirection.Left)
                            },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("Reset Filters")
                        }
                    }
                }

                Row {
                    Column(modifier = Modifier.weight(0.7f)) {
                        Text("Request JSON [${currentItem.dateRequest}]")
                        JsonTree(
                            json = currentItem.bodyRequest,
                            initialState = TreeState.EXPANDED,
                            onLoading = { },
                            onError = { throwable: Throwable ->
//                                println("Error: ${throwable.message}")
                            }
                        )
                    }
                    Column(modifier = Modifier.weight(0.3f)) {
                        Text("Response JSON [${currentItem.dateResponse}]")
                        JsonTree(
                            json = currentItem.bodyResponse,
                            initialState = TreeState.EXPANDED,
                            onLoading = { },
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
        Text("#", Modifier.width(30.dp))
        Text("Date Request", Modifier.weight(1f))
        Text("Date Response", Modifier.weight(1f))
        Text("Body Request", Modifier.weight(1f))
        Text("Body Response", Modifier.weight(1f))
        Text("Enterprise Code", Modifier.weight(1f))
        Text("OS", Modifier.width(50.dp))
        Text("ID Tracking", Modifier.weight(1f))
        Text("URL", Modifier.weight(1f))
    }
}

@Composable
fun CsvRecordRow(index: Int, record: CsvRecord, onClick: (CsvRecord) -> Unit = {}, focusManager: FocusManager) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .padding(8.dp)
        .focusRequester(focusRequester)
        .onFocusChanged { state ->
//            println("onFocusChanged => it.isFocused: ${state.isFocused}")
            isFocused = state.isFocused
            if (state.isFocused) {
                onClick(record)
            }
        }
        .background(if (isFocused) Color.Gray else Color.Transparent)
        .clickable {
            focusRequester.requestFocus()
            onClick(record)
        }
        .onKeyEvent { event ->
            when {
                event.type == KeyEventType.KeyDown && event.key == Key.DirectionUp -> {
                    focusManager.moveFocus(FocusDirection.Up)
                }

                event.type == KeyEventType.KeyDown && event.key == Key.DirectionDown -> {
                    focusManager.moveFocus(FocusDirection.Down)
                }

                else -> false
            }
        }
    ) {
        Text(index.toString(), Modifier.width(30.dp))
        Text(record.dateRequest, Modifier.weight(1f))
        Text(record.dateResponse, Modifier.weight(1f))
        Text(record.bodyRequest, Modifier.weight(1f))
        Text(record.bodyResponse, Modifier.weight(1f))
        Text(record.enterpriseCode, Modifier.weight(1f))
//        Text(record.platform, Modifier.weight(1f))
        Box(
            modifier = Modifier
                .width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    if (record.platform == "android") Res.drawable.ic_android_logo else if (record.platform == "ios") Res.drawable.ic_apple_logo else Res.drawable.compose_multiplatform
                ),
                tint = Color.White,
                contentDescription = "platform icon",
            )
        }

        Text(record.idTracking, Modifier.weight(1f))
        Text(record.url, Modifier.weight(1f))
    }
}

@Composable
fun CsvDataGrid(records: List<CsvRecord>, focusManager: FocusManager, onClick: (CsvRecord) -> Unit = {}) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    CsvHeaderRow()
    LazyColumn(
        state = scrollState,
        modifier = Modifier.focusGroup().draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                coroutineScope.launch {
                    scrollState.scrollBy(-delta)
                }
            },
        )
    ) {
        itemsIndexed(records) { index, record ->
            CsvRecordRow(index.plus(1), record, onClick, focusManager)
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