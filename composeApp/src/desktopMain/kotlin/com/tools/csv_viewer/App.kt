package com.tools.csv_viewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.AwtWindow
import org.jetbrains.compose.ui.tooling.preview.Preview

import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                showContent = !showContent
            }) {
                Text("Abrir archivo CSV")
            }
            AnimatedVisibility(showContent) {
                FileDialog(null) { filePath ->
                    println("Selected file: $filePath")
                    val fileContent = filePath?.let { file -> File(file).readText() }
                    println("File content: $fileContent")

                    filePath?.let {
                        val logCSVParser = LogCSVParser(it)
                        logCSVParser.parse().forEach { row ->
                            println(row)
                        }
                    }


                }
            }
        }
    }
}


@Composable
private fun FileDialog(parent: Frame? = null, onCloseRequest: (result: String?) -> Unit) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(file?.let { directory + it })
                }
            }
        }
    },
    dispose = FileDialog::dispose
)