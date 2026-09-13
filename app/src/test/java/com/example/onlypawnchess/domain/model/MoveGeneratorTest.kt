package com.example.onlypawnchess.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MoveGeneratorTest {

    private lateinit var moveGenerator: MoveGenerator

    @Before
    fun setup() {
        moveGenerator = MoveGenerator()
    }



    @Test
    fun `initial user position should generate eight forward moves`() {
        val board = Board()

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertEquals(8, moves.size)

        assertTrue(
            moves.all { !it.isCapture }
        )
    }

    @Test
    fun `initial computer position should generate eight forward moves`() {
        val board = Board()

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.COMPUTER
        )

        assertEquals(8, moves.size)

        assertTrue(
            moves.all { !it.isCapture }
        )
    }


    @Test
    fun `user pawn should move one row forward`() {
        val board = Board()

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertTrue(
            moves.any {
                it.from == Position(7, 0) &&
                        it.to == Position(6, 0) &&
                        !it.isCapture
            }
        )
    }

    @Test
    fun `computer pawn should move one row forward`() {
        val board = Board()

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.COMPUTER
        )

        assertTrue(
            moves.any {
                it.from == Position(0, 0) &&
                        it.to == Position(1, 0) &&
                        !it.isCapture
            }
        )
    }



    @Test
    fun `user pawn should not move forward when blocked`() {
        val board = Board()

        board.set(
            Position(6, 0),
            Board.USER
        )

        board.set(
            Position(5, 0),
            Board.COMPUTER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertFalse(
            moves.any {
                it.from == Position(6, 0) &&
                        it.to == Position(5, 0)
            }
        )
    }

    @Test
    fun `computer pawn should not move forward when blocked`() {
        val board = Board()

        board.set(
            Position(1, 0),
            Board.COMPUTER
        )

        board.set(
            Position(2, 0),
            Board.USER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.COMPUTER
        )

        assertFalse(
            moves.any {
                it.from == Position(1, 0) &&
                        it.to == Position(2, 0)
            }
        )
    }



    @Test
    fun `user pawn should capture computer pawn diagonally left`() {
        val board = Board()

        board.set(
            Position(6, 3),
            Board.USER
        )

        board.set(
            Position(5, 2),
            Board.COMPUTER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertTrue(
            moves.any {
                it.from == Position(6, 3) &&
                        it.to == Position(5, 2) &&
                        it.isCapture
            }
        )
    }

    @Test
    fun `user pawn should capture computer pawn diagonally right`() {
        val board = Board()

        board.set(
            Position(6, 3),
            Board.USER
        )

        board.set(
            Position(5, 4),
            Board.COMPUTER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertTrue(
            moves.any {
                it.from == Position(6, 3) &&
                        it.to == Position(5, 4) &&
                        it.isCapture
            }
        )
    }

    @Test
    fun `computer pawn should capture user pawn diagonally left`() {
        val board = Board()

        board.set(
            Position(1, 3),
            Board.COMPUTER
        )

        board.set(
            Position(2, 2),
            Board.USER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.COMPUTER
        )

        assertTrue(
            moves.any {
                it.from == Position(1, 3) &&
                        it.to == Position(2, 2) &&
                        it.isCapture
            }
        )
    }

    @Test
    fun `computer pawn should capture user pawn diagonally right`() {
        val board = Board()

        board.set(
            Position(1, 3),
            Board.COMPUTER
        )

        board.set(
            Position(2, 4),
            Board.USER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.COMPUTER
        )

        assertTrue(
            moves.any {
                it.from == Position(1, 3) &&
                        it.to == Position(2, 4) &&
                        it.isCapture
            }
        )
    }


    @Test
    fun `user pawn should not capture opponent directly forward`() {
        val board = Board()

        board.set(
            Position(6, 3),
            Board.USER
        )

        board.set(
            Position(5, 3),
            Board.COMPUTER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertFalse(
            moves.any {
                it.from == Position(6, 3) &&
                        it.to == Position(5, 3)
            }
        )
    }

    @Test
    fun `computer pawn should not capture opponent directly forward`() {
        val board = Board()

        board.set(
            Position(1, 3),
            Board.COMPUTER
        )

        board.set(
            Position(2, 3),
            Board.USER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.COMPUTER
        )

        assertFalse(
            moves.any {
                it.from == Position(1, 3) &&
                        it.to == Position(2, 3)
            }
        )
    }


    @Test
    fun `user pawn should not capture another user pawn`() {
        val board = Board()

        board.set(
            Position(6, 3),
            Board.USER
        )

        board.set(
            Position(5, 2),
            Board.USER
        )

        board.set(
            Position(5, 4),
            Board.USER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertFalse(
            moves.any {
                it.from == Position(6, 3) &&
                        it.isCapture
            }
        )
    }

    @Test
    fun `computer pawn should not capture another computer pawn`() {
        val board = Board()

        board.set(
            Position(1, 3),
            Board.COMPUTER
        )

        board.set(
            Position(2, 2),
            Board.COMPUTER
        )

        board.set(
            Position(2, 4),
            Board.COMPUTER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.COMPUTER
        )

        assertFalse(
            moves.any {
                it.from == Position(1, 3) &&
                        it.isCapture
            }
        )
    }


    @Test
    fun `user pawn on left edge should not generate invalid left capture`() {
        val board = Board()

        board.set(
            Position(6, 0),
            Board.USER
        )

        board.set(
            Position(5, 1),
            Board.COMPUTER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertTrue(
            moves.none {
                it.to.column < 0
            }
        )

        assertTrue(
            moves.any {
                it.from == Position(6, 0) &&
                        it.to == Position(5, 1) &&
                        it.isCapture
            }
        )
    }

    @Test
    fun `computer pawn on left edge should not generate invalid left capture`() {
        val board = Board()

        board.set(
            Position(1, 0),
            Board.COMPUTER
        )

        board.set(
            Position(2, 1),
            Board.USER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.COMPUTER
        )

        assertTrue(
            moves.none {
                it.to.column < 0
            }
        )

        assertTrue(
            moves.any {
                it.from == Position(1, 0) &&
                        it.to == Position(2, 1) &&
                        it.isCapture
            }
        )
    }

    @Test
    fun `user pawn on right edge should not generate invalid right capture`() {
        val board = Board()

        board.set(
            Position(6, 7),
            Board.USER
        )

        board.set(
            Position(5, 6),
            Board.COMPUTER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertTrue(
            moves.none {
                it.to.column > 7
            }
        )

        assertTrue(
            moves.any {
                it.from == Position(6, 7) &&
                        it.to == Position(5, 6) &&
                        it.isCapture
            }
        )
    }


    @Test
    fun `player with no pawns should have no legal moves`() {
        val board = Board()

        for (position in board.positionsOf(Player.USER)) {
            board.set(position, Board.EMPTY)
        }

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertTrue(moves.isEmpty())
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
    fun `pawn should generate forward and both captures`() {
        val board = emptyBoard()


        board.set(
            Position(6, 3),
            Board.USER
        )

        board.set(
            Position(5, 2),
            Board.COMPUTER
        )

        board.set(
            Position(5, 4),
            Board.COMPUTER
        )

        val moves = moveGenerator.generateLegalMoves(
            board = board,
            player = Player.USER
        )

        assertEquals(3, moves.size)

        assertTrue(
            moves.any {
                it.from == Position(6, 3) &&
                        it.to == Position(5, 3) &&
                        !it.isCapture
            }
        )

        assertTrue(
            moves.any {
                it.from == Position(6, 3) &&
                        it.to == Position(5, 2) &&
                        it.isCapture
            }
        )

        assertTrue(
            moves.any {
                it.from == Position(6, 3) &&
                        it.to == Position(5, 4) &&
                        it.isCapture
            }
        )
    }
}