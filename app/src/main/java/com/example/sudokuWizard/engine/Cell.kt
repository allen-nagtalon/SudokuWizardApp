package com.example.sudokuWizard.engine

import android.util.Log
import java.io.Serializable

class Cell (
        val row : Int,
        val col : Int,
        var value : Int,
        var permanent : Boolean = false,
        var notes : Array<Boolean> = Array(9) { false }
) : Serializable {
    fun printCellInfo() {
        var info = "Notes: [ "
        for(i in 0 until 9) {
            if(notes[i]) info += "${i+1} "
        }
        info += "]"

        Log.d("CELL", info)
    }
}