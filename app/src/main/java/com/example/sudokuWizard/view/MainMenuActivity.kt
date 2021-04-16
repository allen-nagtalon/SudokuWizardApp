package com.example.sudokuWizard.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sudokuWizard.R
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    /** OVERRIDE FUNCTIONS ****/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        new_board_button.setOnClickListener {
            startSudokuGame()
        }

        scan_board_button.setOnClickListener {
            startScanBoard()
        }

        actionBar?.hide()
    }

    /** PRIVATE FUNCTIONS ****/

    private fun startSudokuGame() {
        val intent = Intent(this, SudokuGameActivity::class.java)
        startActivity(intent)
    }

    private fun startScanBoard() {
        val intent = Intent(this, ScanBoardActivity::class.java)
        startActivity(intent)
    }
}