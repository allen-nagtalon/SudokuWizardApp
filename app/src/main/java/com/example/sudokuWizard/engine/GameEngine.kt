package com.example.sudokuWizard.engine

import android.util.Log
import androidx.lifecycle.MutableLiveData

class GameEngine(val rows : Int,
                 val cols : Int,
                 val rowSubSize : Int,
                 val colSubSize : Int,
                 private val boardLayout : String) {

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<Array<Array<Cell>>>()
    var boardEditEnabled = false
    var pencilEnabled = false

    private var selectedRow = -1
    private var selectedCol = -1

    private var board : Board

    init {
        // Split board layout string into individual cells
        val cellValues = boardLayout.split(",").toTypedArray()

        val cells = List(rows*cols) {i ->
            when(cellValues[i].toInt()) {
                0 -> Cell(i / cols, i % cols, 0)
                else -> Cell(i / cols, i % cols, cellValues[i].toInt(), true)
            }
        }

        board = Board(cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.board)
        boardEditEnabled = false
    }

    fun changeBoard(boardLayout: String) {
        val cellValues = boardLayout.split(",").toTypedArray()

        val cells = List(rows*cols) {i ->
            when(cellValues[i].toInt()) {
                0 -> Cell(i / cols, i % cols, 0)
                else -> Cell(i / cols, i % cols, cellValues[i].toInt(), true)
            }
        }

        board = Board(cells)
        cellsLiveData.postValue(board.board)
    }

    fun handleInput(number : Int) {
        // No cell selected
        if(selectedRow == -1 || selectedCol == -1) return

        // Board Edit is not enabled and selected cell is permanent
        if(board.getCell(selectedRow, selectedCol).permanent && !boardEditEnabled) return

        // Add note or mark cell
        if(pencilEnabled) {
            var cell = board.getCell(selectedRow, selectedCol)
            if(number == 0) {
                for(i in cell.notes.indices) cell.notes[i] = false
            } else {
                cell.notes[number-1] = !cell.notes[number-1]
            }
        } else {
            board.getCell(selectedRow, selectedCol).value = number
        }

        if(boardEditEnabled) {
            when(number) {
                0 -> board.getCell(selectedRow, selectedCol).permanent = false
                else -> board.getCell(selectedRow, selectedCol).permanent = true
            }
        }
        cellsLiveData.postValue(board.board)
    }

    fun solve() : Boolean{
        val solver = BacktrackingSolver(board)
        val solved = solver.solve()
        cellsLiveData.postValue(board.board)
        return solved
    }

    fun clear() {
        board.clear()
        cellsLiveData.postValue(board.board)
    }

    fun enableBoardEdit() {
        boardEditEnabled = !boardEditEnabled
        pencilEnabled = false
    }

    fun enablePenEdit() {
        if(pencilEnabled) pencilEnabled = false
    }

    fun enablePencilEdit() {
        // Return if board edit mode is on
        if(boardEditEnabled) return

        // Switch between pen and pencil mode
        if(!pencilEnabled) pencilEnabled = true
    }

    fun handleDpadInput(direction : Int) {
        var newRow = selectedRow
        var newCol = selectedCol

        if(newRow == -1 || newCol == -1) {
            val cur = getFirstNonPermanentCell()
            newRow = cur.row
            newCol = cur.col
        }
        else {
            var cur : Cell
            when(direction) {
                UP_DIRECTION -> {
                    do {
                        if(newRow != 0) newRow--
                        else newRow = 8
                    } while(board.board[newRow][newCol].permanent)
                }
                RIGHT_DIRECTION -> {
                    do {
                        if(newCol != 8) newCol++
                        else newCol = 0
                    } while(board.board[newRow][newCol].permanent)
                }
                DOWN_DIRECTION -> {
                    do {
                        if(newRow != 8) newRow++
                        else newRow = 0
                    } while(board.board[newRow][newCol].permanent)
                }
                LEFT_DIRECTION -> {
                    do {
                        if(newCol != 0) newCol--
                        else newCol = 8
                    } while(board.board[newRow][newCol].permanent)
                }
            }
        }

        updateSelectedCell(newRow, newCol)
    }

    fun updateSelectedCell(row : Int, col : Int) {
        if(!board.getCell(row, col).permanent || boardEditEnabled) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(row, col))

            // TEST CODE
            board.getCell(row, col).printCellInfo()
        }
    }

    private fun getFirstNonPermanentCell() : Cell {
        board.board.forEach { row ->
            row.forEach {
                if(!it.permanent) return it
            }
        }
        return board[0][0]
    }


    companion object {
        val UP_DIRECTION = 1
        val RIGHT_DIRECTION = 2
        val DOWN_DIRECTION = 3
        val LEFT_DIRECTION = 4
    }

    constructor() : this(9, 9, 3, 3,
    "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0")
}