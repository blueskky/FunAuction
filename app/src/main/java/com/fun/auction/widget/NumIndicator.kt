package com.`fun`.auction.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.youth.banner.indicator.BaseIndicator
import com.youth.banner.util.BannerUtils

class NumIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseIndicator(context, attrs, defStyleAttr) {

    var mWidth: Int = 0
    var mHeight: Int = 0
    var mRadius: Float = 0f

    init {
        mPaint.textSize = BannerUtils.dp2px(10F)
        mPaint.textAlign = Paint.Align.CENTER
        mWidth = BannerUtils.dp2px(30F).toInt()
        mHeight = BannerUtils.dp2px(20f).toInt()
        mRadius = BannerUtils.dp2px(20f)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val indicatorSize = config.indicatorSize
        if (indicatorSize < 1) {
            return
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val indicatorSize = config.indicatorSize
        if (indicatorSize < 1) {
            return
        }

        val rectF = RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat())
        mPaint.color = Color.parseColor("#70000000")
        canvas?.drawRoundRect(rectF, mRadius, mRadius, mPaint)

        val text = (config.currentPosition + 1).toString() + "/" + indicatorSize
        mPaint.setColor(Color.WHITE)
        canvas?.drawText(text, (width / 2).toFloat(), (height * 0.7).toFloat(), mPaint)
    }
}