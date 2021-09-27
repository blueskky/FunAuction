package com.`fun`.auction.ui

import com.`fun`.auction.R

class SplashActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_splash_main
    override fun init() {
        start(mContext,MainActivity::class.java)
        finish()
    }
}