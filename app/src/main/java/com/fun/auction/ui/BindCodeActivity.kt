package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import com.`fun`.auction.R
import com.`fun`.auction.model.AuthRefreshEvent
import com.`fun`.auction.model.Event
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import kotlinx.android.synthetic.main.activity_bind_code.*
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus

class BindCodeActivity : BaseActivity() {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, BindCodeActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_bind_code

    override fun init() {
        initTitle(iv_back, tv_title, "绑定推广码")

        tv_add.setOnClickListener {
            bindcode()
        }

    }

    private fun bindcode() {

        val code = et_code.text.toString().trim()

        if (code.isNullOrEmpty()) {
            MyToast.show("推广码不能为空")
            return
        }

        val map = mapOf("code_id" to code)
        HttpManager.api?.bindCode(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    EventBus.getDefault().post(Event(Event.BIND_CODE))
                    MyToast.show(msg)
                    finish()
                }
            })
    }
}