package com.example.onlypawnchess.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NewGameFab(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(68.dp)
            .shadow(
                elevation = 10.dp, shape = CircleShape
            )
            .background(
                color = Color(0xFFC62828), shape = CircleShape
            )
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "New Game",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}