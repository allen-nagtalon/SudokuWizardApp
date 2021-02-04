package com.example.sudokuWizard.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sudokuWizard.R

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun startSudokuGame(view : View) {
        val intent = Intent(this, SudokuGameActivity::class.java)
        startActivity(intent)
    }

    fun startScanBoard(view : View) {
        val intent = Intent(this, ScanBoardActivity::class.java)
        startActivity(intent)
    }
}