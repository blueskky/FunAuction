package com.`fun`.auction.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import com.`fun`.auction.*
import com.`fun`.auction.model.VersionCheck
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.dialog.DialogCallback
import com.`fun`.auction.ui.dialog.DownloadDialog
import com.`fun`.auction.ui.dialog.UpdateDialog
import com.`fun`.auction.util.MyToast
import com.blankj.utilcode.util.AppUtils
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.common_title.*

class AboutActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, AboutActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_about

    override fun init() {
        initTitle(iv_back, tv_title, "关于趣拍拍")

        ll_check.setOnClickListener(this)
        tv_copy.setOnClickListener(this)

        tv_version.text = "当前版本\n${AppUtils.getAppVersionName()}"

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_check -> check()
            R.id.tv_copy -> copy()
        }
    }

    private fun copy() {
        val clipboardManager = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText("Label", "bytesmartad@163.com")
        clipboardManager.setPrimaryClip(mClipData)
        MyToast.show("已复制")
    }

    private fun check() {
        val map = mapOf(
            "ver" to AppUtils.getAppVersionCode().toString(),
            "ver_sp" to BuildConfig.VERSION_SP,     //xiaomi
            "ver_up" to AppUtils.getAppVersionCode().toString()      //20210603
        )
        HttpManager.api?.versionCheck(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<VersionCheck>() {
                override fun onSuccess(code: Int, msg: String, data: VersionCheck?) {
                    data?.let {
                        if (it.is_up == 0) {
                            MyToast.show("已是最新版本")

                        } else if (it.is_up == 1 || it.is_up == 2) {
                            val updateDialog = UpdateDialog(mContext, data.des, data.is_up == 2)
                            updateDialog.show()
                            updateDialog.callback = object : DialogCallback {

                                override fun onConfirm() {
                                    val downloadDialog = DownloadDialog(this@AboutActivity)
                                    downloadDialog.setDownload(data.up_url, "funAuction.apk", it.is_up == 2)
                                    downloadDialog.show()
                                }
                            }
                        }
                    }
                }
            })
    }
}