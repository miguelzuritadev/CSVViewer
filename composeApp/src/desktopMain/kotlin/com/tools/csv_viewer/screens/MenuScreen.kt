package com.tools.csv_viewer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tools.csv_viewer.Tool

@Composable
fun MenuScreen(onToolSelected: (Tool) -> Unit) {
    Column(
        modifier = Modifier.Companion.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text("Select a Tool", style = MaterialTheme.typography.h4, modifier = Modifier.Companion.padding(bottom = 32.dp))
        Button(
            onClick = { onToolSelected(Tool.CSV_VIEWER) },
            modifier = Modifier.Companion.fillMaxWidth(0.5f).padding(8.dp)
        ) {
            Text("CSV Viewer")
        }
        Button(
            onClick = { onToolSelected(Tool.JWT_PARSER) },
            modifier = Modifier.Companion.fillMaxWidth(0.5f).padding(8.dp)
        ) {
            Text("JWT Parser")
        }
    }
}