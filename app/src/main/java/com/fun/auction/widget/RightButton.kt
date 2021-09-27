package com.`fun`.auction.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.right_button.view.*


/**
 * view的三个构造合并成一个  再java xml 中使用要 添加 JvmOverloads 兼容java
 */
class RightButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attributeSet, defStyleAttr) {

    val tvText: TextView = tv_text
    val rightIcon: ImageView = right_icon

    init {
        LayoutInflater.from(context).inflate(R.layout.right_button, this, true)
    }

}