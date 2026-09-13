package com.example.onlypawnchess.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlypawnchess.domain.model.GameResult


@Composable
fun CelebrationCard(
    result: GameResult, onNewGame: () -> Unit, onBack: () -> Unit
) {
    val isWin = result == GameResult.UserWins
    val isLose = result == GameResult.ComputerWins

    val title = when (result) {
        GameResult.UserWins -> "YOU WIN"
        GameResult.ComputerWins -> "GAME OVER"
        GameResult.Draw -> "DRAW"
        GameResult.InProgress -> ""
    }

    val titleColor = when (result) {
        GameResult.UserWins -> Color(0xFF35C83D)
        GameResult.ComputerWins -> Color(0xFFFF5555)
        GameResult.Draw -> Color(0xFFFFB52E)
        GameResult.InProgress -> Color.White
    }

    val borderColor = when (result) {
        GameResult.UserWins -> Color(0xFFFF4F5E)
        GameResult.ComputerWins -> Color(0xFF5E7CFF)
        GameResult.Draw -> Color(0xFFFFB52E)
        GameResult.InProgress -> Color.Gray
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(36.dp),
        color = Color(0xFFFFE9C7),
        shadowElevation = 16.dp
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 10.dp, color = borderColor, shape = RoundedCornerShape(36.dp)
                )
                .padding(
                    start = 28.dp, end = 28.dp, top = 70.dp, bottom = 28.dp
                )
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-70).dp)
                    .fillMaxWidth(0.9f)
                    .height(76.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(titleColor), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CelebrationStar(
                        size = 62.dp, color = if (isLose) Color(0xFFFFC94A)
                        else Color(0xFFFFD52E)
                    )

                    CelebrationStar(
                        size = 88.dp, color = Color(0xFFFFD52E)
                    )

                    CelebrationStar(
                        size = 62.dp, color = if (isLose) Color(0xFFFFC94A)
                        else Color(0xFFFFD52E)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = when (result) {
                        GameResult.UserWins -> "CONGRATULATIONS!"
                        GameResult.ComputerWins -> "BETTER LUCK NEXT TIME!"
                        GameResult.Draw -> "NOBODY WON THIS ROUND!"
                        GameResult.InProgress -> ""
                    },
                    color = Color(0xFFFF765F),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFF7A6D)), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (result) {
                            GameResult.UserWins -> "WINNER : YOU"
                            GameResult.ComputerWins -> "WINNER : COMPUTER"
                            GameResult.Draw -> "NO WINNER"
                            GameResult.InProgress -> ""
                        }, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CelebrationButton(
                        icon = "☰", onClick = onBack
                    )



                    CelebrationButton(
                        icon = "▶", onClick = onNewGame
                    )
                }
            }
        }
    }
}