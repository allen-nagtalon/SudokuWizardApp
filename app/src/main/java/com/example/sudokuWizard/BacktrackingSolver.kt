package com.example.sudokuWizard

/** BacktrackingSolver is a sudoku solver that uses a backtracking algorithm to
 *  test all possible values in all empty cells of a given puzzle.*/
class BacktrackingSolver(private val boardLayout : String) : SudokuSolver {
    private val EMPTY_CELL = 0
    private val MIN_VALUE = 1
    private val MAX_VALUE = 9
    private val START_INDEX = 0
    private val END_INDEX = 8

    // Split board layout string into individual cells
    private val cells = boardLayout.split(",").toTypedArray()

    // Initialize board as 2D array of Int
    private var board = Array(9) { i -> IntArray(9) { j -> cells[(i*9)+j].toInt()} }

    /** Private Functions *********/

    // Checks the validity of a given cell in the board against the constraints of Sudoku
    private fun isValid(row : Int, col : Int) : Boolean {
        return rowCheck(row) && colCheck(col) && boxCheck(row, col)
    }

    // Checks the validity of a given row in the board
    private fun rowCheck(row : Int) : Boolean {
        val rowList = ArrayList<Int>()
        for(col in board[row]) {
            if(col != EMPTY_CELL) rowList.add(col)
        }
        return rowList.distinct().size == rowList.size
    }

    // Checks the validity of a given column in the board
    private fun colCheck(col : Int) : Boolean {
        val colList = ArrayList<Int>()
        for(row in board) {
            if(row[col] != EMPTY_CELL) colList.add(row[col])
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
                if(board[i][j] != EMPTY_CELL) boxList.add(board[i][j])
            }
        }

        return boxList.distinct().size == boxList.size
    }

    /** Public Functions *********/

    // Recursive function for backtracking algorithm to traverse all empty cells
    override fun solve() : Boolean {
        for(row in START_INDEX..END_INDEX) {
            for(col in START_INDEX..END_INDEX) {
                if(board[row][col] == EMPTY_CELL) {
                    for(value in MIN_VALUE..MAX_VALUE) {
                        board[row][col] = value
                        if(isValid(row, col) && solve()) {
                            return true
                        }
                        board[row][col] = EMPTY_CELL
                    }
                    return false
                }
            }
        }
        return true
    }

    // Prints text-based UI of board to the console
    override fun printBoard() {
        for(row in board) {
            for(col in row) {
                if (col != EMPTY_CELL) print("$col ") else print("- ")
            }
            println()
        }
    }
}

fun main(args : Array<String>) {
    val s = BacktrackingSolver(
        "8,4,7,5,0,9,0,0,2," +
                "0,0,0,0,0,0,6,0,0," +
                "0,0,0,0,8,0,0,0,9," +
                "0,7,0,2,0,0,0,0,3," +
                "0,0,9,0,7,0,5,0,0," +
                "1,0,0,0,0,5,0,7,0," +
                "2,0,0,0,5,0,0,0,0," +
                "0,0,8,0,0,0,0,0,0," +
                "6,0,0,3,0,4,8,1,7")

    println("========================")

    s.printBoard()

    println("========================")

    if(s.solve()) { println("Solved.") } else { println("Not solved.") }
    s.printBoard()

    println("========================")
}