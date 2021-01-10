package com.example.sudokuWizard

class SudokuSolver(private val boardLayout : String) {
    // Split board layout string into individual cells
    private val cells = boardLayout.split(",").toTypedArray()

    // Initialize board as 2D array of Int
    private var board = Array(9) { i -> IntArray(9) { j -> cells[(i*9)+j].toInt()} }

    fun solve() {

    }

    // Checks the validity of a given cell in the board against the constraints of Sudoku
    fun isValid(row : Int, col : Int) : Boolean {
        return rowCheck(row) && colCheck(col) && boxCheck(row, col)
    }

    // Checks the validity of a given row in the board
    private fun rowCheck(row : Int) : Boolean {
        val rowList = ArrayList<Int>()
        for(col in board[row]) {
            if(col > 0) rowList.add(col)
        }
        return rowList.distinct().size == rowList.size
    }

    // Checks the validity of a given column in the board
    private fun colCheck(col : Int) : Boolean {
        val colList = ArrayList<Int>()
        for(row in board) {
            if(row[col] > 0) colList.add(row[col])
        }
        return colList.distinct().size == colList.size
    }

    // Checks the validity of a given 3x3 subsection of the board
    private fun boxCheck(row : Int, col : Int) : Boolean {
        val boxRow = (row / 3) * 3
        val boxCol = (col / 3) * 3
        val boxList = ArrayList<Int>()

        for(i in boxRow..(boxRow+2)) {
            for(j in boxCol..(boxCol+2)) {
                if(board[i][j] > 0) boxList.add(board[i][j])
            }
        }

        return boxList.distinct().size == boxList.size
    }

    // Prints text-based UI of board to the console
    fun printBoard() {
        for(row in board) {
            for(col in row) {
                if (col > 0) print("$col ") else print("- ")
            }
            println()
        }
    }
}

fun main(args : Array<String>) {
    val s = SudokuSolver(
            "8,4,7,5,0,9,6,0,2," +
            "0,0,0,0,0,0,6,0,0," +
            "0,0,0,0,8,0,0,0,9," +
            "0,7,0,2,0,0,0,0,3," +
            "0,0,9,0,7,0,5,0,0," +
            "1,0,0,0,0,5,0,7,0," +
            "2,0,0,0,5,0,0,0,0," +
            "0,0,8,0,0,0,0,0,0," +
            "6,3,0,3,0,4,8,1,7")
    s.printBoard()
}