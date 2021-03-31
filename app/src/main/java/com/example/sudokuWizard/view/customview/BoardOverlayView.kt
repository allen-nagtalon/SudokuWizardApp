package com.example.sudokuWizard.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.mlkit.vision.text.Text
import kotlin.math.min

class BoardOverlayView(context : Context,
                       attributeSet: AttributeSet) : View(context, attributeSet) {

    private var cellPixelSize = 0F

    private var rows = 9
    private var cols = 9
    private var rowSubSize = 3
    private var colSubSize = 3

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Set height and width of the SudokuBoardView to be the min of the two dimensions
        val size = min(widthMeasureSpec - 64, heightMeasureSpec - 64)
        setMeasuredDimension(size, size)

        /** Adjust this later to account for varying sized boards. **/
    }

    override fun onDraw(canvas : Canvas) {
        cellPixelSize = (width / rows).toFloat()
    }

    fun processScan(results: Text) {
        for(block in results.textBlocks) {
            for(line in block.lines) {
                for(element in line.elements) {
                    // Check if the element is a single digit
                    Log.d("SCANBOARD", "${element.text}: ${element.boundingBox}")
                }
            }
        }
    }
}