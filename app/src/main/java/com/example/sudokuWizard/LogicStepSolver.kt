package com.example.sudokuWizard

import java.util.*

/**
 * Theory:
 * 1. Make an object to store a cell (row, col) pair and list of possible values
 * 2. Iterate through the list of objects to verify validity of each value
 * 3. If an object only has one possible solution, fill that cell and remove the object from the list
 *    - If a full list iteration occurs without being able to mark a new cell, a guess will have to be
 *      made
 *
 *  Issues:
 *  A. How to decide on a cell to guess on
 *  B. How to backtrack to that cell if the guess proves incorrect (IE. leads to one cell having no
 *     possible solutions
 *  C. How to handle the potential of multiple guesses
 *
 *  Things Needed:
 *  - data class to store possible cell values
 *  - priority queue for possible cell value objects
 *  - row/col/box validity checks
 *  - some sort of data structure for storing logic steps
 **/


class LogicStepSolver(private val board : SudokuBoard) : SudokuSolver {

    // Priority Queue of all empty cells ordered by the number of possible values
    private val emptyCellList = createEmptyCellList()

    /** Private Functions *********/

    // Checks if a given value still needs to be placed in a given row
    private fun rowCheck(row : Int, value : Int) : Boolean {
        for(col in board[row]) {
            if(value == col) return false
        }
        return true
    }

    // Checks if a given value still needs to be placed in a given col
    private fun colCheck(col : Int, value : Int) : Boolean {
        for(row in board.board) {
            if (row[col] == value) return false
        }
        return true
    }

    // Checks if a given values still needs to be placed in a given subsection
    private fun boxCheck(row : Int, col : Int, value : Int) : Boolean {
        val boxRow = (row / board.ROW_SUB_LENGTH) * board.ROW_SUB_LENGTH
        val boxCol = (col / board.COL_SUB_LENGTH) * board.COL_SUB_LENGTH

        for(i in boxRow until (boxRow+board.ROW_SUB_LENGTH)) {
            for(j in boxCol until (boxCol+board.COL_SUB_LENGTH)) {
                if(board[i][j] == value) return false
            }
        }
        return true
    }

    // Builds a priority queue of all the empty cells; ordered by possible value count
    private fun createEmptyCellList() : PriorityQueue<PotValuesList> {
        val compareBySize : Comparator<PotValuesList> = compareBy { it.values.size }
        val emptyCells = PriorityQueue<PotValuesList>(compareBySize)

        for(i in board.START_INDEX until board.ROWS) {
            for(j in board.START_INDEX until board.COLS) {
                if (board[i][j] == board.EMPTY_CELL) {
                    emptyCells.add(PotValuesList(i, j, board.MAX_VALUE))
                }
            }
        }

        return emptyCells
    }

    /** Public Functions *********/

    override fun solve() : Boolean {
        /**
         * Cases:
         * 1. EmptyCellsList is empty: Board has been filled.
         * 2. EmptyCellsList is not empty:
         *   A. A cell has no potential values
         *     a. A guess made before was wrong, or
         *     b. The board is invalid
         *   B. A cell has 1 potential value
         *   C. All cells have 2+ pot. values
         */

        return false
    }

    fun test() {
        print("There are ${emptyCellList.size} empty cells.")
    }
}

// Class to store empty cell information: row, col, and the potential values
class PotValuesList(private val row : Int, private val col : Int, private val max : Int) {

    // ArrayList containing the potential values that can fill a given cell (row, col)
    val values = ArrayList<Int>()

    init {
        for(i in 0 until max) values.add(i+1)
    }

}

fun main() {
    // Test for 9x9 Board
    val testBoard2 = SudokuBoard("8,4,7,5,0,9,0,0,2," +
            "0,0,0,0,0,0,6,0,0," +
            "0,0,0,0,8,0,0,0,9," +
            "0,7,0,2,0,0,0,0,3," +
            "0,0,9,0,7,0,5,0,0," +
            "1,0,0,0,0,5,0,7,0," +
            "2,0,0,0,5,0,0,0,0," +
            "0,0,8,0,0,0,0,0,0," +
            "6,0,0,3,0,4,8,1,7")

    val s = LogicStepSolver(testBoard2)

    testBoard2.printBoard()
    s.test()
}