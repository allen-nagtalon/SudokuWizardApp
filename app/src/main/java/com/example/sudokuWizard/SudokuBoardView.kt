package com.example.sudokuWizard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SudokuBoardView(context : Context,
                      attributeSet : AttributeSet) : View(context, attributeSet) {

    private var board = SudokuBoard()
    private var cellPixelSize = 0F

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 5F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("6ead3a")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("efedef")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Set height and width of the SudokuBoardView to be the min of the two dimensions
        val size = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size, size)

        /** Adjust this later to account for varying sized boards. **/
    }

    override fun onDraw(canvas: Canvas) {
        /** Adjust this later to account for varying sized boards. **/
        cellPixelSize = (width / board.ROWS).toFloat()

        drawFrame(canvas)
    }

    private fun drawFrame(canvas : Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for(i in 1 until board.ROWS) {
            val lineThickness = when (i % board.ROW_SUB_LENGTH) {
                0 -> thickLinePaint
                else -> thinLinePaint
            }

            canvas.drawLine(
                    i * cellPixelSize,
                    0F,
                    i * cellPixelSize,
                    height.toFloat(),
                    lineThickness
            )

            canvas.drawLine(
                    0F,
                    i * cellPixelSize,
                    width.toFloat(),
                    i*cellPixelSize,
                    lineThickness
            )
        }
    }

    fun setBoard(board : SudokuBoard) {
        this.board = board
    }
}