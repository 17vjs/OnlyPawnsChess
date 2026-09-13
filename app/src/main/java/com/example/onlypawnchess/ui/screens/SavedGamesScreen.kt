package com.example.onlypawnchess.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.onlypawnchess.GameViewModel
import com.example.onlypawnchess.ui.components.EmptyGamesState
import com.example.onlypawnchess.ui.components.NewGameFab
import com.example.onlypawnchess.ui.components.SavedGameCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedGamesScreen(
    gameViewModel: GameViewModel, onResumeGame: (String) -> Unit, onNewGame: () -> Unit
) {
    val games by gameViewModel.games.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Color(0xFF111111),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Saved Games", fontWeight = FontWeight.Bold
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF111111),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },

        floatingActionButton = {
            if (games.isNotEmpty()) NewGameFab(
                onClick = onNewGame
            )
        }) { paddingValues ->

        if (games.isEmpty()) {
            EmptyGamesState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), onNewGame = onNewGame
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${games.size} saved games",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                items(
                    items = games, key = { it.gameId }) { game ->

                    SavedGameCard(
                        game = game,
                        onResume = {
                            onResumeGame(game.gameId)
                        },
                        onDelete = {
                            gameViewModel?.deleteGame(game.gameId)
                        },

                        )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}







