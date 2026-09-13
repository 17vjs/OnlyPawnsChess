package com.example.onlypawnchess

import GameViewModelFactory
import androidx.lifecycle.ViewModel
import com.example.onlypawnchess.data.local.GameEntity
import com.example.onlypawnchess.data.repository.GameRepositoryInterface
import com.example.onlypawnchess.domain.model.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertThrows

class GameViewModelFactoryTest {

    private lateinit var factory: GameViewModelFactory

    @Before
    fun setup() {
        factory = GameViewModelFactory(
            repository = FakeGameRepository()
        )
    }

    @Test
    fun `create should return GameViewModel`() {
        val viewModel = factory.create(
            GameViewModel::class.java
        )

        assertTrue(
            viewModel is GameViewModel
        )
    }

    @Test
    fun `create should throw for unknown ViewModel`() {
        val exception = assertThrows(
            IllegalArgumentException::class.java
        ) {
            factory.create(
                UnknownViewModel::class.java
            )
        }

        assertEquals(
            "Unknown ViewModel class: ${UnknownViewModel::class.java.name}",
            exception.message
        )
    }

    private class UnknownViewModel : ViewModel()

    private class FakeGameRepository : GameRepositoryInterface {

        override suspend fun createGame(
            gameId: String,
            gameState: GameState
        ) {
        }

        override suspend fun saveGame(
            gameId: String,
            gameState: GameState
        ) {
        }

        override suspend fun getGame(
            gameId: String
        ): GameEntity? {
            return null
        }

        override suspend fun deleteGame(
            gameId: String
        ) {
        }

        override fun observeGames(): Flow<List<GameEntity>> {
            return flowOf(emptyList())
        }
    }
}