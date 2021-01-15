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
    private val emptyCellList = createPotentialCellValues()


    // Uses the given board to create the priority queue of which cells are empty
    private fun createPotentialCellValues() : PriorityQueue<PotValuesList> {
        val compareBySize : Comparator<PotValuesList> = compareBy { it.values.size }
        val emptyCells = PriorityQueue<PotValuesList>(compareBySize)

        // Iterate through board to add cells and their values

        return emptyCells
    }

    override fun solve() : Boolean {
        return false
    }
}

// Class to store empty cell information: row, col, and the potential values
class PotValuesList(val row : Int, val col : Int, val values : List<Int>) {

}