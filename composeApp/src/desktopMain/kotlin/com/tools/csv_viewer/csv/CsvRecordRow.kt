package com.tools.csv_viewer.csv

import CsvRecord
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import csvviewer.composeapp.generated.resources.Res
import csvviewer.composeapp.generated.resources.compose_multiplatform
import csvviewer.composeapp.generated.resources.ic_android_logo
import csvviewer.composeapp.generated.resources.ic_apple_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun CsvRecordRow(index: Int, record: CsvRecord, onClick: (CsvRecord) -> Unit = {}, focusManager: FocusManager) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.Companion
        .fillMaxWidth()
        .height(40.dp)
        .padding(8.dp)
        .focusRequester(focusRequester)
        .onFocusChanged { state ->
//            println("onFocusChanged => it.isFocused: ${state.isFocused}")
            isFocused = state.isFocused
            if (state.isFocused) {
                onClick(record)
            }
        }
        .background(if (isFocused) Color.Companion.Gray else Color.Companion.Transparent)
        .clickable {
            focusRequester.requestFocus()
            onClick(record)
        }
        .onKeyEvent { event ->
            when {
                event.type == KeyEventType.Companion.KeyDown && event.key == Key.Companion.DirectionUp -> {
                    focusManager.moveFocus(FocusDirection.Companion.Up)
                }

                event.type == KeyEventType.Companion.KeyDown && event.key == Key.Companion.DirectionDown -> {
                    focusManager.moveFocus(FocusDirection.Companion.Down)
                }

                else -> false
            }
        }
    ) {
        Text(index.toString(), Modifier.Companion.width(30.dp))
        Text(record.dateRequest, Modifier.Companion.weight(1f))
        Text(record.dateResponse, Modifier.Companion.weight(1f))
        Text(record.bodyRequest, Modifier.Companion.weight(1f))
        Text(record.bodyResponse, Modifier.Companion.weight(1f))
        Text(record.enterpriseCode, Modifier.Companion.weight(1f))
//        Text(record.platform, Modifier.weight(1f))
        Box(
            modifier = Modifier.Companion
                .width(50.dp),
            contentAlignment = Alignment.Companion.Center
        ) {
            Icon(
                painter = painterResource(
                    if (record.platform == "android") Res.drawable.ic_android_logo else if (record.platform == "ios") Res.drawable.ic_apple_logo else Res.drawable.compose_multiplatform
                ),
                tint = Color.Companion.White,
                contentDescription = "platform icon",
            )
        }

        Text(record.idTracking, Modifier.Companion.weight(1f))
        Text(record.url, Modifier.Companion.weight(1f))
    }
}