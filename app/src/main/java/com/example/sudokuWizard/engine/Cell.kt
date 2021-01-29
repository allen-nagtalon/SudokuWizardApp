package com.example.sudokuWizard.engine

class Cell (
        val row : Int,
        val col : Int,
        var value : Int,
        var permanent : Boolean = false
)