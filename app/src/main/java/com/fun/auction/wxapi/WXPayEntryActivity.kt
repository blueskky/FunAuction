package com.`fun`.auction.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.`fun`.auction.Constants
import com.`fun`.auction.model.PayResult
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus


class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    var createWXAPI: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createWXAPI = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID)
        createWXAPI?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        createWXAPI?.handleIntent(intent, this)
    }


    override fun onReq(req: BaseReq?) {
        var re = req
    }

    override fun onResp(resp: BaseResp?) {
        if (resp != null && resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> EventBus.getDefault().post(PayResult())
                BaseResp.ErrCode.ERR_USER_CANCEL -> EventBus.getDefault().post(PayResult(1, "支付取消"))
                else -> EventBus.getDefault().post(PayResult(1, "支付失败"))
            }
        }
        finish()
    }
}