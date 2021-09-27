package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import com.`fun`.auction.R
import com.`fun`.auction.model.MessageModel
import com.`fun`.auction.model.SystemMsg
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import kotlinx.android.synthetic.main.activity_message_detail.*
import kotlinx.android.synthetic.main.common_title.*
import java.text.SimpleDateFormat
import java.util.*

class MessageDetailActivity : BaseActivity() {


    val messageId by lazy {
        intent.getIntExtra("messageId", 0)
    }

    companion object {
        fun start(context: Context?, messageId: Int?) {
            val intent = Intent(context, MessageDetailActivity::class.java)
            intent.putExtra("messageId", messageId)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_message_detail

    override fun init() {
        initTitle(iv_back, tv_title, "消息详情")

    }

    override fun initData() {
        HttpManager.api?.messageDetail(messageId)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<SystemMsg>() {
                override fun onSuccess(code: Int, msg: String, data: SystemMsg?) {
                    data?.let {
                        tv_message.text = it.msg
                        val createTime = it.create_time * 1000
                        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val format = simpleDateFormat.format(Date(createTime))
                        tv_time.text = format
                    }
                }
            })
    }
}