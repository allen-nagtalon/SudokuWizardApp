package com.example.sudokuWizard.view.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.mlkit.vision.text.Text
import kotlin.math.min

class BoardOverlayView(context : Context,
                       attributeSet: AttributeSet) : View(context, attributeSet) {

    private var cellPixelSize = 0

    private var xPadding = 0
    private var yPadding = 0

    private var rows = 9
    private var cols = 9
    private var rowSubSize = 3
    private var colSubSize = 3

    private var board = Array(rows) {
        _ -> Array(cols) {
        _ -> 0 }
    }

    private val permanentCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.CYAN
        textSize = 64F
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Set height and width of the SudokuBoardView to be the min of the two dimensions
        val size = min(widthMeasureSpec - 64 - (widthMeasureSpec % 9),
            heightMeasureSpec- 64 - (heightMeasureSpec % 9))
        xPadding = (widthMeasureSpec - size) / 2
        yPadding = (heightMeasureSpec - size) / 2
        setMeasuredDimension(size, size)

        /** Adjust this later to account for varying sized boards. **/
    }

    override fun onDraw(canvas : Canvas) {
        cellPixelSize = (width / rows)

        drawScans(canvas)
    }

    private fun drawScans(canvas: Canvas) {
        for(i in board.indices) {
            for(j in board[i].indices) {
                if(board[i][j] != 0) {
                    val valString = board[i][j].toString()
                    val textBounds = Rect()

                    permanentCellTextPaint.getTextBounds(valString, 0, valString.length, textBounds)

                    val textWidth = permanentCellTextPaint.measureText(valString)
                    val textHeight = textBounds.height()

                    canvas.drawText(valString,
                        (j * cellPixelSize) + cellPixelSize / 2 - textWidth / 2,
                        (i * cellPixelSize) + cellPixelSize / 2 + textHeight / 2F,
                        permanentCellTextPaint)
                }
            }
        }
    }

    fun processScan(results: Text) {
        for(block in results.textBlocks) {
            for(line in block.lines) {
                for(element in line.elements) {
                    // Check if the element is a single digit
                    if(element.text.length == 1 && element.text[0].isDigit()) {
                        val bounds = element.boundingBox
                        if(bounds != null) {
                            val possibleX = (bounds.centerX() - xPadding) / 330
                            val possibleY = (bounds.centerY() - yPadding) / 330
                            Log.d("SCANBOARD", "\"${element.text}\" at ($possibleX, $possibleY).")
                            if(possibleX < 9 && possibleY < 9)
                                board[possibleY][possibleX] = element.text.toInt()
                        }
                    }
                }
            }
        }
        invalidate()
    }
}