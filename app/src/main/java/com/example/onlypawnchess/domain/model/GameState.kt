package com.example.onlypawnchess.domain.model

import com.example.onlypawnchess.data.local.GameEntity

data class GameState(
    val board: Board, val currentPlayer: Player, val result: GameResult
)

fun GameState.toEntity(
    gameId: String, createdAt: Long, updatedAt: Long
): GameEntity {

    val status = when (result) {
        GameResult.InProgress -> GameStatus.IN_PROGRESS
        GameResult.UserWins -> GameStatus.USER_WINS
        GameResult.ComputerWins -> GameStatus.COMPUTER_WINS
        GameResult.Draw -> GameStatus.DRAW
    }

    return GameEntity(
        gameId = gameId,
        boardState = board.toStorageString(),
        currentPlayer = currentPlayer.name,
        status = status.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

