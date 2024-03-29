package com.example.sudokuWizard.engine

class Board (
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

    // Initialize board as 2D array of Cells
    var board = Array(ROWS) {
            i -> Array(COLS) {
            j -> cells[(i*COLS)+j] }
    }

    operator fun get(i : Int) = board[i]

    fun getCell(row : Int, col : Int) = board[row][col]

    fun clear() {
        board.forEach { row ->
            row.forEach {
                it.value = 0
                it.permanent = false
                for(i in 0..8) {
                    it.notes[i] = false
                }
            }
        }
    }

    fun getCellList() : List<Cell> {
        return List<Cell>(81) {index ->
            board[index / 9][index % 9]
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