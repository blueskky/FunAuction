package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.`fun`.auction.Constants
import com.`fun`.auction.R
import com.`fun`.auction.model.CashConfig
import com.`fun`.auction.model.Event
import com.`fun`.auction.model.WeChatAuthEvent
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.trello.rxlifecycle4.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class AliAccountActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?, data: CashConfig?) {
            val intent = Intent(context, AliAccountActivity::class.java)
            intent.putExtra("data", data)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_add_account

    override fun init() {
        initTitle(iv_back, tv_title, "添加提现账户")
        var data = intent.getSerializableExtra("data") as CashConfig?

        iv_tag.setImageResource(R.drawable.ali_pay)
        if (!data?.zfb.isNullOrEmpty()) {
            et_account.setText(data?.zfb)
            et_name.setText(data?.zfb_true_name)
        }

        tv_add.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_add -> add()
        }
    }

    private fun add() {
        val account = et_account.text.toString().trim()
        val name = et_name.text.toString().trim()
        if (account.isNullOrEmpty()) {
            MyToast.show("身份证号码不能为空")
            return
        }

        if (name.isNullOrEmpty()) {
            MyToast.show("真实姓名不能为空")
            return
        }

        val map = mapOf("oper_type" to "1",  "zfb" to account, "zfb_true_name" to name)

        HttpManager.api?.setCashConfig(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(Event(Event.SET_CASH))
                    finish()
                }
            })
    }



}