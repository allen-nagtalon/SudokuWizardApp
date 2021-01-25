package com.example.sudokuWizard.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sudokuWizard.engine.GameEngine

class BoardViewModel : ViewModel() {
    val sudokuGame = GameEngine()
}