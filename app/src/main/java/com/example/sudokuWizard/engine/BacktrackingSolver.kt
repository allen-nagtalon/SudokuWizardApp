package com.example.sudokuWizard.engine

/** BacktrackingSolver is a sudoku solver that uses a backtracking algorithm to
 *  test all possible values in all empty cells of a given puzzle.*/
class BacktrackingSolver(private val board : Board) : SudokuSolver {

    /** Private Functions *********/

    // Checks the validity of a given cell in the board against the constraints of Sudoku
    private fun isValid(row : Int, col : Int) : Boolean {
        return rowCheck(row) && colCheck(col) && boxCheck(row, col)
    }

    // Checks the validity of a given row in the board
    private fun rowCheck(row : Int) : Boolean {
        val rowList = ArrayList<Int>()
        for(col in board[row]) {
            if(col.value != board.EMPTY_CELL) rowList.add(col.value)
        }
        return rowList.distinct().size == rowList.size
    }

    // Checks the validity of a given column in the board
    private fun colCheck(col : Int) : Boolean {
        val colList = ArrayList<Int>()
        for(row in board.board) {
            if(row[col].value != board.EMPTY_CELL) colList.add(row[col].value)
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
                if(board[i][j].value != board.EMPTY_CELL) boxList.add(board[i][j].value)
            }
        }

        return boxList.distinct().size == boxList.size
    }

    /** Public Functions *********/

    // Recursive function for backtracking algorithm to traverse all empty cells
    override fun solve() : Boolean {
        for(row in board.START_INDEX until board.ROWS) {
            for(col in board.START_INDEX until board.COLS) {
                if(board[row][col].value == board.EMPTY_CELL) {
                    for(value in board.MIN_VALUE..board.MAX_VALUE) {
                        board[row][col].value = value
                        if(isValid(row, col) && solve()) {
                            return true
                        }
                        board[row][col].value = board.EMPTY_CELL
                    }
                    return false
                }
            }
        }
        return true
    }
}
