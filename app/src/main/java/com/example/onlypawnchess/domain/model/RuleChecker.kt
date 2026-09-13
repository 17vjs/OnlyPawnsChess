package com.example.onlypawnchess.domain.model

class RuleChecker(
    private val moveGenerator: MoveGenerator
) {

    fun isLegalMove(
        board: Board, move: Move, player: Player
    ): Boolean {
        return moveGenerator.generateLegalMoves(board, player).contains(move)
    }

    fun checkResult(
        board: Board, playerToMove: Player
    ): GameResult {

        if (board.hasPawnAtFinalRow(Player.USER)) {
            return GameResult.UserWins
        }

        if (board.hasPawnAtFinalRow(Player.COMPUTER)) {
            return GameResult.ComputerWins
        }

        if (board.countPawns(Player.USER) == 0) {
            return GameResult.ComputerWins
        }

        if (board.countPawns(Player.COMPUTER) == 0) {
            return GameResult.UserWins
        }

        val legalMoves = moveGenerator.generateLegalMoves(
            board, playerToMove
        )

        if (legalMoves.isEmpty()) {
            return GameResult.Draw
        }

        return GameResult.InProgress
    }
}