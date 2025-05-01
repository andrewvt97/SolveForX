package com.example.solveforx.ui

data class GameUIState(
    val currentEquation: String = "",
    val isGuessedXWrong: Boolean = false,
    val score: Int = 0,
    val currentEquationCount: Int = 1,
    val isGameOver: Boolean = false,
)
