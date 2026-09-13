package com.example.onlypawnchess.data.repository

import com.example.onlypawnchess.data.local.GameDao
import com.example.onlypawnchess.data.local.GameEntity
import com.example.onlypawnchess.domain.model.Board
import com.example.onlypawnchess.domain.model.GameResult
import com.example.onlypawnchess.domain.model.GameState
import com.example.onlypawnchess.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameRepositoryTest {

    private lateinit var dao: FakeGameDao
    private lateinit var repository: GameRepository

    @Before
    fun setup() {
        dao = FakeGameDao()
        repository = GameRepository(dao)
    }

    private fun createGameState(): GameState {
        return GameState(
            board = Board(),
            currentPlayer = Player.USER,
            result = GameResult.InProgress
        )
    }

    @Test
    fun `createGame should insert game`() = runTest {
        repository.createGame(
            gameId = "game-1",
            gameState = createGameState()
        )

        assertNotNull(dao.insertedGame)
        assertEquals(
            "game-1",
            dao.insertedGame?.gameId
        )
    }

    @Test
    fun `createGame should set createdAt and updatedAt`() = runTest {
        repository.createGame(
            gameId = "game-1",
            gameState = createGameState()
        )

        val game = dao.insertedGame!!

        assertTrue(game.createdAt > 0)
        assertTrue(game.updatedAt > 0)
        assertEquals(game.createdAt, game.updatedAt)
    }

    @Test
    fun `createGame should save correct game state`() = runTest {
        val gameState = createGameState()

        repository.createGame(
            gameId = "game-1",
            gameState = gameState
        )

        val entity = dao.insertedGame!!

        assertEquals(
            gameState.board.toStorageString(),
            entity.boardState
        )

        assertEquals(
            Player.USER.name,
            entity.currentPlayer
        )

        assertEquals(
            "IN_PROGRESS",
            entity.status
        )
    }

    @Test
    fun `saveGame should do nothing when game does not exist`() = runTest {
        repository.saveGame(
            gameId = "game-1",
            gameState = createGameState()
        )

        assertNull(dao.updatedGame)
    }

    @Test
    fun `saveGame should update existing game`() = runTest {
        dao.game = createEntity(
            gameId = "game-1",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        repository.saveGame(
            gameId = "game-1",
            gameState = createGameState()
        )

        assertNotNull(dao.updatedGame)

        assertEquals(
            "game-1",
            dao.updatedGame?.gameId
        )
    }

    @Test
    fun `saveGame should preserve createdAt`() = runTest {
        val createdAt = 1000L

        dao.game = createEntity(
            gameId = "game-1",
            createdAt = createdAt,
            updatedAt = 2000L
        )

        repository.saveGame(
            gameId = "game-1",
            gameState = createGameState()
        )

        assertEquals(
            createdAt,
            dao.updatedGame?.createdAt
        )
    }

    @Test
    fun `saveGame should update updatedAt`() = runTest {
        dao.game = createEntity(
            gameId = "game-1",
            createdAt = 1000L,
            updatedAt = 1000L
        )

        repository.saveGame(
            gameId = "game-1",
            gameState = createGameState()
        )

        assertTrue(
            dao.updatedGame!!.updatedAt >= 1000L
        )
    }

    @Test
    fun `saveGame should save latest game state`() = runTest {
        dao.game = createEntity(
            gameId = "game-1",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val gameState = createGameState()

        repository.saveGame(
            gameId = "game-1",
            gameState = gameState
        )

        val updated = dao.updatedGame!!

        assertEquals(
            gameState.board.toStorageString(),
            updated.boardState
        )

        assertEquals(
            Player.USER.name,
            updated.currentPlayer
        )

        assertEquals(
            "IN_PROGRESS",
            updated.status
        )
    }

    @Test
    fun `getGame should return game from dao`() = runTest {
        val expected = createEntity(
            gameId = "game-1",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        dao.game = expected

        val result = repository.getGame("game-1")

        assertEquals(expected, result)
    }

    @Test
    fun `getGame should return null when game does not exist`() = runTest {
        dao.game = null

        val result = repository.getGame("game-1")

        assertNull(result)
    }

    @Test
    fun `deleteGame should delegate to dao`() = runTest {
        repository.deleteGame("game-1")

        assertEquals(
            "game-1",
            dao.deletedGameId
        )
    }

    @Test
    fun `observeGames should return dao flow`() = runTest {
        val games = listOf(
            createEntity(
                gameId = "game-1",
                createdAt = 1000L,
                updatedAt = 2000L
            )
        )

        dao.gamesFlow = flowOf(games)

        val result = repository.observeGames().first()

        assertEquals(games, result)
    }

    private fun createEntity(
        gameId: String,
        createdAt: Long,
        updatedAt: Long
    ): GameEntity {
        return GameEntity(
            gameId = gameId,
            boardState = Board().toStorageString(),
            currentPlayer = Player.USER.name,
            status = "IN_PROGRESS",
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
private class FakeGameDao : GameDao {

    var insertedGame: GameEntity? = null
    var updatedGame: GameEntity? = null
    var game: GameEntity? = null
    var deletedGameId: String? = null

    var gamesFlow: Flow<List<GameEntity>> =
        flowOf(emptyList())

    override suspend fun insert(game: GameEntity) {
        insertedGame = game
        this.game = game
    }

    override suspend fun update(game: GameEntity) {
        updatedGame = game
        this.game = game
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

        if (game?.gameId == gameId) {
            game = null
        }
    }

    override fun observeGames(): Flow<List<GameEntity>> {
        return gamesFlow
    }
}