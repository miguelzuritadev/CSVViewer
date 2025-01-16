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

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.awt.FileDialog
import java.awt.Frame
import java.io.BufferedReader

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                showContent = !showContent
            }) {
                Text("Click me!")

            }
            AnimatedVisibility(showContent) {
                /*val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }*/

                FileDialog(null) {
                    println("Selected file: $it")
                    val fileContent = it?.let { file -> java.io.File(file).readText() }
                    println("File content: $fileContent")

                    val bufferedReader = BufferedReader(java.io.FileReader(it))
                    val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT);
                    for (csvRecord in csvParser) {
                        val studentId = csvRecord.get(0);
                        val studentName = csvRecord.get(1);
                        val studentLastName = csvRecord.get(2);
                        var studentScore = csvRecord.get(3);
                        println(Student(studentId, studentName, studentLastName, studentScore))
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