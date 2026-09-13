package com.example.onlypawnchess.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.onlypawnchess.domain.model.Board
import com.example.onlypawnchess.domain.model.Move
import com.example.onlypawnchess.domain.model.Position

@Composable
fun ChessBoard(
    board: Board,
    selectedPosition: Position?,
    animatedMove: Move?,
    onSquareClick: (Position) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {

            for (row in 0 until Board.SIZE) {

                Row(
                    modifier = Modifier.weight(1f)
                ) {

                    for (column in 0 until Board.SIZE) {

                        val position = Position(row, column)

                        ChessSquare(
                            position = position,
                            piece = board.get(position),
                            isSelected = position == selectedPosition,
                            isAnimating = position == animatedMove?.to,
                            onClick = {
                                onSquareClick(position)
                            })
                    }
                }
            }
        }
    }
}