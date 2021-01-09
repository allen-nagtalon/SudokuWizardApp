package com.example.sudokuWizard

class SudokuSolver(private val boardLayout : String) {
    // Split board layout string into individual cells
    private val cells = boardLayout.split(",").toTypedArray()

    // Initialize board as 2D array of Int
    private val board = Array(9) { i -> IntArray(9) { j -> cells[(i*9)+j].toInt()} }

    fun solve() {

    }

    // Checks the validity of a given cell in the board against the constraints of Sudoku
    fun isValid(row : Int, col : Int) : Boolean {
        return rowCheck(row) && colCheck(col) && boxCheck(row, col)
    }

    // Checks the validity of a given row in the board
    private fun rowCheck(row : Int) : Boolean {
        return false
    }

    // Checks the validity of a given column in the board
    private fun colCheck(col : Int) : Boolean {
        return false
    }

    // Checks the validity of a given 3x3 subsection of the board
    private fun boxCheck(row : Int, col : Int) : Boolean {
        return false
    }
}