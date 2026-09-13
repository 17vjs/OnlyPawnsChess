package com.example.onlypawnchess.domain.model

class BoardEvaluator {

    companion object {
        const val WIN_SCORE = 100_000
        const val LOSS_SCORE = -100_000
    }

    fun evaluate(board: Board): Int {

        if (board.hasPawnAtFinalRow(Player.COMPUTER)) {
            return WIN_SCORE
        }

        if (board.hasPawnAtFinalRow(Player.USER)) {
            return LOSS_SCORE
        }

        if (board.countPawns(Player.USER) == 0) {
            return WIN_SCORE
        }

        if (board.countPawns(Player.COMPUTER) == 0) {
            return LOSS_SCORE
        }

        var score = 0

        score += board.countPawns(Player.COMPUTER) * 100
        score -= board.countPawns(Player.USER) * 100

        for (position in board.positionsOf(Player.COMPUTER)) {
            score += position.row * 10
        }

        for (position in board.positionsOf(Player.USER)) {
            score -= (Board.SIZE - 1 - position.row) * 10
        }

        return score
    }
}