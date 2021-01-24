package com.example.sudokuWizard.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sudokuWizard.engine.SudokuBoard

class SudokuBoardViewModel : ViewModel() {
    val board = SudokuBoard()
}