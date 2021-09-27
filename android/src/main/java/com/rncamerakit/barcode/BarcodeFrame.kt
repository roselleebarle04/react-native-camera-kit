package com.rncamerakit.barcode

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.annotation.ColorInt
import android.graphics.Color

import com.rncamerakit.R

class BarcodeFrame(context: Context) : View(context) {
    private var borderPaint: Paint = Paint()
    private var firstBorderPaint: Paint = Paint()
    private var laserPaint: Paint = Paint()
    private var shadowPaint: Paint = Paint()
    var frameRect: Rect = Rect()

    private var frameWidth = 0
    private var frameHeight = 0
    private var borderMargin = 0
    private var previousFrameTime = System.currentTimeMillis()
    private var laserY = 0

    private var shadowColor = Color.Black;
    private var offsetX = -25;
    private var offsetY = 30;
    private var blurRadius = 5;

    private fun init(context: Context) {
        borderPaint = Paint()
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = STROKE_WIDTH.toFloat()
        firstBorderPaint = Paint()
        firstBorderPaint.style = Paint.Style.STROKE
        firstBorderPaint.strokeWidth = FIRST_BORDER_STROKE_WIDTH.toFloat()
        laserPaint.style = Paint.Style.STROKE
        laserPaint.strokeWidth = STROKE_WIDTH.toFloat()
        shadowPaint.setColor(shadowColor);
        shadowPaint.setMaskFilter(new BlurMaskFilter(
                    blurRadius /* shadowRadius */,
                    BlurMaskFilter.Blur.NORMAL));

        borderMargin = context.resources.getDimensionPixelSize(R.dimen.border_length)
        firstBorderMargin = context.resources.getDimensionPixelSize(R.dimen.border_length_bg)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        frameWidth = measuredWidth
        frameHeight = measuredHeight
        val marginWidth = width / WIDTH_SCALE
        val marginHeight = (height / HEIGHT_SCALE).toInt()
        frameRect.left = marginWidth
        frameRect.right = width - marginWidth
        frameRect.top = marginHeight
        frameRect.bottom = height - marginHeight
    }

    override fun onDraw(canvas: Canvas) {
        val timeElapsed = System.currentTimeMillis() - previousFrameTime
        super.onDraw(canvas)
        drawBorder(canvas)
        drawLaser(canvas, timeElapsed)
        previousFrameTime = System.currentTimeMillis()
        this.invalidate(frameRect)
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawLine(frameRect.left.toFloat(), frameRect.top.toFloat(), frameRect.left.toFloat(), (frameRect.top + firstBorderMargin).toFloat(), firstBorderPaint)
        canvas.drawLine(frameRect.left.toFloat(), frameRect.top.toFloat(), (frameRect.left + firstBorderMargin).toFloat(), frameRect.top.toFloat(), firstBorderPaint)
        canvas.drawLine(frameRect.left.toFloat(), frameRect.bottom.toFloat(), frameRect.left.toFloat(), (frameRect.bottom - firstBorderMargin).toFloat(), firstBorderPaint)
        canvas.drawLine(frameRect.left.toFloat(), frameRect.bottom.toFloat(), (frameRect.left + firstBorderMargin).toFloat(), frameRect.bottom.toFloat(), firstBorderPaint)
        canvas.drawLine(frameRect.right.toFloat(), frameRect.top.toFloat(), (frameRect.right - firstBorderMargin).toFloat(), frameRect.top.toFloat(), firstBorderPaint)
        canvas.drawLine(frameRect.right.toFloat(), frameRect.top.toFloat(), frameRect.right.toFloat(), (frameRect.top + firstBorderMargin).toFloat(), firstBorderPaint)
        canvas.drawLine(frameRect.right.toFloat(), frameRect.bottom.toFloat(), frameRect.right.toFloat(), (frameRect.bottom - firstBorderMargin).toFloat(), firstBorderPaint)
        canvas.drawLine(frameRect.right.toFloat(), frameRect.bottom.toFloat(), (frameRect.right - firstBorderMargin).toFloat(), frameRect.bottom.toFloat(), firstBorderPaint)
        //Overlay
        canvas.drawLine(frameRect.left.toFloat(), frameRect.top.toFloat(), frameRect.left.toFloat(), (frameRect.top + borderMargin).toFloat(), borderPaint)
        canvas.drawLine(frameRect.left.toFloat(), frameRect.top.toFloat(), (frameRect.left + borderMargin).toFloat(), frameRect.top.toFloat(), borderPaint)
        canvas.drawLine(frameRect.left.toFloat(), frameRect.bottom.toFloat(), frameRect.left.toFloat(), (frameRect.bottom - borderMargin).toFloat(), borderPaint)
        canvas.drawLine(frameRect.left.toFloat(), frameRect.bottom.toFloat(), (frameRect.left + borderMargin).toFloat(), frameRect.bottom.toFloat(), borderPaint)
        canvas.drawLine(frameRect.right.toFloat(), frameRect.top.toFloat(), (frameRect.right - borderMargin).toFloat(), frameRect.top.toFloat(), borderPaint)
        canvas.drawLine(frameRect.right.toFloat(), frameRect.top.toFloat(), frameRect.right.toFloat(), (frameRect.top + borderMargin).toFloat(), borderPaint)
        canvas.drawLine(frameRect.right.toFloat(), frameRect.bottom.toFloat(), frameRect.right.toFloat(), (frameRect.bottom - borderMargin).toFloat(), borderPaint)
        canvas.drawLine(frameRect.right.toFloat(), frameRect.bottom.toFloat(), (frameRect.right - borderMargin).toFloat(), frameRect.bottom.toFloat(), borderPaint)
    }

    private fun drawLaser(canvas: Canvas, timeElapsed: Long) {
        if (laserY > frameRect.bottom || laserY < frameRect.top) laserY = frameRect.top
        canvas.drawLine((frameRect.left + STROKE_WIDTH).toFloat(), laserY.toFloat(), (frameRect.right - STROKE_WIDTH).toFloat(), laserY.toFloat(), shadowPaint)
        canvas.drawLine((frameRect.left + STROKE_WIDTH).toFloat(), laserY.toFloat(), (frameRect.right - STROKE_WIDTH).toFloat(), laserY.toFloat(), laserPaint)
        laserY += (timeElapsed / ANIMATION_SPEED).toInt()
    }

    fun setFrameColor(@ColorInt borderColor: Int) {
        borderPaint.color = borderColor
    }

    fun setLaserColor(@ColorInt laserColor: Int) {
        laserPaint.color = laserColor
    }

    companion object {
        private const val FIRST_BORDER_STROKE_WIDTH = 5
        private const val STROKE_WIDTH = 10
        private const val ANIMATION_SPEED = 8
        private const val WIDTH_SCALE = 7
        private const val HEIGHT_SCALE = 5.5
    }

    init {
        init(context)
    }
}
