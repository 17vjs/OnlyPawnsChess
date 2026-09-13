package com.example.onlypawnchess.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import com.example.onlypawnchess.GameViewModel
import com.example.onlypawnchess.data.local.GameEntity
import com.example.onlypawnchess.data.repository.GameRepositoryInterface
import com.example.onlypawnchess.domain.model.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavedGamesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var repository: FakeGameRepository
    private lateinit var viewModel: GameViewModel

    private var resumedGameId: String? = null
    private var newGameClicked = false

    @Before
    fun setup() {
        repository = FakeGameRepository()
        viewModel = GameViewModel(repository)

        resumedGameId = null
        newGameClicked = false
    }

    @Test
    fun savedGamesScreenShowsEmptyStateWhenThereAreNoGames() {

        composeTestRule.setContent {
            SavedGamesScreen(
                gameViewModel = viewModel,
                onResumeGame = { resumedGameId = it },
                onNewGame = { newGameClicked = true }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Saved Games")
            .assertIsDisplayed()
    }

    @Test
    fun savedGamesScreenShowsSavedGames() {

        repository.setGames(
            listOf(
                createGame("game-1"),
                createGame("game-2")
            )
        )

        composeTestRule.setContent {
            SavedGamesScreen(
                gameViewModel = viewModel,
                onResumeGame = { resumedGameId = it },
                onNewGame = { newGameClicked = true }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("2 saved games")
            .assertIsDisplayed()
    }

    @Test
    fun savedGamesScreenShowsCorrectGameCount() {

        repository.setGames(
            listOf(
                createGame("game-1"),
                createGame("game-2"),
                createGame("game-3")
            )
        )

        composeTestRule.setContent {
            SavedGamesScreen(
                gameViewModel = viewModel,
                onResumeGame = { resumedGameId = it },
                onNewGame = { newGameClicked = true }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("3 saved games")
            .assertIsDisplayed()
    }

    @Test
    fun newGameButtonIsShownWhenSavedGamesExist() {

        repository.setGames(
            listOf(createGame("game-1"))
        )

        composeTestRule.setContent {
            SavedGamesScreen(
                gameViewModel = viewModel,
                onResumeGame = { resumedGameId = it },
                onNewGame = { newGameClicked = true }
            )
        }

        composeTestRule.waitForIdle()

        /*
         * This assumes NewGameFab exposes a "New Game"
         * content description.
         */
        composeTestRule
            .onNodeWithContentDescription("New Game")
            .assertIsDisplayed()
    }

    @Test
    fun newGameButtonInvokesCallback() {

        repository.setGames(
            listOf(createGame("game-1"))
        )

        composeTestRule.setContent {
            SavedGamesScreen(
                gameViewModel = viewModel,
                onResumeGame = { resumedGameId = it },
                onNewGame = { newGameClicked = true }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("New Game")
            .performClick()

        assert(newGameClicked)
    }

    @Test
    fun resumeGameInvokesCallbackWithCorrectGameId() {

        repository.setGames(
            listOf(
                createGame("game-123")
            )
        )

        composeTestRule.setContent {
            SavedGamesScreen(
                gameViewModel = viewModel,
                onResumeGame = { resumedGameId = it },
                onNewGame = { newGameClicked = true }
            )
        }

        composeTestRule.waitForIdle()

        /*
         * This assumes SavedGameCard exposes the game ID
         * as visible text.
         *
         * If it doesn't, we'll add a testTag/contentDescription
         * to SavedGameCard.
         */
        composeTestRule
            .onNodeWithTag("resume_game_game-123")
            .performClick()

        assert(resumedGameId == "game-123")
    }

    @Test
    fun deletingLastGameShowsEmptyState() {

        repository.setGames(
            listOf(createGame("game-1"))
        )

        composeTestRule.setContent {
            SavedGamesScreen(
                gameViewModel = viewModel,
                onResumeGame = { resumedGameId = it },
                onNewGame = { newGameClicked = true }
            )
        }

        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithTag("more_game-1")
            .performClick()
        // 1. Open delete confirmation dialog
        composeTestRule
            .onNodeWithTag("delete_game_game-1")
            .performClick()

        composeTestRule.waitForIdle()

        // 2. Confirm deletion in AlertDialog
        composeTestRule
            .onNodeWithTag("confirm_delete_game-1")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("No saved games")
            .assertIsDisplayed()
    }

    private fun createGame(gameId: String): GameEntity {
        return GameEntity(
            gameId = gameId,
            boardState = createInitialBoardState(),
            currentPlayer = "USER",
            status = "IN_PROGRESS",
            createdAt = 100L,
            updatedAt = 200L
        )
    }

    private fun createInitialBoardState(): String {
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

        private val gamesFlow =
            MutableStateFlow<List<GameEntity>>(emptyList())

        fun setGames(games: List<GameEntity>) {
            gamesFlow.value = games
        }

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
            return gamesFlow.value
                .firstOrNull { it.gameId == gameId }
        }

        override suspend fun deleteGame(
            gameId: String
        ) {
            gamesFlow.value =
                gamesFlow.value.filterNot {
                    it.gameId == gameId
                }
        }

        override fun observeGames(): Flow<List<GameEntity>> {
            return gamesFlow
        }
    }
}