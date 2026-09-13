package com.example.onlypawnchess.data.repository

import com.example.onlypawnchess.data.local.GameEntity
import com.example.onlypawnchess.domain.model.GameState
import kotlinx.coroutines.flow.Flow

interface GameRepositoryInterface {

    suspend fun createGame(
        gameId: String,
        gameState: GameState
    )

    suspend fun saveGame(
        gameId: String,
        gameState: GameState
    )

    suspend fun getGame(
        gameId: String
    ): GameEntity?

    suspend fun deleteGame(
        gameId: String
    )

    fun observeGames(): Flow<List<GameEntity>>
}