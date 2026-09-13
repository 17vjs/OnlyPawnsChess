package com.example.onlypawnchess.domain.model

data class Move(
    val from: Position, val to: Position, val isCapture: Boolean
)