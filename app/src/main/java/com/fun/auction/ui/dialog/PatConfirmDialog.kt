package com.`fun`.auction.ui.dialog

import android.content.Context
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.model.GoodsDetail
import com.`fun`.auction.model.Product
import kotlinx.android.synthetic.main.dialog_pat.*

class PatConfirmDialog(context: Context?, name: String?) : BaseDialog(context), View.OnClickListener {

    var goodsName = name
    var callback: DialogCallback? = null

    override fun getLayoutId() = R.layout.dialog_pat

    override fun init() {
        tvName.text = goodsName
        tvConfirm.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvConfirm -> callback?.onConfirm()
            R.id.tvCancel -> callback?.onCancel()
        }
        dismiss()
    }
}