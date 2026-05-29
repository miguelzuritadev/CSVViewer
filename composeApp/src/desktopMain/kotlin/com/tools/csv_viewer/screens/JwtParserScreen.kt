package com.tools.csv_viewer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.sebastianneubauer.jsontree.JsonTree
import com.sebastianneubauer.jsontree.TreeState
import java.util.Base64

@Composable
fun JwtParserScreen() {
    var jwtInput by remember { mutableStateOf("") }
    var headerJson by remember { mutableStateOf<String?>(null) }
    var payloadJson by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(jwtInput) {
        if (jwtInput.isBlank()) {
            headerJson = null
            payloadJson = null
            error = null
            return@LaunchedEffect
        }

        try {
            val parts = jwtInput.trim().split(".")
            if (parts.size < 2) {
                error = "Invalid JWT: Expected at least 2 parts (Header and Payload)"
                headerJson = null
                payloadJson = null
                return@LaunchedEffect
            }

            val decoder = Base64.getUrlDecoder()
            headerJson = String(decoder.decode(parts[0]))
            payloadJson = String(decoder.decode(parts[1]))
            error = null
        } catch (e: Exception) {
            error = "Error decoding JWT: ${e.message}"
            headerJson = null
            payloadJson = null
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("JWT Parser", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 16.dp))

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Left Column: JWT Input
            Column(modifier = Modifier.weight(1f)) {
                Text("Encoded JWT", style = MaterialTheme.typography.subtitle1)
                OutlinedTextField(
                    value = jwtInput,
                    onValueChange = { jwtInput = it },
                    modifier = Modifier.fillMaxSize().padding(top = 8.dp),
                    placeholder = { Text("Paste your JWT here...") },
                    textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace)
                )
            }

            // Right Column: Decoded Output
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (error != null) {
                    Text(error!!, color = Color.Red, modifier = Modifier.padding(bottom = 16.dp))
                }

                if (headerJson != null) {
                    Text("Header", style = MaterialTheme.typography.subtitle1)
                    Surface(
                        elevation = 2.dp,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).weight(0.3f)
                    ) {
                        JsonTree(
                            json = headerJson!!,
                            initialState = TreeState.EXPANDED,
                            modifier = Modifier.padding(8.dp),
                            onLoading = { },
                            onError = { }
                        )
                    }
                }

                if (payloadJson != null) {
                    Text("Payload", style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(top = 16.dp))
                    Surface(
                        elevation = 2.dp,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).weight(0.6f)
                    ) {
                        JsonTree(
                            json = payloadJson!!,
                            initialState = TreeState.EXPANDED,
                            modifier = Modifier.padding(8.dp),
                            onLoading = { },
                            onError = { }
                        )
                    }
                }

                val signature = jwtInput.trim().split(".").getOrNull(2)
                if (signature != null) {
                    Text("Signature", style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(top = 16.dp))
                    Surface(
                        elevation = 2.dp,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).weight(0.1f)
                    ) {
                        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text(
                                signature,
                                modifier = Modifier.padding(8.dp),
                                style = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                if (headerJson == null && payloadJson == null && error == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Enter a JWT to see the decoded content", style = MaterialTheme.typography.body2, color = Color.Gray)
                    }
                }
            }
        }
    }
}