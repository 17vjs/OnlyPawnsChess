package com.example.onlypawnchess.data.repository

import com.example.onlypawnchess.data.local.GameDao
import com.example.onlypawnchess.data.local.GameEntity
import com.example.onlypawnchess.domain.model.GameState
import com.example.onlypawnchess.domain.model.toEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val gameDao: GameDao
) : GameRepositoryInterface {
    override suspend fun createGame(
        gameId: String, gameState: GameState
    ) {
        val now = System.currentTimeMillis()
        gameDao.insert(
            gameState.toEntity(
                gameId = gameId, createdAt = now, updatedAt = now
            )
        )
    }

    override suspend fun saveGame(
        gameId: String, gameState: GameState
    ) {
        val existing = gameDao.getGame(gameId) ?: return

        gameDao.update(
            gameState.toEntity(
                gameId = gameId,
                createdAt = existing.createdAt,
                updatedAt = System.currentTimeMillis()
            )
        )
    }


    override suspend fun getGame(gameId: String): GameEntity? {
        return gameDao.getGame(gameId)
    }

    override suspend fun deleteGame(gameId: String) {
        gameDao.deleteGame(gameId)
    }

    override fun observeGames(): Flow<List<GameEntity>> {
        return gameDao.observeGames()
    }
}