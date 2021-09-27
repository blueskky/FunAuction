package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.`fun`.auction.Constants
import com.`fun`.auction.R
import com.`fun`.auction.model.CashConfig
import com.`fun`.auction.model.Event
import com.`fun`.auction.model.WeChatAuthEvent
import com.`fun`.auction.model.WxInfo
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.trello.rxlifecycle4.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_we_chat_account.*
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class WeChatAccountActivity() : BaseActivity(), View.OnClickListener {

    var wxInfo: WxInfo? = null

    companion object {
        fun start(context: Context?, data: CashConfig?) {
            val intent = Intent(context, WeChatAccountActivity::class.java)
            intent.putExtra("data", data)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_we_chat_account

    override fun init() {
        EventBus.getDefault().register(this)
        initTitle(iv_back, tv_title, "添加提现账户")
        var data = intent.getSerializableExtra("data") as CashConfig?

        iv_tag.setImageResource(R.drawable.wechat_pay)
        tv_get.visibility = View.VISIBLE
        if (!data?.wxapp_nick.isNullOrEmpty()) {
            et_nick.setText(data?.wxapp_nick)
            et_name.setText(data?.wxapp_true_name)
        }

        tv_add.setOnClickListener(this)
        tv_get.setOnClickListener(this)
    }

    private fun add() {
        val name = et_name.text.toString().trim()

        if (wxInfo == null) {
            MyToast.show("请先获取微信授权信息")
            tv_get.setTextColor(Color.parseColor("#FFD50000"))
            return
        }

        if (name.isNullOrEmpty()) {
            MyToast.show("请填写真实姓名")
            return
        }
        val map = mutableMapOf<String, String?>()
        map["oper_type"] = "3"
        map["wxapp_id"] = wxInfo?.openid
        map["wxapp_nick"] = wxInfo?.nickname
        map["wxapp_true_name"] = name
        map["wxapp_sex"] = wxInfo?.sex

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_add -> add()
            R.id.tv_get -> {
                getWechatName()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun getWechatName() {
        val wxapi = WXAPIFactory.createWXAPI(mContext, Constants.WX_APP_ID)
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "xxxx"
        wxapi.sendReq(req) //发起微信登录，回调到WXEntryActivity¬

    }


    @Subscribe
    fun getAuthCode(event: WeChatAuthEvent) {
        if (event.status == 0) {
            val code = event.code
            val map = mapOf("auth_code" to code, "wx_appid" to Constants.WX_APP_ID)
            HttpManager.api?.getWxInfo(map)
                ?.compose(SchedulersUtils.applySchedulers())
                ?.compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                ?.subscribe(object : SimpleObserver<WxInfo>() {
                    override fun onSuccess(code: Int, msg: String, data: WxInfo?) {
                        wxInfo = data
                        et_nick.setText(data?.nickname)
                    }
                })
        }
    }


}