package com.tools.csv_viewer.csv

import CsvRecord
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import kotlinx.coroutines.launch

@Composable
fun CsvDataGrid(records: List<CsvRecord>, focusManager: FocusManager, onClick: (CsvRecord) -> Unit = {}) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    CsvHeaderRow()
    LazyColumn(
        state = scrollState,
        modifier = Modifier.Companion.focusGroup().draggable(
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