package com.tools.csv_viewer.jwt

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sebastianneubauer.jsontree.JsonTree
import com.sebastianneubauer.jsontree.TreeState

@Composable
fun JwtJsonSection(
    title: String,
    json: String,
    modifier: Modifier = Modifier
) {
    Text(title, style = MaterialTheme.typography.subtitle1)
    Surface(
        elevation = 2.dp,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        JsonTree(
            json = json,
            initialState = TreeState.EXPANDED,
            modifier = Modifier.padding(8.dp),
            onLoading = { },
            onError = { }
        )
    }
}
