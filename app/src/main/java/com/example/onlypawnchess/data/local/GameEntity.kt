package com.example.onlypawnchess.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onlypawnchess.domain.model.Board
import com.example.onlypawnchess.domain.model.GameResult
import com.example.onlypawnchess.domain.model.GameState
import com.example.onlypawnchess.domain.model.Player


@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val gameId: String,

    val boardState: String,

    val currentPlayer: String,

    val status: String,

    val createdAt: Long,

    val updatedAt: Long
)

fun GameEntity.toGameState(): GameState {

    val result = when (status) {
        "IN_PROGRESS" -> GameResult.InProgress
        "USER_WINS" -> GameResult.UserWins
        "COMPUTER_WINS" -> GameResult.ComputerWins
        "DRAW" -> GameResult.Draw

        else -> throw IllegalArgumentException(
            "Unknown game status: $status"
        )
    }

    return GameState(
        board = Board.fromStorageString(boardState),
        currentPlayer = Player.valueOf(currentPlayer),
        result = result
    )
}