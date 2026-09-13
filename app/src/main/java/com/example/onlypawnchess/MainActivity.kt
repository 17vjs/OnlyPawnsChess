package com.example.onlypawnchess

import GameViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onlypawnchess.data.local.DatabaseProvider
import com.example.onlypawnchess.data.repository.GameRepository
import com.example.onlypawnchess.ui.screens.GameScreen
import com.example.onlypawnchess.ui.screens.SavedGamesScreen
import com.example.onlypawnchess.ui.theme.OnlyPawnChessTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Avoiding Hilt for simplicity
        val database = DatabaseProvider.getDatabase(applicationContext)
        val repository = GameRepository(database.gameDao())

        gameViewModel = ViewModelProvider(
            this,
            GameViewModelFactory(repository)
        )[GameViewModel::class.java]

        setContent {
            OnlyPawnChessTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "saved_games"
                ) {
                    composable("saved_games") {
                        SavedGamesScreen(
                            gameViewModel = gameViewModel,

                            onNewGame = {
                                val newGameId = UUID.randomUUID().toString()
                                gameViewModel.createNewGame(newGameId)
                                navController.navigate("game/$newGameId")
                            },

                            onResumeGame = { gameId ->
                                navController.navigate("game/$gameId")
                            }
                        )
                    }
                    composable(
                        route = "game/{gameId}"
                    ) { backStackEntry ->
                        val gameId = backStackEntry.arguments?.getString("gameId")
                            ?: error("gameId is required")
                        GameScreen(
                            gameViewModel = gameViewModel,
                            gameId = gameId,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                }
            }
        }
    }
}
