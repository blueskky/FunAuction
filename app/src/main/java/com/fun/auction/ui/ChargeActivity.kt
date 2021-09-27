package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.View.VISIBLE
import android.widget.CompoundButton
import androidx.recyclerview.widget.GridLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.*
import com.`fun`.auction.model.Event.Companion.CHARGE_SUCCESS
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.ChargeAdapter
import com.`fun`.auction.util.MyToast
import com.`fun`.auction.util.PayHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_charge.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ChargeActivity : BaseActivity(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    var chargeAdapter: ChargeAdapter? = null

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, ChargeActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_charge

    override fun init() {
        EventBus.getDefault().register(this)
        iv_back.setOnClickListener(this)
        tv_log.setOnClickListener(this)
        tv_pay.setOnClickListener(this)


        recyclerview.layoutManager = GridLayoutManager(mContext, 4)
        chargeAdapter = ChargeAdapter()
        recyclerview.adapter = chargeAdapter


        chargeAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                chargeAdapter?.setSelect(position)
                val item = chargeAdapter?.getItem(position)
                if (item != null) {
                    setPayTxt(item)
                }
            }
        })

        cb_balance.setOnCheckedChangeListener(this)
        cb_ali.setOnCheckedChangeListener(this)
        cb_wechat.setOnCheckedChangeListener(this)
    }

    override fun initData() {
        HttpManager.api?.chargeList()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<ChargeModel>() {
                override fun onSuccess(code: Int, msg: String, data: ChargeModel?) {
                    if (data != null) {
                        val list = data.list
                        chargeAdapter?.setNewInstance(list)
                        if (list != null && list.size > 2) {
                            setPayTxt(list[2])
                        }
                        var map = mutableMapOf<String, Int>()
                        for (way in data.pay_way) {
                            map[way.type] = way.status
                        }
                        setPayWay(map)
                    }
                }
            })
    }

    private fun setPayTxt(item: ChargeItem) {
        tv_amount.text = String.format("¥ %.2f", item.amount)
        if (item.experience > 0) {
            tv_point.text = "积分+${item.experience}"
        } else {
            tv_point.text = ""
        }
    }

    private fun setPayWay(map: MutableMap<String, Int>) {
        val surplus = map["surplus"]
        if (surplus == 1) {
            ll_blance.visibility = VISIBLE
        }
        val aliPay = map["aliPay"]
        if (aliPay == 1) {
            ll_ali.visibility = VISIBLE
        }
        val weChat = map["weChat"]
        if (weChat == 1) {
            ll_wechat.visibility = VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> onBackPressed()
            R.id.tv_log -> ChargeLogActivity.start(mContext)
            R.id.tv_pay -> charge()
        }
    }

    private fun charge() {
        val selectItem = chargeAdapter?.getSelectItem()
        if (selectItem != null) {
            if (cb_balance.isChecked) {
                blancePay(selectItem)
            } else if (cb_wechat.isChecked) {
                wechatPay(selectItem)
            } else if (cb_ali.isChecked) {
                aliPay(selectItem)
            }
        }
    }

    private fun blancePay(item: ChargeItem) {
        val map = mapOf("unlock_id" to item.unlock_id)
        HttpManager.api?.balancePay(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(Event(CHARGE_SUCCESS))
                }
            })
    }

    private fun wechatPay(item: ChargeItem) {
        val map = mapOf("unlock_id" to item.unlock_id)
        HttpManager.api?.wechatPay(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<PayConfig>() {
                override fun onSuccess(code: Int, msg: String, data: PayConfig?) {
                    data?.let {
                        PayHelper.wxPay(mContext, data)
                    }
                }
            })
    }


    private fun aliPay(item: ChargeItem) {
        val map = mapOf("unlock_id" to item.unlock_id)
        HttpManager.api?.aliPay(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<String>() {
                override fun onSuccess(code: Int, msg: String, data: String?) {
                    data?.let {
                        PayHelper.aliPay(this@ChargeActivity, data)
                    }
                }
            })
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        buttonView?.let {
            if (isChecked && it.isPressed) {
                cb_balance.isChecked = false
                cb_ali.isChecked = false
                cb_wechat.isChecked = false

                buttonView.isChecked = true
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    fun payResult(result: PayResult) {
        MyToast.show(result.msg)
        if (result.result == 0) {
            EventBus.getDefault().post(Event(CHARGE_SUCCESS))
        }
    }

}