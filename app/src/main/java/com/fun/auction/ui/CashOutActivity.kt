package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.model.AuthRefreshEvent
import com.`fun`.auction.model.Event
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import kotlinx.android.synthetic.main.activity_cash_out.*
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus

class CashOutActivity : BaseActivity(){


    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, CashOutActivity::class.java))
        }
    }


    override fun getLayoutId() = R.layout.activity_cash_out

    override fun init() {
        initTitle(iv_back, tv_title, "提现")
        tv_cash.setOnClickListener {
            cash()
        }
    }

    private fun cash() {
        val money = et_money.text.toString().trim()
        if (money.isNullOrEmpty()) {
            MyToast.show("请填写提现金额")
            return
        }

        val map = mapOf("money" to money)
        HttpManager.api?.cashOut(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(Event(Event.CASH_OUT))
                    finish()
                }
            })
    }


}