package com.example.sudokuWizard.engine

interface SudokuSolver {
    fun solve() : Boolean
    fun checkComplete() : Int

    companion object {
        val SOLVED = 0
        val INCORRECT = 1
        val INCOMPLETE = 2
    }
}