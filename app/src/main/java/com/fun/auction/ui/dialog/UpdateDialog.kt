package com.`fun`.auction.ui.dialog

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.update_layout.*

class UpdateDialog(context: Context?, msg: String, cancel: Boolean) : BaseDialog(context) {

    val des = msg
    val canCancel = cancel

    var callback: DialogCallback? = null


    override fun getLayoutId(): Int {
        return R.layout.update_layout
    }

    override fun init() {
        setCanceledOnTouchOutside(false)
        setCancelable(false)

        iv_cancel.visibility = if (canCancel) View.VISIBLE else View.GONE
        if (!TextUtils.isEmpty(des)) {
            tv_des.text = des
        }

        tv_confirm.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dismiss()
                callback?.onConfirm()
            }
        })
        iv_cancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dismiss()
            }
        })
    }
}