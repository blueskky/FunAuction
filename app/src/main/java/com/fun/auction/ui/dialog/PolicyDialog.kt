package com.`fun`.auction.ui.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.ui.WebViewActivity
import kotlinx.android.synthetic.main.dialog_policy.*

class PolicyDialog(context: Context) : BaseDialog(context), View.OnClickListener {

    val user_privacy = "file:///android_asset/user_privacy.html"
    var dialogCallback: DialogCallback? = null

    override fun getLayoutId() = R.layout.dialog_policy

    override fun init() {
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        val style = SpannableStringBuilder(context.resources.getString(R.string.policy_rule))

        //设置部分文字点击事件
        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                WebViewActivity.start(context, "隐私政策", user_privacy)
            }
        }
        style.setSpan(clickableSpan2, 91, 97, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        //设置部分文字颜色
        val foregroundColorSpan2 = ForegroundColorSpan(Color.parseColor("#4DB9E3"))
        style.setSpan(foregroundColorSpan2, 91, 97, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        //配置给TextView
        tv_content.movementMethod = LinkMovementMethod.getInstance()
        tv_content.text = style

        tv_cancel.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> {
                (context as Activity).finish()
            }
            R.id.tv_confirm -> {
                dialogCallback?.onConfirm()
            }
        }
        dismiss()
    }
}