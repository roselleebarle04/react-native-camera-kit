package com.rncamerakit.barcode

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.annotation.ColorInt

import com.rncamerakit.R

class BarcodeFrame(context: Context) : View(context) {
    private var outsideFramePaint: Paint = Paint()
    private var centerFrameBorderPaint: Paint = Paint()
    private var centerFramePaint: Paint = Paint()
    private var borderPaint: Paint = Paint()
    private var laserPaint: Paint = Paint()
    var frameRect: Rect = Rect()
    var centerFrameRect: Rect = Rect()
    var outsideFrameRect: Rect = Rect()

    private var frameWidth = 0
    private var frameHeight = 0
    private var borderMargin = 0
    private var previousFrameTime = System.currentTimeMillis()
    private var laserY = 0

    private fun init(context: Context) {
        borderPaint = Paint()
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = STROKE_WIDTH.toFloat()

        centerFramePaint = Paint()
        centerFramePaint.color = Color.TRANSPARENT
        centerFramePaint.style = Paint.Style.FILL
        centerFramePaint.strokeWidth = MAIN_BORDER_STROKE_WIDTH.toFloat()
        centerFramePaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        centerFrameBorderPaint = Paint()
        centerFrameBorderPaint.color = Color.LTGRAY
        centerFrameBorderPaint.style = Paint.Style.STROKE
        centerFrameBorderPaint.strokeWidth = MAIN_BORDER_STROKE_WIDTH.toFloat()

        outsideFramePaint = Paint()
        outsideFramePaint.color = Color.argb(0.5f,0f,0f,0f)
        outsideFramePaint.style = Paint.Style.FILL
        outsideFramePaint.strokeWidth = MAIN_BORDER_STROKE_WIDTH.toFloat()

        //todo add shader https://medium.com/@yuriyskul/different-ways-to-create-glowing-shapes-in-android-canvas-8b73010411fe
        laserPaint.style = Paint.Style.STROKE
        laserPaint.color = Color.LTGRAY
        laserPaint.strokeWidth = LASER_STROKE_WIDTH.toFloat()
        laserPaint.setAntiAlias(true);
        borderMargin = context.resources.getDimensionPixelSize(R.dimen.border_length)
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
        frameRect.bottom = width + marginWidth + marginWidth

        centerFrameRect.left = marginWidth
        centerFrameRect.right = width - marginWidth
        centerFrameRect.top = marginHeight
        centerFrameRect.bottom = width + marginWidth + marginWidth

        outsideFrameRect.left = 0
        outsideFrameRect.right = frameWidth
        outsideFrameRect.top = 0
        outsideFrameRect.bottom = frameHeight
    }

    override fun onDraw(canvas: Canvas) {
        val timeElapsed = System.currentTimeMillis() - previousFrameTime
        super.onDraw(canvas)
        drawSquare(canvas)
        drawBorder(canvas)
        drawLaser(canvas, timeElapsed)
        previousFrameTime = System.currentTimeMillis()
        this.invalidate(frameRect)
    }

    private fun drawSquare(canvas: Canvas) {
        canvas.drawRect(outsideFrameRect, outsideFramePaint)
        canvas.drawRect(centerFrameRect, centerFramePaint)
        canvas.drawRect(centerFrameRect, centerFrameBorderPaint)
    }

    private fun drawBorder(canvas: Canvas) {
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
        if (laserY > (frameRect.bottom - STROKE_WIDTH) || laserY < frameRect.top) laserY = frameRect.top
        canvas.drawLine((frameRect.left + STROKE_WIDTH).toFloat(), laserY.toFloat(), (frameRect.right - STROKE_WIDTH).toFloat(), laserY.toFloat(), laserPaint)
        laserY += (timeElapsed / ANIMATION_SPEED).toInt()
    }

    fun setFrameColor(@ColorInt borderColor: Int) {
        borderPaint.color = borderColor
        laserPaint.setShadowLayer(30f, 0f, 0f, borderColor);
    }

    fun setLaserColor(@ColorInt laserColor: Int) {
        //laserPaint.color = laserColor
    }

    companion object {
        private const val MAIN_BORDER_STROKE_WIDTH = 2.5
        private const val LASER_STROKE_WIDTH = 2.5
        private const val STROKE_WIDTH = 12
        private const val ANIMATION_SPEED = 4
        private const val WIDTH_SCALE = 7
        private const val HEIGHT_SCALE = 2.75
    }

    init {
        init(context)
    }
}
