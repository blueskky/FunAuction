package com.`fun`.auction.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.FrameLayout
import com.`fun`.auction.R

class XFrameLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private var mRatio: Float

    init {
        val ta: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RatioLayout)
        mRatio = ta.getFloat(R.styleable.RatioLayout_ratio, 1f)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightMeasureSpec = MeasureSpec.makeMeasureSpec((widthSize * mRatio).toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}