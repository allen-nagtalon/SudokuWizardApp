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
 *
 **/


class LogicStepSolver(private val board : SudokuBoard) : SudokuSolver {
    // Priority Queue of all empty cells ordered by the number of possible values
    private val emptyCellList = {
        val compareBySize : Comparator<PotValuesList> = compareBy { it.values.size }
        val emptyCells = PriorityQueue<PotValuesList>(compareBySize)

        for(i in board.START_INDEX until board.ROWS) {
            for(j in board.START_INDEX until board.COLS) {
                if (board[i][j] == board.EMPTY_CELL) {
                    emptyCells.add(PotValuesList(i, j, board.MAX_VALUE))
                }
            }
        }

        emptyCells
    }

    private fun rowCheck(row : Int, value : Int) : Boolean {
        for(col in board[row]) {
            if(value == col) return false
        }
        return true
    }

    private fun colCheck(col : Int, value : Int) : Boolean {
        for(row in board.board) {
            if (row[col] == value) return false
        }
        return true
    }

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

    override fun solve() : Boolean {


        return false
    }
}

// Class to store empty cell information: row, col, and the potential values
class PotValuesList(private val row : Int, private val col : Int, private val max : Int) {
    val values = ArrayList<Int>(max)
    init {
        for(i in 0 until max) values[i] = i+1
    }
}