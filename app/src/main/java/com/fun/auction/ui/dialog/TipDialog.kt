package com.`fun`.auction.ui.dialog

import android.content.Context
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.dialog_tip.*

class TipDialog(context: Context) : BaseDialog(context) {


    override fun getLayoutId() = R.layout.dialog_tip

    override fun init() {
        tv_confirm.setOnClickListener {
            dismiss()
        }
    }
}