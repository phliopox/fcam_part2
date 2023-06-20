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
    private val rectWidth = 15f
    private var tick = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (rectF in rectList) {
            canvas?.drawRect(rectF, redPaint)
        }
    }

    fun addAmplitude(maxAmplitude: Float) {
        val amplitude =
            (maxAmplitude / Short.MAX_VALUE) * this.height * 0.8f// maxAmplitude / Short.MAX_VALUE -> 0~1사이값

        ampList.add(maxAmplitude)
        rectList.clear()
        //가로 ui 에 몇개의 rect가 들어갈 수 있는가 width
        val maxRect = (this.width / rectWidth).toInt()
        val amps = ampList.takeLast(maxRect)

        for ((i, amp) in amps.withIndex()) {
            val recF = RectF()
            recF.top = (this.height / 2) - amp / 2 // 위로 절반 아래로 절반으로
            recF.bottom = recF.top + amp
            recF.left = i * rectWidth
            recF.right = recF.left + (rectWidth -5f) //마진 5픽셀

            rectList.add(recF)
        }
        invalidate()
    }

    fun replayAmplitude() {
        rectList.clear()
        val maxRect = (this.width / rectWidth).toInt()
        val amps = ampList.take(tick).takeLast(maxRect)
        for ((i, amp) in amps.withIndex()) {
            val recF = RectF()
            recF.top = (this.height / 2) - amp / 2 // 위로 절반 아래로 절반으로
            recF.bottom = recF.top + amp
            recF.left = i * rectWidth
            recF.right = recF.left + (rectWidth -5f) //마진 5픽셀

            rectList.add(recF)
        }
        tick++
        invalidate()
    }

    fun clearData() {
        ampList.clear()
    }

    fun clearWave() {
        rectList.clear()
        tick = 0
        invalidate()
    }
}