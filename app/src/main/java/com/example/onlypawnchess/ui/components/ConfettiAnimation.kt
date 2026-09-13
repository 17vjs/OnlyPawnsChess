package com.example.onlypawnchess.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.onlypawnchess.ui.model.ConfettiPiece
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ConfettiAnimation() {

    val pieces = remember {
        List(90) {

            val colors = listOf(
                Color(0xFFFF5252),
                Color(0xFFFFD740),
                Color(0xFF69F0AE),
                Color(0xFF40C4FF),
                Color(0xFFE040FB),
                Color.White
            )

            ConfettiPiece(
                x = Random.nextFloat(),
                speed = Random.nextFloat() * 0.45f + 0.55f,
                size = Random.nextFloat() * 7f + 5f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 360f - 180f,
                drift = Random.nextFloat() * 0.08f - 0.04f,
                phase = Random.nextFloat(),
                color = colors.random()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(
        label = "confetti_transition"
    )

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 4200
            ), repeatMode = RepeatMode.Restart
        ), label = "confetti_progress"
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        pieces.forEach { piece ->

            val rawY = (progress * piece.speed + piece.phase) % 1.2f

            val y = rawY * size.height

            val drift = sin(
                (progress * 2.0 * PI) + piece.phase * 10
            ).toFloat() * size.width * piece.drift

            val x = piece.x * size.width + drift

            val rotation = piece.rotation + progress * piece.rotationSpeed

            rotate(
                degrees = rotation, pivot = Offset(x, y)
            ) {

                drawRect(
                    color = piece.color, topLeft = Offset(
                        x = x, y = y
                    ), size = Size(
                        width = piece.size, height = piece.size * 1.7f
                    )
                )
            }
        }
    }
}