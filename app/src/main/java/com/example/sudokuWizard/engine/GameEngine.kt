package com.example.sudokuWizard.engine

import androidx.lifecycle.MutableLiveData

class GameEngine(val rows : Int,
                 val cols : Int,
                 val rowSubSize : Int,
                 val colSubSize : Int,
                 val boardLayout : String) {

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<Array<Array<Cell>>>()
    var boardEditEnabled = false

    private var selectedRow = -1
    private var selectedCol = -1

    private val board : Board

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

    fun handleInput(number : Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        if(board.getCell(selectedRow, selectedCol).permanent && !boardEditEnabled) return


        board.getCell(selectedRow, selectedCol).value = number
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
    }

    fun updateSelectedCell(row : Int, col : Int) {
        if(!board.getCell(row, col).permanent || boardEditEnabled) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(row, col))
        }
    }

    fun updateBoardSettings(rows : Int, cols : Int, rowSubSize : Int, colSubSize : Int) {

    }

    constructor() : this(9, 9, 3, 3,
        "8,4,7,5,0,9,0,0,2," +
            "0,0,0,0,0,0,6,0,0," +
            "0,0,0,0,8,0,0,0,9," +
            "0,7,0,2,0,0,0,0,3," +
            "0,0,9,0,7,0,5,0,0," +
            "1,0,0,0,0,5,0,7,0," +
            "2,0,0,0,5,0,0,0,0," +
            "0,0,8,0,0,0,0,0,0," +
            "6,0,0,3,0,4,8,1,7")
}