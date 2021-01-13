package com.example.sudokuWizard

class SudokuBoard(
    private val boardLayout : String,
    val MAX_VALUE : Int,
    val ROWS : Int,
    val COLS : Int,
    val ROW_SUB_LENGTH : Int,
    val COL_SUB_LENGTH : Int) {

    // General Properties
    val EMPTY_CELL = 0
    val START_INDEX = 0
    val MIN_VALUE = 1

    // Split board layout string into individual cells
    private val cells = boardLayout.split(",").toTypedArray()

    // Initialize board as 2D array of Int
    var board = Array(ROWS) {
            i -> IntArray(COLS) {
            j -> cells[(i*COLS)+j].toInt() }
    }

    operator fun get(i : Int) = board[i]

    fun printBoard() {
        for(row in board) {
            for(col in row) {
                if (col != EMPTY_CELL) print("$col ")
                else print("- ")
            }
            println()
        }
    }

    constructor(boardLayout : String) : this(boardLayout,9,9,9,3,3)
}