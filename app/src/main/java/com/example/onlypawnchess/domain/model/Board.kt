package com.example.onlypawnchess.domain.model

class Board {

    companion object {
        const val SIZE = 8
        const val EMPTY = 0
        const val USER = 2
        const val COMPUTER = 1
        fun fromStorageString(value: String): Board {
            require(value.length == SIZE * SIZE) {
                "Invalid board state"
            }

            val board = Board()

            for (row in 0 until SIZE) {
                for (column in 0 until SIZE) {
                    val index = row * SIZE + column

                    board.cells[row][column] = value[index].digitToInt()
                }
            }

            return board
        }

    }

    private val cells = Array(SIZE) {
        IntArray(SIZE) { EMPTY }
    }

    init {
        setupInitialPosition()
    }

    private fun setupInitialPosition() {
        for (column in 0 until SIZE) {
            cells[0][column] = COMPUTER
            cells[SIZE - 1][column] = USER
        }
    }

    fun get(position: Position): Int {
        return cells[position.row][position.column]
    }

    fun set(position: Position, value: Int) {
        cells[position.row][position.column] = value
    }

    fun isEmpty(position: Position): Boolean {
        return get(position) == EMPTY
    }

    fun copy(): Board {
        val copy = Board()

        for (row in 0 until SIZE) {
            for (column in 0 until SIZE) {
                copy.cells[row][column] = cells[row][column]
            }
        }

        return copy
    }

    fun makeMove(move: Move) {
        val piece = get(move.from)

        set(move.from, EMPTY)
        set(move.to, piece)
    }

    fun countPawns(player: Player): Int {
        val value = when (player) {
            Player.USER -> USER
            Player.COMPUTER -> COMPUTER
        }

        var count = 0

        for (row in 0 until SIZE) {
            for (column in 0 until SIZE) {
                if (cells[row][column] == value) {
                    count++
                }
            }
        }

        return count
    }

    fun hasPawnAtFinalRow(player: Player): Boolean {
        val finalRow = when (player) {
            Player.USER -> 0
            Player.COMPUTER -> SIZE - 1
        }

        val pawnValue = when (player) {
            Player.USER -> USER
            Player.COMPUTER -> COMPUTER
        }

        for (column in 0 until SIZE) {
            if (cells[finalRow][column] == pawnValue) {
                return true
            }
        }

        return false
    }

    fun positionsOf(player: Player): List<Position> {
        val pawnValue = when (player) {
            Player.USER -> USER
            Player.COMPUTER -> COMPUTER
        }

        val positions = mutableListOf<Position>()

        for (row in 0 until SIZE) {
            for (column in 0 until SIZE) {
                if (cells[row][column] == pawnValue) {
                    positions.add(Position(row, column))
                }
            }
        }

        return positions
    }

    fun toStorageString(): String {
        return buildString(SIZE * SIZE) {
            for (row in 0 until SIZE) {
                for (column in 0 until SIZE) {
                    append(cells[row][column])
                }
            }
        }
    }

}