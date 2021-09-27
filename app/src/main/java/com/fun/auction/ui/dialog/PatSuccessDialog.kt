package com.`fun`.auction.ui.dialog

import android.content.Context
import com.`fun`.auction.R
import com.`fun`.auction.ui.AddressActivitiy
import com.`fun`.auction.ui.BoxOrderActivity
import com.`fun`.auction.ui.PatRecordActivity
import kotlinx.android.synthetic.main.dialog_pat_success.*

class PatSuccessDialog(context: Context?) : BaseDialog(context) {


    override fun getLayoutId() = R.layout.dialog_pat_success

    override fun init() {
        setCanceledOnTouchOutside(false)

        btn_adress.setOnClickListener {
//            BoxOrderActivity.start(context,)
            PatRecordActivity.start(context, 1)
            dismiss()
        }
        iv_close.setOnClickListener {
            dismiss()
        }
    }

}