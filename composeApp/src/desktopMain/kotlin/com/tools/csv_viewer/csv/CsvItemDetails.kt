package com.tools.csv_viewer.csv

import CsvRecord
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianneubauer.jsontree.JsonTree
import com.sebastianneubauer.jsontree.TreeState

@Composable
fun CsvItemDetails(currentItem: CsvRecord) {
    Row {
        Column(modifier = Modifier.Companion.weight(0.7f)) {
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
        Column(modifier = Modifier.Companion.weight(0.3f)) {
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