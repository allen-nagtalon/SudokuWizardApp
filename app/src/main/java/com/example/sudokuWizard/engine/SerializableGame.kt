package com.example.sudokuWizard.engine

import java.io.Serializable

class SerializableGame(val cells : List<Cell>, val seconds : Int) : Serializable