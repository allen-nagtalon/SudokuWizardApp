package com.example.sudokuWizard.view.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.sudokuWizard.engine.Cell
import kotlin.math.min

class BoardViewRemake(context: Context, attributeSet : AttributeSet) : View(context, attributeSet) {

    private var cellPixelSize = 0F

    private var selectedRow = -1
    private var selectedCol = -1

    private var listener : OnTouchListener? = null

    private var cells : Array<Array<Cell>>? = null

    /** PAINTS ***********************************************/
    /**** Line Paints ****************************************/

    private val borderLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 8F
    }

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    /**** Text Paints ****************************************/

    private val notePaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 12F
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 48F
    }

    private val permanentCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 48F
        typeface = Typeface.DEFAULT_BOLD
    }

    /**** Cell Color Paints ***********************************/

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#a6b3d0")
    }

    private val relatedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#e5e5e5")
    }

    override fun onMeasure(widthMeasureSpec : Int, heightMeasureSpec : Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val minDim = min(widthMeasureSpec - 64 - ((widthMeasureSpec - 64) % 9),
                heightMeasureSpec - 64 - ((heightMeasureSpec - 64) % 9))

        val size = minDim + 28

        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        cellPixelSize = ((width - 28)/ 9).toFloat()

        drawFrame(canvas)
        drawText(canvas)
    }

    private fun fillCells(canvas: Canvas) {
        var xBuffer = 0F
        var yBuffer = 0F
        cells?.forEach { row ->
            row.forEach {
                val r = it.row
                val c = it.col


            }

        }
    }

    private fun drawFrame(canvas: Canvas) {
        var buffer = 0F
        for(i in 0..9) {
            val cur = when (i % 3) {
                0 -> {
                    if(i == 0 || i == 9) 4F
                    else 2F
                }
                else -> 1F
            }

            val lineThickness = when (i % 3) {
                0 -> {
                    if (i == 0 || i == 9) borderLinePaint
                    else thickLinePaint
                }
                else -> thinLinePaint
            }

            buffer += cur

            canvas.drawLine(
                    i * cellPixelSize + buffer,
                    0F,
                    i * cellPixelSize + buffer,
                    height.toFloat(),
                    lineThickness
            )

            canvas.drawLine(
                    0F,
                    i * cellPixelSize + buffer,
                    width.toFloat(),
                    i * cellPixelSize + buffer,
                    lineThickness
            )

            buffer += cur
        }
    }

    private fun drawText(canvas: Canvas) {
        var xBuffer = 0F
        var yBuffer = 0F

        cells?.forEach { row ->
            val curRow = row[0].row
            val curX = when (curRow % 3) {
                0 -> {
                    if (curRow == 0 || curRow == 9) 4F
                    else 2F
                }
                else -> 1F
            }
            xBuffer += curX

            row.forEach {
                if (it.value != 0) {
                    val row = it.row
                    val col = it.col

                    val curY = when (col % 3) {
                        0 -> {
                            if (col == 0 || col == 9) 4F
                            else 2F
                        }
                        else -> 1F
                    }

                    val valString = it.value.toString()
                    val paintToUse = if (it.permanent) permanentCellTextPaint else textPaint
                    val textBounds = Rect()

                    yBuffer += curY

                    paintToUse.getTextBounds(valString, 0, valString.length, textBounds)

                    val textWidth = paintToUse.measureText(valString)
                    val textHeight = textBounds.height()

                    canvas.drawText(valString,
                            (col * cellPixelSize) + cellPixelSize / 2 - textWidth / 2 + xBuffer,
                            (row * cellPixelSize) + cellPixelSize / 2 + textHeight / 2 + yBuffer,
                            paintToUse)

                    yBuffer += curY
                }
                else {
                    for(i in it.notes.indices) {
                        if(it.notes[i]) {
                            val r = it.row
                            val c = it.col
                            val valString = (i+1).toString()
                            val textBounds = Rect()


                        }
                    }
                }
            }
            xBuffer += curX
            yBuffer = 0F
        }
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

    fun updateCells(cells : Array<Array<Cell>>) {
        this.cells = cells
        invalidate()
    }

    interface OnTouchListener {
        fun onCellTouched(row : Int, col : Int)
    }
}