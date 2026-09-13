package com.example.onlypawnchess.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GlassBackButton(
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 0.dp,
        animationSpec = tween(100),
        label = "back_button_press"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "back_button_scale"
    )

    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(20.dp)
            .scale(scale)
            .offset(y = pressOffset)
            .size(60.dp)
            .clickable(
                interactionSource = interactionSource, indication = null, onClick = onClick
            ), contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val center = Offset(
                x = size.width / 2f, y = size.height / 2f
            )

            val radius = size.minDimension * 0.42f

            // ─────────────────────────
            // Bottom soft shadow
            // ─────────────────────────
            drawCircle(
                color = Color.Black.copy(alpha = 0.25f),
                radius = radius + 5.dp.toPx(),
                center = center.copy(
                    y = center.y + 7.dp.toPx()
                )
            )

            // ─────────────────────────
            // 3D glass bottom/base
            // ─────────────────────────
            drawCircle(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD8D8D8), Color(0xFFAAAAAA), Color(0xFF777777)
                    )
                ), radius = radius, center = center.copy(
                    y = center.y + 5.dp.toPx()
                )
            )

            // ─────────────────────────
            // Darker lower rim
            // ─────────────────────────
            drawCircle(
                color = Color(0xFF777777), radius = radius - 3.dp.toPx(), center = center.copy(
                    y = center.y + 3.dp.toPx()
                )
            )

            // ─────────────────────────
            // White glass surface
            // ─────────────────────────
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.98f),
                        Color.White.copy(alpha = 0.88f),
                        Color(0xFFE8E8E8).copy(alpha = 0.90f)
                    ), center = Offset(
                        center.x - radius * 0.25f, center.y - radius * 0.30f
                    ), radius = radius * 1.35f
                ), radius = radius - 2.dp.toPx(), center = center
            )

            // ─────────────────────────
            // Glass outer rim
            // ─────────────────────────
            drawCircle(
                color = Color.White.copy(alpha = 0.95f),
                radius = radius - 2.dp.toPx(),
                center = center,
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )

            // ─────────────────────────
            // Inner subtle rim
            // ─────────────────────────
            drawCircle(
                color = Color(0xFFBDBDBD).copy(alpha = 0.45f),
                radius = radius - 7.dp.toPx(),
                center = center,
                style = Stroke(
                    width = 1.dp.toPx()
                )
            )

            // ─────────────────────────
            // Top glass reflection
            // ─────────────────────────
            drawArc(
                color = Color.White.copy(alpha = 0.75f),
                startAngle = 205f,
                sweepAngle = 95f,
                useCenter = false,
                topLeft = Offset(
                    center.x - radius + 5.dp.toPx(), center.y - radius + 5.dp.toPx()
                ),
                size = Size(
                    width = (radius - 5.dp.toPx()) * 2, height = (radius - 5.dp.toPx()) * 2
                ),
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )

            // ─────────────────────────
            // Small secondary highlight
            // ─────────────────────────
            drawArc(
                color = Color.White.copy(alpha = 0.40f),
                startAngle = 25f,
                sweepAngle = 55f,
                useCenter = false,
                topLeft = Offset(
                    center.x - radius + 7.dp.toPx(), center.y - radius + 7.dp.toPx()
                ),
                size = Size(
                    width = (radius - 7.dp.toPx()) * 2, height = (radius - 7.dp.toPx()) * 2
                ),
                style = Stroke(
                    width = 1.5.dp.toPx()
                )
            )
        }

        // Back arrow
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color(0xFF333333),
            modifier = Modifier.size(27.dp)
        )
    }
}