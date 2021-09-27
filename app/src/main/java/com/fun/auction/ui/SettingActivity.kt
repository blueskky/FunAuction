package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.model.AddressModel
import com.`fun`.auction.model.AuthRefreshEvent
import com.`fun`.auction.model.AuthResult
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SettingActivity : BaseActivity(), View.OnClickListener {


    val user_policy = "file:///android_asset/user_policy.html"
    val user_privacy = "file:///android_asset/user_privacy.html"
    var isReal: Int? = 0

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, SettingActivity::class.java)
            context?.startActivity(intent)
        }
    }


    override fun getLayoutId() = R.layout.activity_setting

    override fun init() {
        EventBus.getDefault().register(this)
        initTitle(iv_back, tv_title, "设置中心")

        ll_adress.setOnClickListener(this)
        ll_pay_pwd.setOnClickListener(this)
        ll_auth.setOnClickListener(this)
        ll_policy.setOnClickListener(this)
        ll_privacy.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
        ll_about.setOnClickListener(this)
    }


    override fun initData() {
        HttpManager.api?.authStatus()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<AuthResult>() {
                override fun onSuccess(code: Int, msg: String, data: AuthResult?) {
                    isReal = data?.is_real
                    when (isReal) {
                        0 -> tv_auth_result.text = "未认证 >"
                        1 -> tv_auth_result.text = "已认证 >"
                        2 -> tv_auth_result.text = "认证中 >"
                        -1 -> tv_auth_result.text = "已驳回 >"
                    }
                }
            })

        HttpManager.api?.addressList()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<AddressModel>() {
                override fun onSuccess(code: Int, msg: String, data: AddressModel?) {
                    val list = data?.list
                    if (list.isNullOrEmpty()) {
                        tv_address_status.text = "未设置 >"
                    } else {
                        tv_address_status.text = "已设置 >"
                    }
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_adress -> AddressActivitiy.start(mContext)
            R.id.ll_pay_pwd -> ""
            R.id.ll_auth -> if (isReal == 0 || isReal == -1) {
                AuthActivity.start(mContext)
            } else {
                MyToast.show(if (isReal == 1) "已认证" else "认证中")
            }
            R.id.ll_policy -> WebViewActivity.start(mContext, "用户协议", user_policy)
            R.id.ll_privacy -> WebViewActivity.start(mContext, "隐私政策", user_privacy)
            R.id.btn_logout -> logout()
            R.id.ll_about->AboutActivity.start(mContext)

        }
    }

    private fun logout() {
        MainActivity.logout(mContext)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun authRefresh(event: AuthRefreshEvent) {
        initData()
    }
}