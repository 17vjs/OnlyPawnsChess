package com.example.onlypawnchess.domain

import com.example.onlypawnchess.domain.model.Board
import com.example.onlypawnchess.domain.model.GameResult
import com.example.onlypawnchess.domain.model.GameState
import com.example.onlypawnchess.domain.model.Move
import com.example.onlypawnchess.domain.model.Player
import com.example.onlypawnchess.domain.model.Position
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameEngineTest {

    private lateinit var gameEngine: GameEngine

    @Before
    fun setup() {
        gameEngine = GameEngine()
    }

    // ---------------------------------------------------------
    // Initial State
    // ---------------------------------------------------------

    @Test
    fun `initial state should have user as current player`() {
        val state = gameEngine.getState()

        assertEquals(
            Player.USER,
            state.currentPlayer
        )
    }

    @Test
    fun `initial state should be in progress`() {
        val state = gameEngine.getState()

        assertEquals(
            GameResult.InProgress,
            state.result
        )
    }

    @Test
    fun `initial board should contain eight user pawns`() {
        val state = gameEngine.getState()

        assertEquals(
            8,
            state.board.countPawns(Player.USER)
        )
    }

    @Test
    fun `initial board should contain eight computer pawns`() {
        val state = gameEngine.getState()

        assertEquals(
            8,
            state.board.countPawns(Player.COMPUTER)
        )
    }

    @Test
    fun `initial board should provide legal user moves`() {
        val moves = gameEngine.getLegalMoves()

        assertTrue(moves.isNotEmpty())
    }

    // ---------------------------------------------------------
    // User Move
    // ---------------------------------------------------------

    @Test
    fun `legal user move should move user pawn`() {
        val move = gameEngine.getLegalMoves().first()

        val before = gameEngine.getState().board

        val from = move.from
        val to = move.to

        assertEquals(
            Board.USER,
            before.get(from)
        )

        assertEquals(
            Board.EMPTY,
            before.get(to)
        )

        gameEngine.makeUserMove(move)

        val after = gameEngine.getState().board

        assertEquals(
            Board.EMPTY,
            after.get(from)
        )

        assertEquals(
            Board.USER,
            after.get(to)
        )
    }

    @Test
    fun `legal user move should preserve user pawn count for normal move`() {
        val before = gameEngine.getState().board

        val userPawnCountBefore =
            before.countPawns(Player.USER)

        val move = gameEngine.getLegalMoves().first()

        gameEngine.makeUserMove(move)

        val after = gameEngine.getState().board

        assertEquals(
            userPawnCountBefore,
            after.countPawns(Player.USER)
        )
    }

    @Test
    fun `legal user move should change current player to computer when game continues`() {
        val move = gameEngine.getLegalMoves().first()

        val result = gameEngine.makeUserMove(move)

        if (result == GameResult.InProgress) {
            assertEquals(
                Player.COMPUTER,
                gameEngine.getState().currentPlayer
            )
        }
    }

    @Test
    fun `illegal user move should not change board`() {
        val before = gameEngine.getState().board

        val beforeStorage = before.toStorageString()

        val illegalMove = Move(
            from = Position(0, 0),
            to = Position(7, 7),
            isCapture = false
        )

        gameEngine.makeUserMove(illegalMove)

        val after = gameEngine.getState().board

        assertEquals(
            beforeStorage,
            after.toStorageString()
        )

        assertEquals(
            Player.USER,
            gameEngine.getState().currentPlayer
        )
    }

    @Test
    fun `user cannot move when it is computer turn`() {
        val userMove = gameEngine.getLegalMoves().first()

        val result = gameEngine.makeUserMove(userMove)

        if (result != GameResult.InProgress) {
            return
        }

        val before = gameEngine.getState().board.toStorageString()

        val computerMoves = gameEngine.getLegalMoves()

        if (computerMoves.isEmpty()) {
            return
        }

        val computerMove = computerMoves.first()

        gameEngine.makeUserMove(computerMove)

        val after = gameEngine.getState().board.toStorageString()

        assertEquals(
            before,
            after
        )

        assertEquals(
            Player.COMPUTER,
            gameEngine.getState().currentPlayer
        )
    }

    // ---------------------------------------------------------
    // Computer Move
    // ---------------------------------------------------------

    @Test
    fun `computer should not move when it is user's turn`() {
        val before = gameEngine.getState()

        val beforeBoard =
            before.board.toStorageString()

        gameEngine.makeComputerMove()

        val after = gameEngine.getState()

        assertEquals(
            beforeBoard,
            after.board.toStorageString()
        )

        assertEquals(
            Player.USER,
            after.currentPlayer
        )

        assertEquals(
            GameResult.InProgress,
            after.result
        )
    }

    @Test
    fun `computer should move after user move when game continues`() {
        val userMove = gameEngine.getLegalMoves().first()

        val userResult =
            gameEngine.makeUserMove(userMove)

        if (userResult != GameResult.InProgress) {
            return
        }

        assertEquals(
            Player.COMPUTER,
            gameEngine.getState().currentPlayer
        )

        val beforeComputerMove =
            gameEngine.getState().board.toStorageString()

        val computerResult =
            gameEngine.makeComputerMove()

        val afterComputerMove =
            gameEngine.getState()

        if (computerResult == GameResult.InProgress) {
            assertEquals(
                Player.USER,
                afterComputerMove.currentPlayer
            )

            assertTrue(
                beforeComputerMove !=
                        afterComputerMove.board.toStorageString()
            )
        }
    }

    // ---------------------------------------------------------
    // Reset
    // ---------------------------------------------------------

    @Test
    fun `reset should restore initial board`() {
        val initialBoard =
            gameEngine.getState().board.toStorageString()

        val move =
            gameEngine.getLegalMoves().first()

        gameEngine.makeUserMove(move)

        gameEngine.reset()

        val resetBoard =
            gameEngine.getState().board.toStorageString()

        assertEquals(
            initialBoard,
            resetBoard
        )
    }

    @Test
    fun `reset should restore user as current player`() {
        val move =
            gameEngine.getLegalMoves().first()

        gameEngine.makeUserMove(move)

        gameEngine.reset()

        assertEquals(
            Player.USER,
            gameEngine.getState().currentPlayer
        )
    }

    @Test
    fun `reset should restore game result to in progress`() {
        gameEngine.reset()

        assertEquals(
            GameResult.InProgress,
            gameEngine.getState().result
        )
    }

    // ---------------------------------------------------------
    // Restore State
    // ---------------------------------------------------------

    @Test
    fun `restoreState should restore board`() {
        val originalState =
            gameEngine.getState()

        val originalBoard =
            originalState.board.toStorageString()

        val move =
            gameEngine.getLegalMoves().first()

        gameEngine.makeUserMove(move)

        gameEngine.restoreState(originalState)

        val restoredBoard =
            gameEngine.getState().board.toStorageString()

        assertEquals(
            originalBoard,
            restoredBoard
        )
    }

    @Test
    fun `restoreState should restore current player`() {
        val originalState =
            gameEngine.getState()

        val move =
            gameEngine.getLegalMoves().first()

        gameEngine.makeUserMove(move)

        gameEngine.restoreState(originalState)

        assertEquals(
            originalState.currentPlayer,
            gameEngine.getState().currentPlayer
        )
    }

    @Test
    fun `restoreState should restore result`() {
        val originalState =
            gameEngine.getState()

        gameEngine.restoreState(originalState)

        assertEquals(
            originalState.result,
            gameEngine.getState().result
        )
    }

    // ---------------------------------------------------------
    // Game Finished
    // ---------------------------------------------------------

    @Test
    fun `user move should be ignored after game is finished`() {
        val state =
            gameEngine.getState()

        val finishedState = GameState(
            board = state.board,
            currentPlayer = Player.USER,
            result = GameResult.Draw
        )

        gameEngine.restoreState(finishedState)

        val before =
            gameEngine.getState().board.toStorageString()

        val move = Move(
            from = Position(7, 0),
            to = Position(6, 0),
            isCapture = false
        )

        val result =
            gameEngine.makeUserMove(move)

        val after =
            gameEngine.getState().board.toStorageString()

        assertEquals(
            GameResult.Draw,
            result
        )

        assertEquals(
            before,
            after
        )
    }

    @Test
    fun `computer move should be ignored after game is finished`() {
        val state =
            gameEngine.getState()

        val finishedState = GameState(
            board = state.board,
            currentPlayer = Player.COMPUTER,
            result = GameResult.Draw
        )

        gameEngine.restoreState(finishedState)

        val before =
            gameEngine.getState().board.toStorageString()

        val result =
            gameEngine.makeComputerMove()

        val after =
            gameEngine.getState().board.toStorageString()

        assertEquals(
            GameResult.Draw,
            result
        )

        assertEquals(
            before,
            after
        )
    }

    // ---------------------------------------------------------
    // findMove()
    // ---------------------------------------------------------

    @Test
    fun `findMove should return null when boards are identical`() {
        val oldBoard = Board()
        val newBoard = oldBoard.copy()

        val move = gameEngine.findMove(
            oldBoard = oldBoard,
            newBoard = newBoard,
            player = Player.USER
        )

        assertNull(move)
    }

    @Test
    fun `findMove should find user pawn movement`() {
        val oldBoard = Board()
        val newBoard = oldBoard.copy()

        val expectedMove = Move(
            from = Position(7, 0),
            to = Position(6, 0),
            isCapture = false
        )

        newBoard.makeMove(expectedMove)

        val actualMove = gameEngine.findMove(
            oldBoard = oldBoard,
            newBoard = newBoard,
            player = Player.USER
        )

        assertNotNull(actualMove)

        assertEquals(
            expectedMove.from,
            actualMove?.from
        )

        assertEquals(
            expectedMove.to,
            actualMove?.to
        )

        assertEquals(
            false,
            actualMove?.isCapture
        )
    }

    @Test
    fun `findMove should identify computer pawn movement`() {
        val oldBoard = Board()
        val newBoard = oldBoard.copy()

        val expectedMove = Move(
            from = Position(0, 0),
            to = Position(1, 0),
            isCapture = false
        )

        newBoard.makeMove(expectedMove)

        val actualMove = gameEngine.findMove(
            oldBoard = oldBoard,
            newBoard = newBoard,
            player = Player.COMPUTER
        )

        assertNotNull(actualMove)

        assertEquals(
            expectedMove.from,
            actualMove?.from
        )

        assertEquals(
            expectedMove.to,
            actualMove?.to
        )

        assertEquals(
            false,
            actualMove?.isCapture
        )
    }

    @Test
    fun `findMove should detect capture`() {
        val oldBoard = Board()
        val newBoard = oldBoard.copy()

        // User pawn moves diagonally and captures computer pawn.
        oldBoard.set(
            Position(6, 1),
            Board.USER
        )

        oldBoard.set(
            Position(5, 2),
            Board.COMPUTER
        )

        newBoard.set(
            Position(6, 1),
            Board.EMPTY
        )

        newBoard.set(
            Position(5, 2),
            Board.USER
        )

        val move = gameEngine.findMove(
            oldBoard = oldBoard,
            newBoard = newBoard,
            player = Player.USER
        )

        assertNotNull(move)

        assertEquals(
            Position(6, 1),
            move?.from
        )

        assertEquals(
            Position(5, 2),
            move?.to
        )

        assertTrue(
            move?.isCapture == true
        )
    }
}