package com.example.sudokuWizard.view

import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sudokuWizard.R
import com.example.sudokuWizard.viewmodel.SudokuBoardViewModel

class MainActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel : SudokuBoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board_view.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(SudokuBoardViewModel::class.java)
        viewModel.board
            .selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it)})
    }

    private fun updateSelectedCellUI(cell : Pair<Int, Int>?) = cell?.let {
        board_view.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouched(row : Int, col : Int) {
        viewModel.board.updateSelectedCell(row, col)
    }
}