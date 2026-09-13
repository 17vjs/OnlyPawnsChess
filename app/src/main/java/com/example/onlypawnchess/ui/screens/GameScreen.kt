package com.example.onlypawnchess.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.onlypawnchess.GameViewModel
import com.example.onlypawnchess.R
import com.example.onlypawnchess.domain.model.Board
import com.example.onlypawnchess.domain.GameEngine
import com.example.onlypawnchess.domain.model.GameResult
import com.example.onlypawnchess.domain.model.GameState
import com.example.onlypawnchess.domain.model.Move
import com.example.onlypawnchess.domain.model.Player
import com.example.onlypawnchess.domain.model.Position
import com.example.onlypawnchess.ui.components.BackButton
import com.example.onlypawnchess.ui.components.ChessBoard
import com.example.onlypawnchess.ui.components.GameResultOverlay
import com.example.onlypawnchess.ui.components.NewGameButton

@Composable
fun GameScreen(
    gameId: String, onBack: () -> Unit, gameViewModel: GameViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    val gameEngine = remember {
        GameEngine()
    }

    var gameState by remember {
        mutableStateOf<GameState?>(null)
    }

    var selectedPosition by remember {
        mutableStateOf<Position?>(null)
    }

    var animatedMove by remember {
        mutableStateOf<Move?>(null)
    }

    var isComputerThinking by remember {
        mutableStateOf(false)
    }

    val latestGameState by rememberUpdatedState(gameState)

    // Load saved game
    LaunchedEffect(gameId) {
        val savedState = gameViewModel.loadGame(gameId)

        if (savedState != null) {
            gameEngine.restoreState(savedState)
            gameState = gameEngine.getState()
        } else {
            // New game
            gameState = gameEngine.getState()
        }
    }

    // Save game when screen is destroyed
    DisposableEffect(gameId) {
        onDispose {
            latestGameState?.let { state ->
                gameViewModel.saveGame(
                    gameId = gameId, gameState = state
                )
            }
        }
    }


    LaunchedEffect(isComputerThinking) {


        if (!isComputerThinking) return@LaunchedEffect


        gameState?.let {
            if (it.result == GameResult.InProgress) {

                val previousBoard = it.board

                gameEngine.makeComputerMove()

                val newState = gameEngine.getState()

                animatedMove = gameEngine.findMove(
                    oldBoard = previousBoard, newBoard = newState.board, player = Player.COMPUTER
                )

                gameState = newState
                gameViewModel.saveGame(
                    gameId = gameId, gameState = newState
                )
                delay(400)

                animatedMove = null
            }
        }

        isComputerThinking = false
    }

    fun startOver() {
        gameEngine.reset()
        gameState = gameEngine.getState()

        selectedPosition = null
        animatedMove = null
        isComputerThinking = false
        gameState?.let {
            gameViewModel.saveGame(
                gameId = gameId, gameState = it
            )
        }

    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
    ) { innerPadding ->

        gameState?.let { state ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                BackButton {
                    onBack()
                }
                ChessBoard(
                    board = state.board,
                    selectedPosition = selectedPosition,
                    animatedMove = animatedMove,
                    onSquareClick = { position ->

                        if (state.result != GameResult.InProgress) {
                            return@ChessBoard
                        }

                        if (state.currentPlayer != Player.USER) {
                            return@ChessBoard
                        }

                        if (isComputerThinking) {
                            return@ChessBoard
                        }

                        val piece = state.board.get(position)

                        if (selectedPosition == null) {

                            if (piece == Board.USER) {
                                selectedPosition = position
                            }

                        } else {

                            val move = gameEngine.getLegalMoves().firstOrNull {
                                    it.from == selectedPosition && it.to == position
                                }

                            if (move != null) {

                                val previousBoard = state.board

                                gameEngine.makeUserMove(move)

                                val newState = gameEngine.getState()

                                animatedMove = gameEngine.findMove(
                                    oldBoard = previousBoard,
                                    newBoard = newState.board,
                                    player = Player.USER
                                )

                                gameState = newState
                                gameViewModel.saveGame(
                                    gameId = gameId, gameState = newState
                                )
                                selectedPosition = null

                                coroutineScope.launch {

                                    delay(450)

                                    animatedMove = null

                                    if (state.result == GameResult.InProgress) {
                                        isComputerThinking = true
                                    }
                                }

                            } else {

                                selectedPosition = if (piece == Board.USER) {
                                    position
                                } else {
                                    null
                                }
                            }
                        }
                    })

                AnimatedVisibility(
                    visible = isComputerThinking,
                    modifier = Modifier.align(Alignment.TopCenter),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = stringResource(R.string.computer_thinking),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                NewGameButton(

                    onClick = {
                        startOver()
                    })

                GameResultOverlay(result = state.result, onNewGame = {
                    startOver()
                }, onBack = {
                    onBack()
                })
            }
        }
    }
}


