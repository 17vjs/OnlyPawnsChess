package com.example.onlypawnchess.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
class BoardEvaluatorTest {

    private val evaluator = BoardEvaluator()

    private fun emptyBoard(): Board {
        val board = Board()

        for (row in 0 until Board.SIZE) {
            for (column in 0 until Board.SIZE) {
                board.set(
                    Position(row, column),
                    Board.EMPTY
                )
            }
        }

        return board
    }

    @Test
    fun `computer reaching final row should have winning score`() {
        val board = emptyBoard()

        board.set(
            Position(7, 0),
            Board.COMPUTER
        )

        assertEquals(
            BoardEvaluator.WIN_SCORE,
            evaluator.evaluate(board)
        )
    }

    @Test
    fun `user reaching final row should have losing score`() {
        val board = emptyBoard()

        board.set(
            Position(0, 0),
            Board.USER
        )

        assertEquals(
            BoardEvaluator.LOSS_SCORE,
            evaluator.evaluate(board)
        )
    }

    @Test
    fun `no user pawns should be winning score`() {
        val board = emptyBoard()

        board.set(
            Position(3, 0),
            Board.COMPUTER
        )

        assertEquals(
            BoardEvaluator.WIN_SCORE,
            evaluator.evaluate(board)
        )
    }

    @Test
    fun `no computer pawns should be losing score`() {
        val board = emptyBoard()

        board.set(
            Position(3, 0),
            Board.USER
        )

        assertEquals(
            BoardEvaluator.LOSS_SCORE,
            evaluator.evaluate(board)
        )
    }

    @Test
    fun `computer material advantage should increase score`() {
        val board = emptyBoard()

        board.set(Position(3, 0), Board.COMPUTER)
        board.set(Position(3, 1), Board.COMPUTER)
        board.set(Position(3, 2), Board.USER)

        val score = evaluator.evaluate(board)

        assertTrue(score > 0)
    }

    @Test
    fun `user material advantage should decrease score`() {
        val board = emptyBoard()

        board.set(Position(3, 0), Board.COMPUTER)

        board.set(Position(3, 1), Board.USER)
        board.set(Position(3, 2), Board.USER)

        val score = evaluator.evaluate(board)

        assertTrue(score < 0)
    }

    @Test
    fun `computer advancement should improve score`() {
        val board1 = emptyBoard()

        board1.set(
            Position(1, 0),
            Board.COMPUTER
        )

        board1.set(
            Position(6, 7),
            Board.USER
        )

        val board2 = emptyBoard()

        board2.set(
            Position(5, 0),
            Board.COMPUTER
        )

        board2.set(
            Position(6, 7),
            Board.USER
        )

        val score1 = evaluator.evaluate(board1)
        val score2 = evaluator.evaluate(board2)

        assertTrue(score2 > score1)
    }
    @Test
    fun `user advancement should improve computer score negatively`() {
        val board1 = emptyBoard()

        board1.set(
            Position(6, 0),
            Board.USER
        )

        board1.set(
            Position(1, 7),
            Board.COMPUTER
        )

        val board2 = emptyBoard()

        board2.set(
            Position(2, 0),
            Board.USER
        )

        board2.set(
            Position(1, 7),
            Board.COMPUTER
        )

        val score1 = evaluator.evaluate(board1)
        val score2 = evaluator.evaluate(board2)

        assertTrue(score2 < score1)
    }
}