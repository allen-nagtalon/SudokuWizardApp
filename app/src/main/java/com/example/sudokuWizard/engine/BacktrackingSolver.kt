package com.example.sudokuWizard.engine

/** BacktrackingSolver is a sudoku solver that uses a backtracking algorithm to
 *  test all possible values in all empty cells of a given puzzle.*/
class BacktrackingSolver(private val board : SudokuBoard) :
    SudokuSolver {

    /** Private Functions *********/

    // Checks the validity of a given cell in the board against the constraints of Sudoku
    private fun isValid(row : Int, col : Int) : Boolean {
        return rowCheck(row) && colCheck(col) && boxCheck(row, col)
    }

    // Checks the validity of a given row in the board
    private fun rowCheck(row : Int) : Boolean {
        val rowList = ArrayList<Int>()
        for(col in board[row]) {
            if(col != board.EMPTY_CELL) rowList.add(col)
        }
        return rowList.distinct().size == rowList.size
    }

    // Checks the validity of a given column in the board
    private fun colCheck(col : Int) : Boolean {
        val colList = ArrayList<Int>()
        for(row in board.board) {
            if(row[col] != board.EMPTY_CELL) colList.add(row[col])
        }
        return colList.distinct().size == colList.size
    }

    // Checks the validity of a given 3x3 subsection of the board
    private fun boxCheck(row : Int, col : Int) : Boolean {
        val boxRow = (row / board.ROW_SUB_LENGTH) * board.ROW_SUB_LENGTH
        val boxCol = (col / board.COL_SUB_LENGTH) * board.COL_SUB_LENGTH
        val boxList = ArrayList<Int>()

        for(i in boxRow until (boxRow+board.ROW_SUB_LENGTH)) {
            for(j in boxCol until (boxCol+board.COL_SUB_LENGTH)) {
                if(board[i][j] != board.EMPTY_CELL) boxList.add(board[i][j])
            }
        }

        return boxList.distinct().size == boxList.size
    }

    /** Public Functions *********/

    // Recursive function for backtracking algorithm to traverse all empty cells
    override fun solve() : Boolean {
        for(row in board.START_INDEX until board.ROWS) {
            for(col in board.START_INDEX until board.COLS) {
                if(board[row][col] == board.EMPTY_CELL) {
                    for(value in board.MIN_VALUE..board.MAX_VALUE) {
                        board[row][col] = value
                        if(isValid(row, col) && solve()) {
                            return true
                        }
                        board[row][col] = board.EMPTY_CELL
                    }
                    return false
                }
            }
        }
        return true
    }
}

fun main(args : Array<String>) {
    // Test for 4x4 Board
    val testBoard1 = SudokuBoard(
        "1,0,0,2," +
                "0,2,3,0," +
                "0,0,0,3," +
                "0,1,0,0",
        4, 4, 4, 2, 2
    )

    // Test for 9x9 Board
    val testBoard2 = SudokuBoard(
        "8,4,7,5,0,9,0,0,2," +
                "0,0,0,0,0,0,6,0,0," +
                "0,0,0,0,8,0,0,0,9," +
                "0,7,0,2,0,0,0,0,3," +
                "0,0,9,0,7,0,5,0,0," +
                "1,0,0,0,0,5,0,7,0," +
                "2,0,0,0,5,0,0,0,0," +
                "0,0,8,0,0,0,0,0,0," +
                "6,0,0,3,0,4,8,1,7"
    )

    // Test for 16,16 Board
    val testBoard3 = SudokuBoard(
        "0,0,5,14,0,12,9,16,0,3,0,1,4,0,8,13," +
                "0,0,11,0,0,0,0,6,0,15,0,0,0,16,1,0," +
                "0,15,9,2,3,13,1,0,0,0,0,0,0,0,0,11," +
                "0,6,4,8,0,0,0,0,0,13,2,9,0,0,0,0," +
                "0,16,0,0,10,2,0,0,0,9,0,14,13,4,0,0," +
                "15,0,0,0,0,3,13,0,11,0,12,4,0,0,6,16," +
                "4,9,7,11,0,0,15,0,0,16,0,0,0,1,0,8," +
                "0,0,6,5,0,11,0,0,10,1,0,8,14,0,12,15," +
                "12,8,0,13,0,15,0,14,4,0,16,0,11,0,0,7," +
                "0,7,0,0,0,0,10,0,0,12,0,0,16,0,13,2," +
                "6,3,0,0,0,0,0,11,0,2,0,0,0,0,5,10," +
                "0,0,0,0,6,0,0,13,0,10,0,0,12,0,15,4," +
                "0,0,0,0,0,1,12,0,2,11,10,0,0,0,14,6," +
                "0,0,0,0,0,6,0,0,0,0,13,7,15,0,0,12," +
                "9,11,0,0,13,5,4,7,12,14,1,0,0,0,16,3," +
                "0,0,0,15,0,0,11,10,3,4,0,0,0,7,0,0",
        16, 16, 16, 4, 4
    )

    val s = BacktrackingSolver(testBoard3)

    println("========================")

    testBoard3.printBoard()

    println("========================")

    if(s.solve()) { println("Solved.") } else { println("Not solved.") }

    testBoard3.printBoard()

    println("========================")
}