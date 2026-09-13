package com.example.onlypawnchess.domain.model

class MoveGenerator {

    fun generateLegalMoves(
        board: Board, player: Player
    ): List<Move> {

        val moves = mutableListOf<Move>()

        val direction = when (player) {
            Player.COMPUTER -> 1
            Player.USER -> -1
        }

        for (position in board.positionsOf(player)) {

            val forward = Position(
                row = position.row + direction, column = position.column
            )

            if (forward.isValid() && board.isEmpty(forward)) {
                moves.add(
                    Move(
                        from = position, to = forward, isCapture = false
                    )
                )
            }

            val captureColumns = listOf(
                position.column - 1, position.column + 1
            )

            for (column in captureColumns) {

                val capturePosition = Position(
                    row = position.row + direction, column = column
                )

                if (!capturePosition.isValid()) {
                    continue
                }

                val opponentValue = when (player.opponent()) {
                    Player.USER -> Board.USER
                    Player.COMPUTER -> Board.COMPUTER
                }

                if (board.get(capturePosition) == opponentValue) {
                    moves.add(
                        Move(
                            from = position, to = capturePosition, isCapture = true
                        )
                    )
                }
            }
        }

        return moves
    }
}