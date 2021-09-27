package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.model.CashConfig
import com.`fun`.auction.model.Event
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import kotlinx.android.synthetic.main.activity_cash_account.*
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CashAccountActivity : BaseActivity(), View.OnClickListener {


    var data: CashConfig? = null

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, CashAccountActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_cash_account

    override fun init() {
        EventBus.getDefault().register(this)
        initTitle(iv_back, tv_title, "设置提款账户")
        ll_ali.setOnClickListener(this)
        ll_wechat.setOnClickListener(this)

        cb_ali.setOnClickListener(this)
        cb_wechat.setOnClickListener(this)

    }


    override fun initData() {
        HttpManager.api?.getCashConfig()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<CashConfig>() {
                override fun onSuccess(code: Int, msg: String, data: CashConfig?) {
                    this@CashAccountActivity.data = data

                    cb_ali.setImageResource(R.drawable.check_normal)
                    cb_wechat.setImageResource(R.drawable.check_normal)

                    when (data?.bingo_type) {
                        -1 -> "未设置 >"
                        0 -> cb_ali.setImageResource(R.drawable.check_checked)
                        2 -> cb_wechat.setImageResource(R.drawable.check_checked)
                    }

                    if (!data?.zfb.isNullOrEmpty()) {
                        tv_account.text = "支付宝账号：${data?.zfb}"
                        tv_name.text = "支付宝姓名：${data?.zfb_true_name}"
                        tv_name.visibility = View.VISIBLE
                    }

                    if (!data?.wxapp_true_name.isNullOrEmpty()) {
                        tv_we_account.text = "微信昵称：${data?.wxapp_nick}"
                        tv_we_name.text = "微信姓名：${data?.wxapp_true_name}"
                        tv_we_name.visibility = View.VISIBLE
                    }
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_ali -> AliAccountActivity.start(mContext, data)
            R.id.ll_wechat -> WeChatAccountActivity.start(mContext, data)
            R.id.cb_ali -> {
                if (data?.bingo_type == 0) {
                    return
                }
                setConfig("1")
            }
            R.id.cb_wechat -> {
                if (data?.bingo_type == 2) {
                    return
                }
                setConfig("3")
            }
        }
    }


    fun setConfig(type: String) {
        val map = mapOf("oper_type" to "0", "cash_type" to type)
        HttpManager.api?.selectCashConfig(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(Event(Event.SElECT_CASH))
                    initData()
                }
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    fun refresh(event: Event) {
        if (event.type == Event.SET_CASH) {
            initData()
        }
    }
}