package com.`fun`.auction.ui.dialog

import android.content.Context
import android.view.View
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.dialog_offer_success.*

class PrePatSuccessDialog(context: Context?) : BaseDialog(context), View.OnClickListener {


    override fun getLayoutId() = R.layout.dialog_offer_success

    override fun init() {
        btn_pat.setOnClickListener { dismiss() }

    }

    override fun onClick(v: View?) {

    }


}