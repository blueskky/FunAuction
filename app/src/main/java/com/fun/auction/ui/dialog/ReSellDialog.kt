package com.`fun`.auction.ui.dialog

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.model.ReSellInfo
import kotlinx.android.synthetic.main.dialog_re_sell.*

class ReSellDialog(context: Context, data: ReSellInfo?) : BaseDialog(context) {

    var dialogCallback: DialogCallback? = null

    var data = data

    override fun getLayoutId() = R.layout.dialog_re_sell

    override fun init() {

        tv_name.text = data?.goods_name
        tv_price2.text = "预估转卖价：${data?.price_range}"

        if (!TextUtils.isEmpty(data?.goods_price)) {
            tv_price.text = "市场价：${data?.goods_price}"
            tv_price.visibility = View.VISIBLE
        }

        tv_confirm.setOnClickListener {
            dialogCallback?.onConfirm()
            dismiss()
        }
    }
}