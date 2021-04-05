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

    private var board = Array(rows) {
        _ -> Array(cols) {
        _ -> 0 }
    }

    private val borderLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 8F
    }

    private val permanentCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#FF9BA5C1")
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
        drawFrame(canvas)
    }

    private fun drawFrame(canvas: Canvas) {
        val padding = 4F
        val halfCell = cellPixelSize / 2
        val end = width - padding
        canvas.drawLine(
            padding,
            padding,
            padding + halfCell,
            padding,
            borderLinePaint
        )

        // Top Left
        canvas.drawLine( padding, padding, padding + halfCell, padding, borderLinePaint)
        canvas.drawLine( padding, padding, padding, padding + halfCell, borderLinePaint )

        // Top Right
        canvas.drawLine( end - halfCell, padding, end, padding, borderLinePaint)
        canvas.drawLine( end, padding, end, padding + halfCell, borderLinePaint )

        // Bottom Left
        canvas.drawLine( padding, end, padding + halfCell, end, borderLinePaint)
        canvas.drawLine( padding, end - halfCell, padding, end, borderLinePaint )

        // Bottom Right
        canvas.drawLine( end - halfCell, end, end, end, borderLinePaint )
        canvas.drawLine( end, end - halfCell, end, end, borderLinePaint )
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
        emptyBoard()
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
                            if(possibleX < 9 && possibleY < 9 && possibleX > -1 && possibleY > -1)
                                board[possibleY][possibleX] = element.text.toInt()
                        }
                    }
                }
            }
        }
        invalidate()
    }

    fun boardToString() : String {
        var boardLayout = ""
        board.forEach { row ->
            row.forEach {
                boardLayout += "$it,"
            }
        }
        return boardLayout.substring(0, boardLayout.length - 1)
    }

    private fun emptyBoard() {
        for(i in board.indices) {
            for(j in board[i].indices) {
                board[i][j] = 0
            }
        }
    }
}