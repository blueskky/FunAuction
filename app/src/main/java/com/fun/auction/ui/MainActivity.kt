package com.`fun`.auction.ui


import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.`fun`.auction.*
import com.`fun`.auction.model.LoginEvent
import com.`fun`.auction.model.VersionCheck
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.dialog.DialogCallback
import com.`fun`.auction.ui.dialog.DownloadDialog
import com.`fun`.auction.ui.dialog.PolicyDialog
import com.`fun`.auction.ui.dialog.UpdateDialog
import com.`fun`.auction.util.MyToast
import com.`fun`.auction.widget.TabItem
import com.blankj.utilcode.util.AppUtils

import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MainActivity : BaseActivity() {


    var lastTime: Long? = null
    var dialog: AlertDialog? = null

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        fun logout(mContext: Context) {
            val intent = Intent(mContext, MainActivity::class.java)
            intent.putExtra("logout", true)
            mContext.startActivity(intent)
        }
    }


    override fun getLayoutId() = R.layout.activity_main


    override fun init() {
        EventBus.getDefault().register(this)
        val fragments = mutableListOf<Fragment>()
        fragments.add(HomeFragment.newInstance())
        fragments.add(ShopFragment.newInstance())
        fragments.add(MeFragment.newInstance())
        viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager, 1) {
            override fun getCount(): Int {
                return fragments.size
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }
        }

        val build = navigation.custom()
            .addItem(newItem(R.drawable.icon_home_, R.drawable.icon_home, "首页"))
            .addItem(newItem(R.drawable.icon_pai, R.drawable.icon_pai_check, "拍拍"))
            .addItem(newItem(R.drawable.icon_me_, R.drawable.icon_me, "我的"))
            .build()
        build.setupWithViewPager(viewpager)
        viewpager.offscreenPageLimit = 2

        requestPermission()

        versionCheck()
    }


    private fun newItem(res: Int, checkRes: Int, text: String): BaseTabItem {
        val item = TabItem(mContext)
        item.initialize(res, checkRes, text)
        item.setTextCheckedColor(Color.parseColor("#45FFDF"))
        item.setTextDefaultColor(Color.parseColor("#ffffff"))
        return item;
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val logout = intent?.getBooleanExtra("logout", false) ?: false
        if (logout) {
            PrefercencesManager.getInstance()?.logout()
            start(mContext, PhoneLoginActivity::class.java)
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    fun loginEvent(event: LoginEvent) {
        logout(mContext)
    }


    private fun requestPermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.requestEachCombined(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .subscribe { permission: Permission ->  // will emit 1 Permission object
                if (permission.granted) {
                    // All permissions are granted !
                    showPolicyDialog()

                } else if (permission.shouldShowRequestPermissionRationale) {
                    // At least one denied permission without ask never again
//                    val tip = "该应用需要用到存储读写权限，请开启相关权限"
//                    dialog = AlertDialog.Builder(mContext)
//                        .setMessage(tip)
//                        .setPositiveButton("确定") { _, _ ->
//                            requestPermission()
//                        }
//                        .setNegativeButton("取消") { _, _ ->
//                            dialog?.dismiss()
//                        }
//                        .setCancelable(false)
//                        .create()
//                    dialog?.show()
                } else {
                    // At least one denied permission with ask never again
                    // Need to go to the settings
//                    val tip = "该应用需要用到存储读写权限，请前往设置中心开启权限"
//                    dialog = AlertDialog.Builder(mContext)
//                        .setMessage(tip)
//                        .setPositiveButton("确定") { _, _ ->
//                            goSetting()
//                            finish()
//                        }
//                        .setNegativeButton("取消") { _, _ ->
//                            dialog?.dismiss()
//                        }
//                        .setCancelable(false)
//                        .create()
//                    dialog?.show()


                }
            }
    }

    private fun showPolicyDialog() {
        val policy = PrefercencesManager.getInstance()?.getPolicy() ?: false
        if (!policy) {
            val dialog = PolicyDialog(mContext)
            dialog.show()
            dialog.dialogCallback = object : DialogCallback {
                override fun onConfirm() {
                    PrefercencesManager.getInstance()?.setPolicy()
                }
            }
        }
    }

    private fun goSetting() {
        val intent = Intent(Settings.ACTION_SETTINGS)
        startActivity(intent)
    }


    fun versionCheck() {
        val map = mapOf(
            "ver" to AppUtils.getAppVersionCode().toString(), //20210603
            "ver_sp" to BuildConfig.VERSION_SP,     //xiaomi
            "ver_up" to AppUtils.getAppVersionCode().toString()      //20210603
        )
        HttpManager.api?.versionCheck(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<VersionCheck>() {
                override fun onSuccess(code: Int, msg: String, data: VersionCheck?) {
                    data?.let {
                        if (it.is_up == 1 || it.is_up == 2) {
                            val updateDialog = UpdateDialog(mContext, data.des, it.is_up == 2)
                            updateDialog.show()
                            updateDialog.callback = object : DialogCallback {
                                override fun onConfirm() {
                                    val downloadDialog = DownloadDialog(this@MainActivity)
                                    downloadDialog.setDownload(data.up_url, "funAuction.apk", it.is_up == 2)
                                    downloadDialog.show()
                                }
                            }
                        }
                    }
                }
            })
    }


    override fun onBackPressed() {
        var last = lastTime
        if (last != null && (System.currentTimeMillis() - last) < 2000) {
            super.onBackPressed()
        } else {
            MyToast.showBottom("再按一下退出")
        }
        lastTime = System.currentTimeMillis()
    }

}