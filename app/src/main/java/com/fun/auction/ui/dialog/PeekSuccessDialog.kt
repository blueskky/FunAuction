package com.`fun`.auction.ui.dialog

import android.content.Context
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.model.Peek

import kotlinx.android.synthetic.main.dialog_price_tip.*

class PeekSuccessDialog(context: Context?, name: String?, peek: Peek?) : BaseDialog(context), View.OnClickListener {


    var peek: Peek? = peek
    var name: String? = name
    var callback: DialogCallback? = null

    override fun getLayoutId() = R.layout.dialog_price_tip

    override fun init() {

        tv_name.text = name
        tv_price.text = "¥ ${peek?.current_price}"

        tv_pat.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_pat -> callback?.onConfirm()
            R.id.tv_cancel -> callback?.onCancel()
        }
        dismiss()
    }


}