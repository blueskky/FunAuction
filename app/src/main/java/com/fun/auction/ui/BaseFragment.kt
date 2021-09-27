package com.`fun`.auction.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle4.components.support.RxFragment
import com.`fun`.auction.MyApplication
import com.`fun`.auction.ui.dialog.LoadingDialog


abstract class BaseFragment : RxFragment() {


    lateinit var mContext: Context
    open val pos: Int? = 0
    var loadingDialog: LoadingDialog? = null

    companion object {
        fun start(context: Context, cls: Class<*>) {
            context.startActivity(Intent(context, cls))
        }
    }

    var firstLoad: Boolean = false
    var isRuning: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(getLayoutId(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity ?: MyApplication.mContext
        isRuning=true
        init()

        initData()
    }

    override fun onResume() {
        super.onResume()
        if (!firstLoad) {
            firstLoad = true
            lazyInit()
        }

    }

    open fun lazyInit() {
    }

    open fun initData() {
    }


    fun getData(): ArrayList<Any> {
        val list = arrayListOf<Any>()
        for (i in 0..4) {
            list.add(i)
        }
        return list;
    }


    abstract fun getLayoutId(): Int

    abstract fun init()


    fun showLoading(show: Boolean) {
        if (show) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(mContext)
            }
            loadingDialog?.show()

        } else {
            loadingDialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isRuning=false
    }


}