package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.formatTime
import com.`fun`.auction.line
import com.`fun`.auction.model.*
import com.`fun`.auction.model.Event.Companion.BOX_UPDATE
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.MyToast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_order.tv_order_no
import kotlinx.android.synthetic.main.common_title.*
import kotlinx.android.synthetic.main.goods_detail.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat

class BoxOrderActivity : BaseActivity() {

    val limitTime = 24 * 60 * 60 * 1000
    val boxId by lazy {
        intent.getIntExtra("boxId", 0)
    }
    var orderId: String? = null
    var selectAddress: Address? = null

    companion object {
        fun start(context: Context?, boxId: Int?) {
            val intent = Intent(context, BoxOrderActivity::class.java)
            intent.putExtra("boxId", boxId)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_order

    override fun init() {
        initTitle(iv_back, tv_title, "我的订单")

        tv_logistics.setOnClickListener {
            orderId?.let {
                LogisticsActivity.start(mContext, it)
            }
        }
    }

    override fun initData() {
        HttpManager.api?.recordDetail(boxId)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<RecordDetail>() {
                override fun onSuccess(code: Int, msg: String, data: RecordDetail?) {
                    orderId = data?.order_info?.order_id

                    Glide.with(mContext).load(data?.goods_image).into(iv_img)
                    tv_name.text = data?.goods_name
                    tv_price.text = "成交价¥ ${data?.total_price}"
                    tv_market.text = "市场价¥ ${data?.goods_price}"
                    tv_market.line()
                    tv_time.text = fTime(data?.create_time)

                    if (data?.order_address != null) {
                        tv_address.text = data?.order_address?.region + data?.order_address?.detail
                    }
                    tv_pay.text = "¥ ${data?.total_price}"


                    val orderInfo = data?.order_info
                    if (orderInfo?.order_status == 20) {
                        tv_status.text = "已取消"
                    } else if (orderInfo?.pay_status == 10) {
                        tv_status.text = "待支付"
                    } else if (orderInfo?.pay_status == 20 && orderInfo?.delivery_status == 10) {
                        tv_status.text = "待发货"
                    } else if (orderInfo?.delivery_status == 20 && orderInfo?.receipt_status == 10) {
                        tv_status.text = "待收货"
                    } else if (orderInfo?.order_status == 30) {
                        tv_status.text = "已完成"
                    } else {
                        tv_status.text = "备货中"
                    }

                    if (orderInfo?.delivery_status == 20) {
                        rl_logistics.visibility = View.VISIBLE
                        line.visibility = View.VISIBLE
                    } else {
                        rl_logistics.visibility = View.GONE
                        line.visibility = View.GONE
                    }


                    if (data?.box_status == 0) {
                        ll_time_down.visibility = View.VISIBLE
                        tv_order.text = "盒子编号"
                        tv_order_no.text = data?.box_no
                        tv_pay.isEnabled = true
                    } else {
                        ll_time_down.visibility = View.GONE
                        tv_order.text = "订单编号"
                        tv_order_no.text = data?.order_info?.order_no
                        tv_pay.isEnabled = false
                    }



                    when (data?.box_status) {
                        0 -> {
                            btn_pay.text = "确认支付"
                            btn_pay.setBackgroundResource(R.drawable.btn_pay)
                            tv_address.text = "选择地址 >"
                            val l = limitTime - (System.currentTimeMillis() - data.create_time * 1000)
                            count_view.start(l)

                            tv_address.setOnClickListener {
                                selectAddress()
                            }
                            btn_pay.setOnClickListener {
                                if (selectAddress == null) {
                                    selectAddress()
                                    MyToast.show("请选择收货地址")
                                    return@setOnClickListener
                                }
                                enity(data.box_id, selectAddress)

                            }
                            getDefaultAddress()
                        }
                        3 -> {
                            btn_pay.text = "已过期"
                            btn_pay.setBackgroundColor(Color.parseColor("#9A9A9A"))
                        }
                        else -> {
                            btn_pay.text = "已结束"
                            btn_pay.setBackgroundColor(Color.parseColor("#9A9A9A"))
                        }
                    }
                }
            })
    }

    private fun fTime(createTime: Long?): String? {
        if (createTime != null) {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val format = simpleDateFormat.format(createTime * 1000)
            return format
        }
        return ""
    }

    private fun selectAddress() {
        AddressActivitiy.startResult(this@BoxOrderActivity, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val address = data?.getSerializableExtra("data") as Address?
            if (address != null) {
                selectAddress = address
                tv_address.text = "${address.region} ${address.detail}>"
            }
        }
    }

    private fun getDefaultAddress() {
        HttpManager.api?.addressList()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<AddressModel>() {
                override fun onSuccess(code: Int, msg: String, data: AddressModel?) {
                    val list = data?.list
                    if (list!=null) {
                        for (item in list) {
                            if (item.address_id == data.default_id) {
                                selectAddress = item
                                tv_address.text = "${item.region} ${item.detail}>"
                            }
                        }
                    }
                }
            })
    }


    private fun enity(boxId: Int?, address: Address?) {
        val map = mapOf("box_id" to boxId, "address_id" to address?.address_id)
        HttpManager.api?.enity(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    initData()
                    EventBus.getDefault().post(Event(BOX_UPDATE))
                }
            })
    }
}