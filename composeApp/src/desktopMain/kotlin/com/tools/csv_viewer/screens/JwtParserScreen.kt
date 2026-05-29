package com.tools.csv_viewer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tools.csv_viewer.jwt.JwtDecodedSection
import com.tools.csv_viewer.jwt.JwtInputSection
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
            JwtInputSection(
                jwtInput = jwtInput,
                onValueChange = { jwtInput = it },
                modifier = Modifier.weight(1f)
            )

            JwtDecodedSection(
                headerJson = headerJson,
                payloadJson = payloadJson,
                signature = jwtInput.trim().split(".").getOrNull(2),
                error = error,
                modifier = Modifier.weight(1f)
            )
        }
    }
}