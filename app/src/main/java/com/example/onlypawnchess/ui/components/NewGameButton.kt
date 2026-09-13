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
import androidx.compose.material.icons.filled.Refresh
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
fun NewGameButton(
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 0.dp,
        animationSpec = tween(100),
        label = "button_press"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "button_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .scale(scale)
                .offset(y = pressOffset)
                .size(82.dp)
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

                val radius = size.minDimension * 0.43f

                // Bottom shadow
                drawCircle(
                    color = Color.Black.copy(alpha = 0.45f),
                    radius = radius + 5.dp.toPx(),
                    center = center.copy(
                        y = center.y + 7.dp.toPx()
                    )
                )

                // Black cylindrical base
                drawCircle(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF555555), Color(0xFF202020), Color(0xFF090909)
                        )
                    ), radius = radius, center = center.copy(
                        y = center.y + 5.dp.toPx()
                    )
                )

                // Inner black rim
                drawCircle(
                    color = Color(0xFF111111), radius = radius - 5.dp.toPx(), center = center.copy(
                        y = center.y + 3.dp.toPx()
                    )
                )

                // Red button
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF3B3B), Color(0xFFE60000), Color(0xFF9B0000)
                        ), center = Offset(
                            center.x - radius * 0.22f, center.y - radius * 0.25f
                        ), radius = radius * 1.25f
                    ), radius = radius - 2.dp.toPx(), center = center
                )

                // Dark red outer rim
                drawCircle(
                    color = Color(0xFF8B0000),
                    radius = radius - 2.dp.toPx(),
                    center = center,
                    style = Stroke(
                        width = 3.dp.toPx()
                    )
                )

                // Inner highlight
                drawCircle(
                    color = Color(0xFFE00000), radius = radius - 9.dp.toPx(), center = center.copy(
                        y = center.y - 2.dp.toPx()
                    ), style = Stroke(
                        width = 2.dp.toPx()
                    )
                )

                // Glossy highlight
                drawArc(
                    color = Color.White.copy(alpha = 0.32f),
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
            }

            // Refresh arrow on top of the red button
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "New Game",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
