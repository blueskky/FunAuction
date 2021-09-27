package com.`fun`.auction.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.Info
import com.`fun`.auction.model.LogisticeModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.LogisticsAdapter
import com.`fun`.auction.util.MyToast
import kotlinx.android.synthetic.main.activity_charge.iv_back
import kotlinx.android.synthetic.main.activity_charge.tv_title
import kotlinx.android.synthetic.main.activity_logistics2.*
import java.util.*

class LogisticsActivity : BaseActivity(), View.OnClickListener {

    val orderId by lazy {
        intent.getStringExtra("order_id")
    }
    var logisticsAdapter: LogisticsAdapter? = null
    var logisticeData: LogisticeModel? = null

    companion object {
        fun start(context: Context?, order_id: String) {
            val intent = Intent(context, LogisticsActivity::class.java)
            intent.putExtra("order_id", order_id)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_logistics2

    override fun init() {
        initTitle(iv_back, tv_title, "物流详情")

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        logisticsAdapter = LogisticsAdapter()
        recyclerView.adapter = logisticsAdapter

        refreshLayout.setOnRefreshListener {
            initData()
        }
        tv_copy.setOnClickListener(this)
    }

    override fun initData() {
        HttpManager.api?.logisticsInfo(orderId)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<LogisticeModel>() {
                override fun onSuccess(code: Int, msg: String, data: LogisticeModel?) {
                    logisticeData = data
                    if (data != null) {
                        if (data.no_info != null) {
                            tv_order.text = "快递单号：${data.no_info.express_no}"
                            tv_copy.visibility = View.VISIBLE

                            tv_des.text = "未查询到订单信息~ 点击复制快递单号查询"
                            tv_des.visibility = View.VISIBLE


                        } else {
                            tv_order.text = "快递单号：${data.nu}"
                            tv_copy.visibility = View.VISIBLE

                            val infos = data.info
                            if (!infos.isNullOrEmpty()) {
                                infos.sort()
                                val status = when (data.state) {
                                    1 -> "正常"
                                    2 -> "派送中"
                                    3 -> "已签收"
                                    4 -> "退回"
                                    5 -> "其他问题"
                                    else -> ""
                                }

                                infos[0].content = "${status}\n\n${infos[0].content}"

                                logisticsAdapter?.setNewInstance(infos)
                            } else {
                                tv_des.text = "未查询到订单信息~ 点击复制快递单号查询"
                                tv_des.visibility = View.VISIBLE
                            }
                        }

                    } else {
                        tv_order.text = "耐心等待，还未发货~"
                    }
                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                }
            })
    }

    override fun onClick(v: View?) {
        logisticeData?.let {
            val clipboardManager = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", logisticeData?.nu)
            clipboardManager.setPrimaryClip(mClipData)
            MyToast.show("已复制")
        }
    }
}