package com.example.onlypawnchess.domain.model

enum class Player {
    USER, COMPUTER;

    fun opponent(): Player {
        return when (this) {
            USER -> COMPUTER
            COMPUTER -> USER
        }
    }
}