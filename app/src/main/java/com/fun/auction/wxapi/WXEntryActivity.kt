package com.`fun`.auction.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.`fun`.auction.Constants
import com.`fun`.auction.model.WeChatAuthEvent
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus

class WXEntryActivity : Activity(), IWXAPIEventHandler {


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
        when (resp?.type) {
            ConstantsAPI.COMMAND_SENDAUTH -> {
                if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                    val resp1 = resp as SendAuth.Resp
                    val code = resp1.code
                    EventBus.getDefault().post(WeChatAuthEvent(0, code))
                } else {
                    EventBus.getDefault().post(WeChatAuthEvent(-1, ""))
                }
            }

            ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> {
            }
            ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM -> {
            }

        }
        finish()

    }
}