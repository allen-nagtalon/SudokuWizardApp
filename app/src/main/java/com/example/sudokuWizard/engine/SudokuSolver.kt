package com.example.sudokuWizard.engine

interface SudokuSolver {
    fun solve() : Boolean
    fun checkComplete() : Boolean
}