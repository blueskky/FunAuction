package com.`fun`.auction.util

import android.app.Activity
import android.content.Context
import com.`fun`.auction.Constants
import com.`fun`.auction.model.PayConfig
import com.`fun`.auction.model.PayResult
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.Executors

object PayHelper {

    fun wxPay(context: Context, config: PayConfig) {
        val api = WXAPIFactory.createWXAPI(context, config.appid)
        api.registerApp(config.appid)

        val request = PayReq()
        request.appId = config.appid
        request.partnerId = config.partnerid
        request.prepayId = config.prepayid
        request.packageValue = config.`package`
        request.nonceStr = config.noncestr
        request.timeStamp = config.timestamp
        request.sign = config.sign
        api.sendReq(request)
    }

    //必须异步调用
    fun aliPay(context: Activity, orderInfo: String) {
        Executors.newCachedThreadPool().execute(object : Runnable {
            override fun run() {
                val alipay = PayTask(context)
                val result: Map<String, String> = alipay.payV2(orderInfo, true)
                context.runOnUiThread {
                    when (result["resultStatus"]) {
                        "9000" -> EventBus.getDefault().post(PayResult())
                        else -> {
                            val s = result["memo"] ?: "支付失败"
                            EventBus.getDefault().post(PayResult(1, s))
                        }
                    }
                }
            }
        })
    }
}
