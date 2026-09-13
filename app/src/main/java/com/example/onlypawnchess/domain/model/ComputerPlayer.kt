package com.example.onlypawnchess.domain.model

class ComputerPlayer(
    private val minimax: Minimax, private val searchDepth: Int = 3
) {
    fun getMove(board: Board): Move? {
        return minimax.findBestMove(
            board = board, depth = searchDepth
        )
    }
}