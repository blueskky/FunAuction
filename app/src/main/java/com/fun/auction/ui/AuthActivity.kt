package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import com.`fun`.auction.R
import com.`fun`.auction.model.AuthRefreshEvent
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus

class AuthActivity : BaseActivity() {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, AuthActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_auth

    override fun init() {
        initTitle(iv_back, tv_title, "实名认证")

        tv_add.setOnClickListener {
            auth()
        }

    }

    private fun auth() {
        val name = et_name.text.toString().trim()
        val code = et_code.text.toString().trim()

        if (name.isNullOrEmpty()) {
            MyToast.show("真实姓名不能为空")
            return
        }
        if (code.isNullOrEmpty()) {
            MyToast.show("身份证号码不能为空")
            return
        }

        val map = mapOf("true_name" to name, "id_card" to code)
        HttpManager.api?.auth(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(AuthRefreshEvent())
                    finish()
                }
            })
    }
}