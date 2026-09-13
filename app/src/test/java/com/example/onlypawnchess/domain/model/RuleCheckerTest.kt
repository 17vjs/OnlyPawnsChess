package com.example.onlypawnchess.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RuleCheckerTest {

    private lateinit var moveGenerator: MoveGenerator
    private lateinit var ruleChecker: RuleChecker

    @Before
    fun setup() {
        moveGenerator = MoveGenerator()
        ruleChecker = RuleChecker(moveGenerator)
    }

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
    fun `legal move should return true`() {
        val board = emptyBoard()

        board.set(
            Position(6, 3),
            Board.USER
        )

        val move = Move(
            from = Position(6, 3),
            to = Position(5, 3),
            isCapture = false
        )

        assertTrue(
            ruleChecker.isLegalMove(
                board = board,
                move = move,
                player = Player.USER
            )
        )
    }

    @Test
    fun `illegal move should return false`() {
        val board = emptyBoard()

        board.set(
            Position(6, 3),
            Board.USER
        )

        val move = Move(
            from = Position(6, 3),
            to = Position(4, 3),
            isCapture = false
        )

        assertFalse(
            ruleChecker.isLegalMove(
                board = board,
                move = move,
                player = Player.USER
            )
        )
    }

    @Test
    fun `user reaching final row should result in user win`() {
        val board = emptyBoard()

        board.set(
            Position(0, 3),
            Board.USER
        )

        assertEquals(
            GameResult.UserWins,
            ruleChecker.checkResult(
                board = board,
                playerToMove = Player.COMPUTER
            )
        )
    }

    @Test
    fun `computer reaching final row should result in computer win`() {
        val board = emptyBoard()

        board.set(
            Position(7, 3),
            Board.COMPUTER
        )

        assertEquals(
            GameResult.ComputerWins,
            ruleChecker.checkResult(
                board = board,
                playerToMove = Player.USER
            )
        )
    }

    @Test
    fun `no user pawns should result in computer win`() {
        val board = emptyBoard()

        board.set(
            Position(3, 3),
            Board.COMPUTER
        )

        assertEquals(
            GameResult.ComputerWins,
            ruleChecker.checkResult(
                board = board,
                playerToMove = Player.USER
            )
        )
    }

    @Test
    fun `no computer pawns should result in user win`() {
        val board = emptyBoard()

        board.set(
            Position(3, 3),
            Board.USER
        )

        assertEquals(
            GameResult.UserWins,
            ruleChecker.checkResult(
                board = board,
                playerToMove = Player.COMPUTER
            )
        )
    }

    @Test
    fun `no legal moves should result in draw`() {
        val board = emptyBoard()

        // User pawn is blocked by a computer pawn.
        board.set(
            Position(6, 3),
            Board.USER
        )

        board.set(
            Position(5, 3),
            Board.COMPUTER
        )

        assertEquals(
            GameResult.Draw,
            ruleChecker.checkResult(
                board = board,
                playerToMove = Player.USER
            )
        )
    }

    @Test
    fun `game should be in progress when legal moves exist`() {
        val board = emptyBoard()

        board.set(
            Position(6, 3),
            Board.USER
        )

        board.set(
            Position(0, 0),
            Board.COMPUTER
        )

        assertEquals(
            GameResult.InProgress,
            ruleChecker.checkResult(
                board = board,
                playerToMove = Player.USER
            )
        )
    }
}