package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.gyf.immersionbar.ImmersionBar
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.`fun`.auction.R

abstract class BaseActivity : RxAppCompatActivity() {

    lateinit var mContext: Context

    companion object {
        fun start(context: Context, cls: Class<*>) {
            context.startActivity(Intent(context, cls))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        mContext = this
        initStatusBar()
        init()
        initData()
    }

    open fun initData() {

    }

    open fun initStatusBar() {
        if (fullScreen()) {
            ImmersionBar.with(this).init()
        } else {
            ImmersionBar.with(this)
                //使用该属性,必须指定状态栏颜色
                .fitsSystemWindows(true)
                .statusBarColor(R.color.common_bg)
                .init()
        }

    }

    open fun fullScreen(): Boolean {
        return false
    }

    fun getData(k: Int = 4): ArrayList<Any> {
        val list = arrayListOf<Any>()
        for (i in 1..k) {
            list.add(i)
        }
        return list;
    }


    fun initTitle(ivBack: ImageView?, tvTitle: TextView?, title: String) {
        tvTitle?.text = title
        ivBack?.setOnClickListener { onBackPressed() }
    }


    abstract fun getLayoutId(): Int


    abstract fun init()
}