package com.example.sudokuWizard.view

import kotlinx.android.synthetic.main.activity_sudoku_game.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sudokuWizard.R
import com.example.sudokuWizard.engine.Cell
import com.example.sudokuWizard.engine.GameEngine
import com.example.sudokuWizard.engine.SudokuSolver
import com.example.sudokuWizard.view.customview.BoardViewRemake
import com.example.sudokuWizard.viewmodel.BoardViewModel

class SudokuGameActivity() : AppCompatActivity(), BoardViewRemake.OnTouchListener {
    private lateinit var viewModel : BoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku_game)

        board_view.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(BoardViewModel::class.java)

        intent.getStringExtra("boardLayout")?.let {
            viewModel.sudokuGame.changeBoard(it)
        }

        viewModel.sudokuGame
            .selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it)})
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })

        val buttons = listOf(one_button, two_button, three_button,
            four_button, five_button, six_button, seven_button,
            eight_button, nine_button)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
                when(viewModel.sudokuGame.checkComplete()) {
                    SudokuSolver.SOLVED -> {
                        Toast.makeText(this, "The board is solved!", Toast.LENGTH_SHORT).show()
                    }
                    SudokuSolver.INCORRECT -> {
                        Toast.makeText(this, "A cell is incorrect.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        clear_button.setOnClickListener {
            viewModel.sudokuGame.handleInput(0)
        }

        pen_button.isChecked = true

        pen_button.setOnClickListener {
            viewModel.sudokuGame.enablePenEdit()
        }

        pencil_button.setOnClickListener {
            viewModel.sudokuGame.enablePencilEdit()
        }

        dpad_up_button.setOnClickListener {
            viewModel.sudokuGame.handleDpadInput(GameEngine.UP_DIRECTION)
        }

        dpad_right_button.setOnClickListener {
            viewModel.sudokuGame.handleDpadInput(GameEngine.RIGHT_DIRECTION)
        }

        dpad_down_button.setOnClickListener {
            viewModel.sudokuGame.handleDpadInput(GameEngine.DOWN_DIRECTION)
        }

        dpad_left_button.setOnClickListener {
            viewModel.sudokuGame.handleDpadInput(GameEngine.LEFT_DIRECTION)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun updateCells(cells : Array<Array<Cell>>?) = cells?.let {
        board_view.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell : Pair<Int, Int>?) = cell?.let {
        board_view.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_solve -> {
                if(!viewModel.sudokuGame.solve()) {
                    Toast.makeText(this, "Error: Board could not be solved.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "The board is solved!", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_clear -> {
                viewModel.sudokuGame.clear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCellTouched(row : Int, col : Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}