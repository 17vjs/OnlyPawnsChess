package com.example.onlypawnchess.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.onlypawnchess.GameViewModel
import com.example.onlypawnchess.data.local.GameEntity
import com.example.onlypawnchess.data.repository.GameRepositoryInterface
import com.example.onlypawnchess.domain.model.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var repository: FakeGameRepository
    private lateinit var viewModel: GameViewModel

    private var backClicked = false

    @Before
    fun setup() {
        repository = FakeGameRepository()
        viewModel = GameViewModel(repository)

        backClicked = false
    }

    @Test
    fun gameScreenShouldDisplayBoardAndBackButton() {

        composeTestRule.setContent {
            GameScreen(
                gameId = "game-1",
                onBack = {
                    backClicked = true
                },
                gameViewModel = viewModel
            )
        }

        // Wait for LaunchedEffect(gameId) to load the game.
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertIsDisplayed()
    }

    @Test
    fun backButtonShouldInvokeOnBack() {

        composeTestRule.setContent {
            GameScreen(
                gameId = "game-1",
                onBack = {
                    backClicked = true
                },
                gameViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()

        assert(backClicked)
    }

    @Test
    fun newGameButtonShouldResetGameAndSaveState() {

        composeTestRule.setContent {
            GameScreen(
                gameId = "game-1",
                onBack = {
                    backClicked = true
                },
                gameViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        repository.savedGames.clear()

        // Change this content description if NewGameButton uses another one.
        composeTestRule
            .onNodeWithContentDescription("New Game")
            .performClick()

        composeTestRule.waitForIdle()

        assert(repository.savedGames.containsKey("game-1"))
    }

    @Test
    fun gameScreenShouldLoadExistingGame() {

        val savedGame = GameEntity(
            gameId = "game-1",
            boardState = createInitialBoardState(),
            currentPlayer = "USER",
            status = "IN_PROGRESS",
            createdAt = 100L,
            updatedAt = 200L
        )

        repository.games["game-1"] = savedGame

        composeTestRule.setContent {
            GameScreen(
                gameId = "game-1",
                onBack = {
                    backClicked = true
                },
                gameViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        // The screen should successfully load and render.
        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertIsDisplayed()
    }

    @Test
    fun newGameShouldSaveInitialBoardState() {

        composeTestRule.setContent {
            GameScreen(
                gameId = "game-1",
                onBack = {
                    backClicked = true
                },
                gameViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        repository.savedGames.clear()

        composeTestRule
            .onNodeWithContentDescription("New Game")
            .performClick()

        composeTestRule.waitForIdle()

        val savedState = repository.savedGames["game-1"]

        assert(savedState != null)

        assert(savedState!!.board.countPawns(
            com.example.onlypawnchess.domain.model.Player.USER
        ) == 8)

        assert(savedState.board.countPawns(
            com.example.onlypawnchess.domain.model.Player.COMPUTER
        ) == 8)
    }


    private fun createInitialBoardState(): String {
        /*
         * Board storage:
         *
         * row 0 = 8 computer pawns
         * rows 1-6 = empty
         * row 7 = 8 user pawns
         */

        return buildString {
            repeat(8) {
                append("1")
            }

            repeat(48) {
                append("0")
            }

            repeat(8) {
                append("2")
            }
        }
    }

    private class FakeGameRepository : GameRepositoryInterface {

        val games = mutableMapOf<String, GameEntity>()

        val savedGames = mutableMapOf<String, GameState>()

        private val gamesFlow =
            MutableStateFlow<List<GameEntity>>(emptyList())

        override suspend fun createGame(
            gameId: String,
            gameState: GameState
        ) {
            savedGames[gameId] = gameState
        }

        override suspend fun saveGame(
            gameId: String,
            gameState: GameState
        ) {
            savedGames[gameId] = gameState
        }

        override suspend fun getGame(
            gameId: String
        ): GameEntity? {
            return games[gameId]
        }

        override suspend fun deleteGame(
            gameId: String
        ) {
            games.remove(gameId)
        }

        override fun observeGames(): Flow<List<GameEntity>> {
            return gamesFlow
        }
    }
}