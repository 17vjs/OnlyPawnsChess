package com.example.onlypawnchess.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.onlypawnchess.domain.model.Board
import com.example.onlypawnchess.domain.model.Position

@Composable
fun ChessSquare(
    position: Position, piece: Int, isSelected: Boolean, isAnimating: Boolean, onClick: () -> Unit
) {

    val isLightSquare = (position.row + position.column) % 2 == 0

    val squareColor = when {
        isSelected -> Color(0xFFFFD54F)
        isLightSquare -> Color(0xFFF0D9B5)
        else -> Color(0xFFB58863)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(squareColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        if (piece != Board.EMPTY) {

            val scale by animateFloatAsState(
                targetValue = if (isAnimating) 1.12f else 1f, animationSpec = tween(
                    durationMillis = 350, easing = LinearOutSlowInEasing
                ), label = "pawn_scale"
            )

            Text(
                text = "♟", fontSize = 34.sp, color = if (piece == Board.USER) {
                    Color.White
                } else {
                    Color.Black
                }, modifier = Modifier.scale(scale)
            )
        }
    }
}