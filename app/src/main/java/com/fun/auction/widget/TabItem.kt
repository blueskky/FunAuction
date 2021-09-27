package com.`fun`.auction.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.tab_item.view.*
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem

class TabItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTabItem(context, attrs, defStyleAttr) {


    var mDefaultDrawable: Drawable? = null
    var mCheckedDrawable: Drawable? = null

    var mDefaultTextColor = 0x56000000
    var mCheckedTextColor = 0x56000000
    var mChecked: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.tab_item, this, true)
    }

    fun initialize(@DrawableRes drawableRes: Int, @DrawableRes checkedDrawableRes: Int, title: String?) {
        mDefaultDrawable = ContextCompat.getDrawable(context, drawableRes)
        mCheckedDrawable = ContextCompat.getDrawable(context, checkedDrawableRes)
        tv_title.text = title
    }


    override fun setChecked(checked: Boolean) {
       if(checked){
           icon.setImageDrawable(mCheckedDrawable)
           tv_title.setTextColor(mCheckedTextColor)
       }else{
           icon.setImageDrawable(mDefaultDrawable)
           tv_title.setTextColor(mDefaultTextColor)
       }
        mChecked=checked
    }

    override fun setMessageNumber(number: Int) {
        messages.messageNumber=number
    }

    override fun setHasMessage(hasMessage: Boolean) {
        messages.setHasMessage(hasMessage)
    }

    override fun setTitle(title: String?) {
        tv_title.text = title
    }

    override fun setDefaultDrawable(drawable: Drawable?) {
        mDefaultDrawable = drawable
        if (!mChecked) {
            icon.setImageDrawable(drawable)
        }
    }

    override fun setSelectedDrawable(drawable: Drawable?) {
        mCheckedDrawable = drawable
        if (mChecked) {
            icon.setImageDrawable(drawable)
        }
    }

    override fun getTitle(): String {
        return tv_title.text.toString()
    }


    fun setTextDefaultColor(@ColorInt color: Int) {
        mDefaultTextColor = color
    }

    fun setTextCheckedColor(@ColorInt color: Int) {
        mCheckedTextColor = color
    }
}