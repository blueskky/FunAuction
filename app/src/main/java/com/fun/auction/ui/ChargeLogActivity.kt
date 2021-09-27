package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.CashLog
import com.`fun`.auction.model.CashModel
import com.`fun`.auction.model.ChargeLog
import com.`fun`.auction.model.ChargeLogModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.CashLogAdapter
import com.`fun`.auction.ui.adapter.ChargeLogAdapter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_charge_log.*
import kotlinx.android.synthetic.main.common_title.*

class ChargeLogActivity : BaseActivity() {


    var last_id: Int ?=null
    var cashLogAdapter: ChargeLogAdapter? = null

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, ChargeLogActivity::class.java))
        }
    }

    override fun getLayoutId()=R.layout.activity_charge_log

    override fun init() {
        initTitle(iv_back, tv_title, "充值记录")

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        cashLogAdapter = ChargeLogAdapter()
        recyclerView.adapter = cashLogAdapter

        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                last_id = null
                initData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                initData()
            }
        })

    }


    override fun initData() {
        HttpManager.api?.chargeLog(last_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<ChargeLogModel>() {
                override fun onSuccess(code: Int, msg: String, data: ChargeLogModel?) {
                    if (data?.list != null) {
                        setData(data.list)
                    }
                    last_id = data?.last_id
                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                }
            })
    }

    private fun setData(list: MutableList<ChargeLog>) {
        if (last_id == null) {
            cashLogAdapter?.setNewInstance(list)
        } else {
            cashLogAdapter?.addData(list)
        }
    }
}