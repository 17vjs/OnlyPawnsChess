package com.example.onlypawnchess

import com.example.onlypawnchess.data.local.GameEntity
import com.example.onlypawnchess.data.repository.GameRepositoryInterface
import com.example.onlypawnchess.domain.model.Board
import com.example.onlypawnchess.domain.model.GameResult
import com.example.onlypawnchess.domain.model.GameState
import com.example.onlypawnchess.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class GameViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createGameState(): GameState {
        return GameState(
            board = Board(),
            currentPlayer = Player.USER,
            result = GameResult.InProgress
        )
    }

    @Test
    fun `createNewGame should create game`() = runTest {
        val repository = FakeGameRepository()
        val viewModel = GameViewModel(repository)

        viewModel.createNewGame("game-1")

        advanceUntilIdle()

        assertEquals(
            "game-1",
            repository.createdGameId
        )

        assertEquals(
            GameResult.InProgress,
            repository.createdGameState?.result
        )

        assertEquals(
            Player.USER,
            repository.createdGameState?.currentPlayer
        )
    }

    @Test
    fun `saveGame should save game`() = runTest {
        val repository = FakeGameRepository()
        val viewModel = GameViewModel(repository)

        val state = createGameState()

        viewModel.saveGame(
            gameId = "game-1",
            gameState = state
        )

        advanceUntilIdle()

        assertEquals(
            "game-1",
            repository.savedGameId
        )

        assertEquals(
            state,
            repository.savedGameState
        )
    }

    @Test
    fun `deleteGame should delete game`() = runTest {
        val repository = FakeGameRepository()
        val viewModel = GameViewModel(repository)

        viewModel.deleteGame("game-1")

        advanceUntilIdle()

        assertEquals(
            "game-1",
            repository.deletedGameId
        )
    }

    @Test
    fun `loadGame should return game state`() = runTest {
        val repository = FakeGameRepository()

        val state = createGameState()

        repository.game = GameEntity(
            gameId = "game-1",
            boardState = state.board.toStorageString(),
            currentPlayer = state.currentPlayer.name,
            status = "IN_PROGRESS",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val viewModel = GameViewModel(repository)

        val result = viewModel.loadGame("game-1")

        assertEquals(
            state.board.toStorageString(),
            result?.board?.toStorageString()
        )

        assertEquals(
            state.currentPlayer,
            result?.currentPlayer
        )

        assertEquals(
            state.result,
            result?.result
        )
    }

    @Test
    fun `loadGame should return null when game does not exist`() =
        runTest {
            val repository = FakeGameRepository()
            val viewModel = GameViewModel(repository)

            val result = viewModel.loadGame("game-1")

            assertNull(result)
        }

    @Test
    fun `games should expose repository games`() = runTest {
        val game = GameEntity(
            gameId = "game-1",
            boardState = Board().toStorageString(),
            currentPlayer = Player.USER.name,
            status = "IN_PROGRESS",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val repository = FakeGameRepository()

        repository.games.value = listOf(game)

        val viewModel = GameViewModel(repository)

        val job = backgroundScope.launch {
            viewModel.games.collect {}
        }

        advanceUntilIdle()

        assertEquals(
            listOf(game),
            viewModel.games.value
        )

        job.cancel()
    }
}

private class FakeGameRepository : GameRepositoryInterface {

    var createdGameId: String? = null
    var createdGameState: GameState? = null

    var savedGameId: String? = null
    var savedGameState: GameState? = null

    var deletedGameId: String? = null

    var game: GameEntity? = null

    val games = MutableStateFlow<List<GameEntity>>(
        emptyList()
    )

    override suspend fun createGame(
        gameId: String,
        gameState: GameState
    ) {
        createdGameId = gameId
        createdGameState = gameState
    }

    override suspend fun saveGame(
        gameId: String,
        gameState: GameState
    ) {
        savedGameId = gameId
        savedGameState = gameState
    }

    override suspend fun getGame(
        gameId: String
    ): GameEntity? {
        return game?.takeIf {
            it.gameId == gameId
        }
    }

    override suspend fun deleteGame(
        gameId: String
    ) {
        deletedGameId = gameId
    }

    override fun observeGames(): Flow<List<GameEntity>> {
        return games
    }
}