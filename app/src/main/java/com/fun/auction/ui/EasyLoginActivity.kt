package com.`fun`.auction.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.activity_login.*

class EasyLoginActivity : BaseActivity(), View.OnClickListener {


    var phone: String? = null

    override fun fullScreen(): Boolean {
        return true
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun init() {
        setTheme(R.style.theme)
        btn_login.setOnClickListener(this)
        tv_other.setOnClickListener(this)

        requestPermission()
    }

    private fun requestPermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.requestEachCombined(Manifest.permission.READ_PHONE_STATE)
            .subscribe { permission: Permission ->  // will emit 1 Permission object
                if (permission.granted) {
                    // All permissions are granted !
                    readPhoneNum()
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // At least one denied permission without ask never again
                    val tip = "一键登录需要读取本机号码，请开启相关权限"
                    AlertDialog.Builder(mContext).setMessage("")
                        .setPositiveButton(
                            "确定"
                        ) { dialog, which ->
                            requestPermission()
                        }
                        .setNegativeButton(
                            "取消"
                        ) { dialog, which ->
                            finish()
                        }.setCancelable(false).create().show()
                } else {
                    // At least one denied permission with ask never again
                    // Need to go to the settings
                    val tip = "一键登录需要读取本机号码，请前往设置中心开启权限"
                    AlertDialog.Builder(mContext).setMessage(tip)
                        .setPositiveButton(
                            "确定"
                        ) { dialog, which ->
                            goSetting()
                            finish()
                        }
                        .setNegativeButton(
                            "取消"
                        ) { dialog, which ->
                            finish()
                        }.setCancelable(false).create().show()
                }
            }
    }

    private fun goSetting() {
        val intent = Intent(Settings.ACTION_SETTINGS)
        startActivity(intent)
    }

    private fun readPhoneNum() {
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phone = manager.line1Number
        if (!TextUtils.isEmpty(phone)) {
            tv_phone.text = phone
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> login()
            R.id.tv_other -> other()
        }
    }

    private fun other() {
        start(mContext,PhoneLoginActivity::class.java)
        finish()
    }

    private fun login() {
    }


}