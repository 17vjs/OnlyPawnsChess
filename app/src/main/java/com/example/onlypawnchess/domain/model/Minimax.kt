package com.example.onlypawnchess.domain.model

class Minimax(
    private val moveGenerator: MoveGenerator,
    private val evaluator: BoardEvaluator,
    private val ruleChecker: RuleChecker
) {

    fun findBestMove(
        board: Board, depth: Int
    ): Move? {

        val moves = moveGenerator.generateLegalMoves(
            board, Player.COMPUTER
        )

        if (moves.isEmpty()) {
            return null
        }

        var bestMove: Move? = null
        var bestScore = Int.MIN_VALUE

        for (move in moves) {

            val newBoard = board.copy()
            newBoard.makeMove(move)

            val score = minimax(
                board = newBoard,
                playerToMove = Player.USER,
                depth = depth - 1,
                alpha = Int.MIN_VALUE,
                beta = Int.MAX_VALUE
            )

            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }

        return bestMove
    }

    private fun minimax(
        board: Board, playerToMove: Player, depth: Int, alpha: Int, beta: Int
    ): Int {

        val result = ruleChecker.checkResult(
            board, playerToMove
        )

        if (result != GameResult.InProgress) {
            return evaluator.evaluate(board)
        }

        if (depth <= 0) {
            return evaluator.evaluate(board)
        }

        val moves = moveGenerator.generateLegalMoves(
            board, playerToMove
        )

        if (moves.isEmpty()) {
            return evaluator.evaluate(board)
        }

        var currentAlpha = alpha
        var currentBeta = beta

        return if (playerToMove == Player.COMPUTER) {

            var bestScore = Int.MIN_VALUE

            for (move in moves) {

                val newBoard = board.copy()
                newBoard.makeMove(move)

                val score = minimax(
                    board = newBoard,
                    playerToMove = Player.USER,
                    depth = depth - 1,
                    alpha = currentAlpha,
                    beta = currentBeta
                )

                bestScore = maxOf(bestScore, score)
                currentAlpha = maxOf(currentAlpha, bestScore)

                if (currentBeta <= currentAlpha) {
                    break
                }
            }

            bestScore

        } else {

            var bestScore = Int.MAX_VALUE

            for (move in moves) {

                val newBoard = board.copy()
                newBoard.makeMove(move)

                val score = minimax(
                    board = newBoard,
                    playerToMove = Player.COMPUTER,
                    depth = depth - 1,
                    alpha = currentAlpha,
                    beta = currentBeta
                )

                bestScore = minOf(bestScore, score)
                currentBeta = minOf(currentBeta, bestScore)

                if (currentBeta <= currentAlpha) {
                    break
                }
            }

            bestScore
        }
    }
}