package com.example.sudokuWizard.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import kotlinx.android.synthetic.main.activity_sudoku_game.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sudokuWizard.R
import com.example.sudokuWizard.engine.Cell
import com.example.sudokuWizard.engine.GameEngine
import com.example.sudokuWizard.engine.SerializableGame
import com.example.sudokuWizard.engine.SudokuSolver
import com.example.sudokuWizard.view.customview.BoardViewRemake
import com.example.sudokuWizard.viewmodel.BoardViewModel
import java.io.ObjectOutputStream

class SudokuGameActivity() : AppCompatActivity(), BoardViewRemake.OnTouchListener {
    private lateinit var viewModel : BoardViewModel

    private var seconds : Int = 0
    private var minutes : Int = 0
    private lateinit var timerHandler : Handler
    private lateinit var timerRunnable : Runnable

    /** OVERRIDE FUNCTIONS ******/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku_game)

        board_view.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(BoardViewModel::class.java)

        val boardEdit = intent.getBooleanExtra("newBoard", true)

        intent.getStringExtra("boardLayout")?.let {
            viewModel.sudokuGame.changeBoard(it)
        }

        if(boardEdit) {
            pen_button.visibility = View.GONE
            pencil_button.visibility = View.GONE
            viewModel.sudokuGame.toggleBoardEdit()
        } else {
            check_button.visibility = View.GONE
        }

        viewModel.sudokuGame
            .selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it)})
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })

        applyButtonListeners()

        timerHandler = Handler()
        timerRunnable = Runnable {
            seconds++
            minutes = seconds / 60

            timer.text = String.format("%d:%02d", minutes, seconds % 60)

            timerHandler.postDelayed(timerRunnable, 1000)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onPause() {
        super.onPause()
        timerHandler.removeCallbacks(timerRunnable)
    }

    override fun onResume() {
        super.onResume()
        if(!viewModel.sudokuGame.boardEditEnabled)
            timerHandler.postDelayed(timerRunnable, 0)
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
                    timerHandler.removeCallbacks(timerRunnable)
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

    /** PRIVATE FUNCTIONS ******/

    private fun applyButtonListeners() {
        val buttons = listOf(one_button, two_button, three_button,
            four_button, five_button, six_button, seven_button,
            eight_button, nine_button)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
                when(viewModel.sudokuGame.checkComplete()) {
                    SudokuSolver.SOLVED -> {
                        Toast.makeText(this, String.format("The board was solved in %d:%02d!", minutes, seconds % 60), Toast.LENGTH_SHORT).show()
                        timerHandler.removeCallbacks(timerRunnable)
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

        check_button.setOnClickListener {
            confirmNewBoardCompletion()
        }
    }

    private fun updateCells(cells : Array<Array<Cell>>?) = cells?.let {
        board_view.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell : Pair<Int, Int>?) = cell?.let {
        board_view.updateSelectedCellUI(cell.first, cell.second)
    }

    private fun confirmNewBoardCompletion() {
        val newFragment = ConfirmationDialogFragment(this)
        newFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
        newFragment.show(supportFragmentManager, "confirm-check")
    }

    /** PUBLIC FUNCTIONS ****/

    fun startGame() {
        check_button.visibility = View.GONE
        pen_button.visibility = View.VISIBLE
        pencil_button.visibility = View.VISIBLE

        viewModel.sudokuGame.toggleBoardEdit()
        timerHandler.postDelayed(timerRunnable, 0)
    }

    fun save() {
        val testFileName = "Test.dat"
        applicationContext.openFileOutput(testFileName, Context.MODE_PRIVATE).use {
            val out = ObjectOutputStream(it)
            out.writeObject(SerializableGame(viewModel.sudokuGame.getCellList(), seconds))

            out.close()
            it.close()
        }
    }

    fun load() {

    }

    class ConfirmationDialogFragment(private val game : SudokuGameActivity) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage(R.string.manual_board_confirmation)
                    .setPositiveButton(R.string.continue_button) { _, _ ->
                        game.startGame()
                    }
                    .setNegativeButton(R.string.cancel_button) { _, _ ->
                        dismiss()
                    }

                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}