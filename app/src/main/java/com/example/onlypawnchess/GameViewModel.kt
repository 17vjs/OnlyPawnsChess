package com.example.onlypawnchess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlypawnchess.data.local.GameEntity
import com.example.onlypawnchess.data.local.toGameState
import com.example.onlypawnchess.data.repository.GameRepository
import com.example.onlypawnchess.data.repository.GameRepositoryInterface
import com.example.onlypawnchess.domain.GameEngine
import com.example.onlypawnchess.domain.model.GameState

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: GameRepositoryInterface
) : ViewModel(){

    val games: StateFlow<List<GameEntity>> =
        repository
            .observeGames()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun deleteGame(gameId: String) {
        viewModelScope.launch {
            repository.deleteGame(gameId)
        }
    }


    fun createNewGame(gameId: String) {
        viewModelScope.launch {
            val gameEngine = GameEngine()
            val gameState = gameEngine.getState()

            repository.createGame(
                gameId = gameId,
                gameState = gameState
            )
        }
    }

    fun saveGame(
        gameId: String,
        gameState: GameState
    ) {
        viewModelScope.launch {
            repository.saveGame(
                gameId = gameId,
                gameState = gameState
            )
        }
    }
    suspend fun loadGame(gameId: String): GameState? {
        return repository
            .getGame(gameId)
            ?.toGameState()
    }

}