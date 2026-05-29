package com.tools.csv_viewer.jwt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun JwtDecodedSection(
    headerJson: String?,
    payloadJson: String?,
    signature: String?,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (error != null) {
            Text(error, color = Color.Red, modifier = Modifier.padding(bottom = 16.dp))
        }

        if (headerJson != null) {
            JwtJsonSection(
                title = "Header",
                json = headerJson,
                modifier = Modifier.fillMaxWidth().weight(0.3f)
            )
        }

        if (payloadJson != null) {
            Text("Payload", style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(top = 16.dp))
            JwtJsonSection(
                title = "Payload",
                json = payloadJson,
                modifier = Modifier.fillMaxWidth().weight(0.6f)
            )
        }

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
                Text(
                    "Enter a JWT to see the decoded content",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
        }
    }
}
