package com.example.onlypawnchess.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.onlypawnchess.domain.model.GameResult

@Composable
fun GameResultOverlay(
    result: GameResult, onNewGame: () -> Unit, onBack: () -> Unit
) {

    AnimatedVisibility(
        visible = result != GameResult.InProgress, enter = fadeIn(
            animationSpec = tween(500)
        ), exit = fadeOut(
            animationSpec = tween(300)
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.35f)
                )
        ) {

            // Celebration confetti
            ConfettiAnimation()

            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {

                CelebrationCard(
                    result = result, onNewGame = onNewGame, onBack = onBack
                )
            }
        }
    }
}