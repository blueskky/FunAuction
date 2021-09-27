package com.`fun`.auction.ui.dialog

import android.content.Context
import com.`fun`.auction.R
import com.`fun`.auction.model.Goods
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_sell.*

class SellDialog(context: Context?, img: String, count: Int) : BaseDialog(context) {

    var callback: DialogCallback? = null
    var img = img
    var count = count

    override fun getLayoutId() = R.layout.dialog_sell

    override fun init() {
        Glide.with(context).load(img).into(iv_cover)
        tv_count.text = "共一件"

        btn_sell.setOnClickListener {
            callback?.onConfirm()
            dismiss()
        }
    }
}