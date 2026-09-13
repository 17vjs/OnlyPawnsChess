package com.example.onlypawnchess.ui.model

import androidx.compose.ui.graphics.Color

data class ConfettiPiece(
    val x: Float,
    val speed: Float,
    val size: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val drift: Float,
    val phase: Float,
    val color: Color
)