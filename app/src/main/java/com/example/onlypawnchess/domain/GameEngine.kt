package com.example.onlypawnchess.domain

import com.example.onlypawnchess.domain.model.Board
import com.example.onlypawnchess.domain.model.BoardEvaluator
import com.example.onlypawnchess.domain.model.ComputerPlayer
import com.example.onlypawnchess.domain.model.GameResult
import com.example.onlypawnchess.domain.model.GameState
import com.example.onlypawnchess.domain.model.Minimax
import com.example.onlypawnchess.domain.model.Move
import com.example.onlypawnchess.domain.model.MoveGenerator
import com.example.onlypawnchess.domain.model.Player
import com.example.onlypawnchess.domain.model.Position
import com.example.onlypawnchess.domain.model.RuleChecker

class GameEngine {

    private val moveGenerator = MoveGenerator()

    private val boardEvaluator = BoardEvaluator()

    private val ruleChecker = RuleChecker(
        moveGenerator = moveGenerator
    )

    private val minimax = Minimax(
        moveGenerator = moveGenerator, evaluator = boardEvaluator, ruleChecker = ruleChecker
    )

    private val computerPlayer = ComputerPlayer(
        minimax = minimax, searchDepth = 3
    )

    private var board = Board()

    private var currentPlayer = Player.USER

    private var result: GameResult = GameResult.InProgress

    fun getState(): GameState {
        return GameState(
            board = board.copy(), currentPlayer = currentPlayer, result = result
        )
    }

    fun restoreState(state: GameState) {
        board = state.board.copy()
        currentPlayer = state.currentPlayer
        result = state.result
    }

    fun getLegalMoves(): List<Move> {
        return moveGenerator.generateLegalMoves(
            board = board, player = currentPlayer
        )
    }

    fun makeUserMove(move: Move): GameResult {

        if (result != GameResult.InProgress) {
            return result
        }

        if (currentPlayer != Player.USER) {
            return result
        }

        if (!ruleChecker.isLegalMove(
                board = board, move = move, player = Player.USER
            )
        ) {
            return result
        }

        board.makeMove(move)

        result = ruleChecker.checkResult(
            board = board, playerToMove = Player.COMPUTER
        )

        if (result != GameResult.InProgress) {
            return result
        }

        currentPlayer = Player.COMPUTER

        return result
    }

    fun makeComputerMove(): GameResult {

        if (result != GameResult.InProgress) {
            return result
        }

        if (currentPlayer != Player.COMPUTER) {
            return result
        }

        val move = computerPlayer.getMove(board)

        if (move == null) {
            result = GameResult.Draw
            return result
        }

        board.makeMove(move)

        result = ruleChecker.checkResult(
            board = board, playerToMove = Player.USER
        )

        if (result != GameResult.InProgress) {
            return result
        }

        currentPlayer = Player.USER

        return result
    }

    fun reset() {
        board = Board()
        currentPlayer = Player.USER
        result = GameResult.InProgress
    }

    fun findMove(
        oldBoard: Board, newBoard: Board, player: Player
    ): Move? {

        val pawnValue = when (player) {
            Player.USER -> Board.USER
            Player.COMPUTER -> Board.COMPUTER
        }

        var from: Position? = null
        var to: Position? = null

        for (row in 0 until Board.SIZE) {

            for (column in 0 until Board.SIZE) {

                val position = Position(row, column)

                val oldValue = oldBoard.get(position)
                val newValue = newBoard.get(position)

                if (oldValue == pawnValue && newValue != pawnValue) {
                    from = position
                }

                if (oldValue != pawnValue && newValue == pawnValue) {
                    to = position
                }
            }
        }

        return if (from != null && to != null) {

            Move(
                from = from, to = to, isCapture = oldBoard.get(to) != Board.EMPTY
            )

        } else {
            null
        }
    }
}