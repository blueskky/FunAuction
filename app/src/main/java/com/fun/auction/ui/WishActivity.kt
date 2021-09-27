package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.activity_wish.*
import kotlinx.android.synthetic.main.common_title.iv_back
import kotlinx.android.synthetic.main.common_title.tv_title

class WishActivity : BaseActivity() {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, WishActivity::class.java))
        }
    }


    override fun getLayoutId() = R.layout.activity_wish

    override fun init() {
        initTitle(iv_back, tv_title, "许个愿")

        val list = listOf("淘宝", "京东", "拼多多", "唯品会", "其他")

        spinner.attachDataSource(list)
        spinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            val item = spinner.getItemAtPosition(position) as String
            etPlatform.setText(item)
        }

    }
}