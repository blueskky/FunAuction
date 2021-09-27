package com.`fun`.auction.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.model.Address
import com.`fun`.auction.model.RefreshAddressEvent
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import com.baidu.mapapi.search.core.PoiInfo
import com.blankj.utilcode.util.RegexUtils
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.activity_edit_adress.*
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus

class EditAdressActivity : BaseActivity(), View.OnClickListener {


    var address: Address? = null

    companion object {
        fun start(context: Context?, item: Address?) {
            val intent = Intent(context, EditAdressActivity::class.java)
            intent.putExtra("data", item)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_edit_adress

    override fun init() {
        initTitle(iv_back, tv_title, "添加地址")
        tv_location.setOnClickListener(this)
        btnAdd.setOnClickListener(this)

        address = intent.getSerializableExtra("data") as Address?
        address?.let {
            et_name.setText(it.name)
            et_phone.setText(it.phone)
            et_region.setText(it.region)
            et_adress.setText(it.detail)
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_location -> requestPermission()
            R.id.btnAdd -> {
                if (address != null) {
                    editAddress(address)
                } else {
                    addAdress()
                }
            }
        }
    }


    private fun requestPermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.requestEachCombined(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).subscribe { permission: Permission ->  // will emit 1 Permission object
            when {
                permission.granted -> {
                    // All permissions are granted !
                    LocationActivity.startForResult(this@EditAdressActivity, 200)
                }
                permission.shouldShowRequestPermissionRationale -> {
                    // At least one denied permission without ask never again
                    val tip = "需要用到定位权限，请开启相关权限"
                    AlertDialog.Builder(mContext)
                        .setMessage(tip)
                        .setPositiveButton("确定") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("取消") { _, _ ->

                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
                else -> {
                    // At least one denied permission with ask never again
                    // Need to go to the settings
                    val tip = "需要用到定位权限，请前往设置中心开启相关权限"
                    AlertDialog.Builder(mContext)
                        .setMessage(tip)
                        .setPositiveButton("确定") { _, _ ->
                            goSetting()
                        }
                        .setNegativeButton("取消") { _, _ ->

                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
            }
        }
    }

    private fun goSetting() {
        val intent = Intent(Settings.ACTION_SETTINGS)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            val poiInfo = data?.getParcelableExtra<Parcelable>("location") as PoiInfo?
            var address = poiInfo?.getAddress()

            val province = poiInfo?.getProvince()
            val city = poiInfo?.getCity() ?: ""
            val area = poiInfo?.getArea()

            if (address != null && !address?.contains(city)) {
                address = city + address
            }
            et_region.setText(address)
        }
    }


    private fun addAdress() {
        val name = et_name.text.toString().trim()
        val phone = et_phone.text.toString().trim()
        val region = et_region.text.toString().trim()
        val adress = et_adress.text.toString().trim()

        if (TextUtils.isEmpty(name)) {
            MyToast.show("请填写姓名")
            return
        }
        if (TextUtils.isEmpty(name) || !RegexUtils.isMobileSimple(phone)) {
            MyToast.show("请填写正确的手机号")
            return
        }
        if (TextUtils.isEmpty(region) || TextUtils.isEmpty(adress)) {
            MyToast.show("请填写地址信息")
            return
        }

        val map = mutableMapOf<String, String>()
        map["name"] = name
        map["phone"] = phone
        map["region"] = region
        map["detail"] = adress

        HttpManager.api?.addAddress(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    EventBus.getDefault().post(RefreshAddressEvent())
                    finish()
                }
            })
    }


    private fun editAddress(address: Address?) {
        val name = et_name.text.toString().trim()
        val phone = et_phone.text.toString().trim()
        val region = et_region.text.toString().trim()
        val adress = et_adress.text.toString().trim()

        if (TextUtils.isEmpty(name)) {
            MyToast.show("请填写姓名")
            return
        }
        if (TextUtils.isEmpty(name) || !RegexUtils.isMobileSimple(phone)) {
            MyToast.show("请填写正确的手机号")
            return
        }
        if (TextUtils.isEmpty(region) || TextUtils.isEmpty(adress)) {
            MyToast.show("请填写地址信息")
            return
        }


        val map = mutableMapOf<String, String?>()
        map["name"] = name
        map["phone"] = phone
        map["region"] = region
        map["detail"] = adress
        map["address_id"] = address?.address_id?.toString()

        HttpManager.api?.editAddress(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    EventBus.getDefault().post(RefreshAddressEvent())
                    finish()
                }
            })
    }

}