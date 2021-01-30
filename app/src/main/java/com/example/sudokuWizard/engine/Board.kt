package com.example.sudokuWizard.engine

class Board(
    private val cells: List<Cell>,
    val MAX_VALUE : Int,
    val ROWS : Int,
    val COLS : Int,
    val ROW_SUB_LENGTH : Int,
    val COL_SUB_LENGTH : Int) {

    // General Properties
    val EMPTY_CELL = 0
    val START_INDEX = 0
    val MIN_VALUE = 1

    // Initialize board as 2D array of Int
    var board = Array(ROWS) {
            i -> Array(COLS) {
            j -> cells[(i*COLS)+j] }
    }

    operator fun get(i : Int) = board[i]

    fun getCell(row : Int, col : Int) = board[row][col]

    fun clear() {
        board.forEach {
            it.forEach {
                it.value = 0
                it.permanent = false
            }
        }
    }

    fun printBoard() {
        for(row in board) {
            for(col in row) {
                if (col.value != EMPTY_CELL) {
                    if(col.value > 9) print("${(col.value+55).toChar()} ")
                    else print("$col ")
                }
                else print("- ")
            }
            println()
        }
    }

    constructor(cells : List<Cell>) : this(cells,9,9,9,3,3)
}