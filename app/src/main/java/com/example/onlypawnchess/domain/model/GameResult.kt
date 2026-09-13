package com.example.onlypawnchess.domain.model

sealed class GameResult {

    data object InProgress : GameResult()

    data object UserWins : GameResult()

    data object ComputerWins : GameResult()

    data object Draw : GameResult()
}