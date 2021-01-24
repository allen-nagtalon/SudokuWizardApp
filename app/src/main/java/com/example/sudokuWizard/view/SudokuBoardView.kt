package com.example.sudokuWizard.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.sudokuWizard.engine.SudokuBoard

class SudokuBoardView(context : Context,
                      attributeSet : AttributeSet) : View(context, attributeSet) {

    private var board = SudokuBoard()
    private var cellPixelSize = 0F

    private var selectedRow = 1
    private var selectedCol = 4

    private var listener : OnTouchListener? = null

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
        color = Color.parseColor("#6ead3a")
    }

    private val relatedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efedef")
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

        fillCells(canvas)
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

    private fun fillCells(canvas : Canvas) {
        if (selectedRow == -1 || selectedCol == -1) return

        for(r in 0 until board.ROWS) {
            for(c in 0 until board.COLS) {
                if(r == selectedRow && c == selectedCol) {
                    fillCellColor(canvas, r, c, selectedCellPaint)
                } else if (r == selectedRow || c == selectedCol) {
                    fillCellColor(canvas, r, c, relatedCellPaint)
                } else if (r / board.ROW_SUB_LENGTH == selectedRow / board.ROW_SUB_LENGTH &&
                        c / board.COL_SUB_LENGTH == selectedCol / board.COL_SUB_LENGTH) {
                    fillCellColor(canvas, r, c, relatedCellPaint)
                }
            }
        }
    }

    private fun fillCellColor(canvas: Canvas, r : Int, c : Int, paint : Paint) {
        canvas.drawRect(
                c * cellPixelSize,
                r * cellPixelSize,
                (c+1) * cellPixelSize,
                (r+1) * cellPixelSize,
                paint)
    }

    fun setBoard(board : SudokuBoard) {
        this.board = board
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x : Float, y : Float) {
        val possibleSelectedRow = (y / cellPixelSize).toInt()
        val possibleSelectedCol = (x / cellPixelSize).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    fun updateSelectedCellUI(row : Int, col : Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun registerListener(listener : OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row : Int, col : Int)
    }
}