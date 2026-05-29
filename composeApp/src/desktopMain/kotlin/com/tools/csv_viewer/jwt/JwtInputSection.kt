package com.tools.csv_viewer.jwt

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun JwtInputSection(
    jwtInput: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Encoded JWT", style = MaterialTheme.typography.subtitle1)
        OutlinedTextField(
            value = jwtInput,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxSize().padding(top = 8.dp),
            placeholder = { Text("Paste your JWT here...") },
            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace)
        )
    }
}
