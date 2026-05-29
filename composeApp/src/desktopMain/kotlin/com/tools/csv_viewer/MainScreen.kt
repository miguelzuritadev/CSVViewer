package com.tools.csv_viewer

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tools.csv_viewer.screens.CsvViewerScreen
import com.tools.csv_viewer.screens.JwtParserScreen
import com.tools.csv_viewer.screens.MenuScreen
import com.tools.csv_viewer.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


enum class Tool {
    MENU, CSV_VIEWER, JWT_PARSER
}

@Composable
@Preview
fun MainScreen() {
    var currentTool by remember { mutableStateOf(Tool.MENU) }

    AppTheme {
        Scaffold(
            topBar = {
                if (currentTool != Tool.MENU) {
                    Button(onClick = { currentTool = Tool.MENU }, modifier = Modifier.padding(8.dp)) {
                        Text("Back to Menu")
                    }
                }
            }
        ) {
            when (currentTool) {
                Tool.MENU -> MenuScreen { tool -> currentTool = tool }
                Tool.CSV_VIEWER -> CsvViewerScreen()
                Tool.JWT_PARSER -> JwtParserScreen()
            }
        }
    }
}

