package com.ph.fastcam_part2.chap2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.plus


class WaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private val ampList = mutableListOf<Float>()
    private val rectList = mutableListOf<RectF>()
    private val redPaint = Paint().apply {

        color = Color.RED
    }
    private val rectWidth = 10f
    private var tick = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for(rectF in rectList){
            canvas?.drawRect(rectF,redPaint)
        }
    }

    fun addAmplitude(maxAmplitude: Float) {

        ampList.add(maxAmplitude)
        rectList.clear()
        //가로 ui 에 몇개의 rect가 들어갈 수 있는가 width
        val maxRect = (this.width / rectWidth).toInt()
        val amps = ampList.takeLast(maxRect)

        for ((i, amp) in amps.withIndex()) {
            val recF = RectF()
            recF.top = 0f
            recF.bottom = amp
            recF.left = i * rectWidth
            recF.right = recF.left + rectWidth

            rectList.add(recF)
        }
        invalidate()
    }

    fun replayAmplitude(duration: Int) {
        rectList.clear()
        val maxRect =(this.width/rectWidth).toInt()
        val amps = ampList.take(tick).takeLast(maxRect)
        for ((i, amp) in amps.withIndex()) {
            val recF = RectF()
            recF.top = 0f
            recF.bottom = amp
            recF.left = i * rectWidth
            recF.right = recF.left + rectWidth

            rectList.add(recF)
        }
        tick++
        invalidate()
    }

    fun clearData(){
        ampList.clear()
    }
    fun clearWave(){
        rectList.clear()
        tick =0
        invalidate()
    }
}