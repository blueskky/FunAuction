package com.`fun`.auction.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.`fun`.auction.R

abstract class BaseDialog(context: Context?) : AlertDialog(context, R.style.dialog_center) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        init()
    }

    abstract fun getLayoutId(): Int

    abstract fun init()


}