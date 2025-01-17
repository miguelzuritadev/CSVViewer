package com.tools.csv_viewer

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Toolkit

fun main() = application {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val windowSize = DpSize(screenSize.width.dp, screenSize.height.dp)


    Window(
        onCloseRequest = ::exitApplication,
        title = "CSVViewer",
        state = rememberWindowState(size = windowSize, position = WindowPosition.Aligned(Alignment.Center), isMinimized = false)
    ) {
        App()
    }
}