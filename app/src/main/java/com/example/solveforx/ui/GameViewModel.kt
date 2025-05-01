package com.example.solveforx.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.solveforx.data.MAX_NO_OF_EQUATIONS
import com.example.solveforx.data.SCORE_INCREASE
import com.example.solveforx.data.allEquations
import com.example.solveforx.data.answers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {
    // create in class not before, slide is wrong
    private val _uiState = MutableStateFlow(GameUIState())
    private var usedEquations: MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
        private set

    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow()
    init {
        resetGame()
    }

    private lateinit var currentEquation: String
    private lateinit var currentAnswer: String


    private fun pickRandomEquation():String{
        val index = (0..49).random()
        currentEquation = allEquations.elementAt(index)
        currentAnswer = answers[index]

        if (usedEquations.contains(currentEquation)){
            return pickRandomEquation()
        }else{
            usedEquations.add(currentEquation)
            return currentEquation
        }
    }

    private fun updateGameState(updatedScore: Int){
        if (usedEquations.size == MAX_NO_OF_EQUATIONS) {
            _uiState.update { currentState ->
                currentState.copy(
                    score = updatedScore,
                    isGuessedXWrong = false,
                    isGameOver = true,
                )
            }
        }
        else {
            _uiState.update { currentState ->
                currentState.copy(
                    score = updatedScore,
                    currentEquation = pickRandomEquation(),
                    isGuessedXWrong = false,
                    currentEquationCount = currentState.currentEquationCount.inc()
                )
            }
        }
    }

    fun checkUserGuess(){
        if (userGuess == currentAnswer){
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        }
        else{
            _uiState.update{currentState ->
            currentState.copy(isGuessedXWrong = true)
            }
        }
        updateUserGuess("")
    }

    fun skipEquation(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

    fun updateUserGuess(guessedX:String){
        userGuess = guessedX
    }

    fun resetGame(){
        usedEquations.clear()
        _uiState.value = GameUIState(currentEquation = pickRandomEquation())
    }
}