package com.example.sudokuWizard.view

import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sudokuWizard.R
import com.example.sudokuWizard.engine.Cell
import com.example.sudokuWizard.viewmodel.BoardViewModel

class SudokuGameActivity : AppCompatActivity(), BoardView.OnTouchListener {

    private lateinit var viewModel : BoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board_view.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(BoardViewModel::class.java)
        viewModel.sudokuGame
            .selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it)})
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })
    }

    private fun updateCells(cells : Array<Array<Cell>>?) = cells?.let {
        board_view.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell : Pair<Int, Int>?) = cell?.let {
        board_view.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouched(row : Int, col : Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}