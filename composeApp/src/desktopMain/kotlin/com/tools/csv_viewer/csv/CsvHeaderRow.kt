package com.tools.csv_viewer.csv

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CsvHeaderRow() {
    Row(Modifier.Companion.fillMaxWidth().padding(8.dp)) {
        Text("#", Modifier.Companion.width(30.dp))
        Text("Date Request", Modifier.Companion.weight(1f))
        Text("Date Response", Modifier.Companion.weight(1f))
        Text("Body Request", Modifier.Companion.weight(1f))
        Text("Body Response", Modifier.Companion.weight(1f))
        Text("Enterprise Code", Modifier.Companion.weight(1f))
        Text("OS", Modifier.Companion.width(50.dp))
        Text("ID Tracking", Modifier.Companion.weight(1f))
        Text("URL", Modifier.Companion.weight(1f))
    }
}