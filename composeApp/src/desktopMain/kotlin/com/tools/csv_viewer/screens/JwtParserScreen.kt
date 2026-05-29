package com.tools.csv_viewer.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JwtParserScreen() {
    Column(
        modifier = Modifier.Companion.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text("JWT Parser", style = MaterialTheme.typography.h5)
        Text("Implementation coming soon...", modifier = Modifier.Companion.padding(top = 16.dp))
    }
}