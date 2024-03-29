package com.example.sudokuWizard.view.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.sudokuWizard.engine.Cell
import kotlin.math.min

class BoardView(context : Context,
                attributeSet : AttributeSet) : View(context, attributeSet) {

    private var cellPixelSize = 0F

    private var rows = 9
    private var cols = 9
    private var rowSubSize = 3
    private var colSubSize = 3

    private var selectedRow = 1
    private var selectedCol = 4

    private var listener : OnTouchListener? = null

    private var cells : Array<Array<Cell>>? = null

    private val borderLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 7F
    }

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

    private val permanentCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#acacac")
    }

    private val relatedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efedef")
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 24F
    }

    private val permanentCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 32F
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Set height and width of the SudokuBoardView to be the min of the two dimensions
        val size = min(widthMeasureSpec - 64 - (widthMeasureSpec % 9),
                heightMeasureSpec- 64 - (heightMeasureSpec % 9))
        setMeasuredDimension(size, size)

        /** Adjust this later to account for varying sized boards. **/
    }

    override fun onDraw(canvas: Canvas) {
        /** Adjust this later to account for varying sized boards. **/
        cellPixelSize = (width / rows).toFloat()

        fillCells(canvas)
        drawFrame(canvas)
        drawText(canvas)
    }

    private fun drawFrame(canvas : Canvas) {
        for(i in 0..9) {
            val lineThickness = when (i % rowSubSize) {
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
                    i * cellPixelSize,
                    lineThickness
            )
        }

        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)
    }

    private fun fillCells(canvas : Canvas) {
        cells?.forEach { row ->
            row.forEach {
                val r = it.row
                val c = it.col

                if(it.permanent) {
                    fillCellColor(canvas, r, c, permanentCellPaint)
                } else if(r == selectedRow && c == selectedCol) {
                    fillCellColor(canvas, r, c, selectedCellPaint)
                } else if (r == selectedRow || c == selectedCol) {
                    fillCellColor(canvas, r, c, relatedCellPaint)
                } else if (r / rowSubSize == selectedRow / rowSubSize &&
                    c / colSubSize == selectedCol / colSubSize) {
                    fillCellColor(canvas, r, c, relatedCellPaint)
                }
            }
        }
    }

    private fun drawText(canvas : Canvas) {
        cells?.forEach { row ->
            row.forEach {
                if(it.value != 0) {
                    val row = it.row
                    val col = it.col
                    val valString = it.value.toString()
                    val paintToUse = if(it.permanent) permanentCellTextPaint else textPaint
                    val textBounds = Rect()

                    paintToUse.getTextBounds(valString, 0, valString.length, textBounds)

                    val textWidth = paintToUse.measureText(valString)
                    val textHeight = textBounds.height()

                    canvas.drawText(valString,
                        (col * cellPixelSize) + cellPixelSize / 2 - textWidth / 2,
                        (row * cellPixelSize) + cellPixelSize / 2 - textHeight / 2,
                        paintToUse)
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

    fun updateBoardSetttings(rows : Int, cols : Int, rowSubSize : Int, colSubSize : Int) {
        this.rows = rows
        this.cols = cols
        this.rowSubSize = rowSubSize
        this.colSubSize = colSubSize
    }

    fun registerListener(listener : OnTouchListener) {
        this.listener = listener
    }

    fun updateCells(cells : Array<Array<Cell>>) {
        this.cells = cells
        invalidate()
    }

    interface OnTouchListener {
        fun onCellTouched(row : Int, col : Int)
    }
}