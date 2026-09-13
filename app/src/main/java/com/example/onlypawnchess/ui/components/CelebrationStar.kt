package com.example.onlypawnchess.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun CelebrationStar(
    size: Dp, color: Color
) {
    Canvas(
        modifier = Modifier.size(size)
    ) {
        val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
        val outerRadius = size.toPx() / 2f
        val innerRadius = outerRadius * 0.45f

        val path = Path()

        for (i in 0 until 10) {
            val radius = if (i % 2 == 0) outerRadius else innerRadius
            val angle = (-90f + i * 36f) * Math.PI / 180f

            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        path.close()

        drawPath(
            path = path, color = color
        )

        drawPath(
            path = path, color = Color(0xFFE7A91B), style = Stroke(width = 5.dp.toPx())
        )
    }
}