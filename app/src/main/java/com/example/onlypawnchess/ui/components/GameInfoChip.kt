package com.example.onlypawnchess.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GameInfoChip(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp), color = Color(0xFF292929)
    ) {
        Text(
            text = text, modifier = Modifier.padding(
                horizontal = 10.dp, vertical = 6.dp
            ), color = Color.LightGray, style = MaterialTheme.typography.labelMedium
        )
    }
}