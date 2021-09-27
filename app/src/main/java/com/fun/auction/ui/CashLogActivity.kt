package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.CashLog
import com.`fun`.auction.model.CashModel
import com.`fun`.auction.model.UserModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.CashLogAdapter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_cash_log.*
import kotlinx.android.synthetic.main.common_title.*

class CashLogActivity : BaseActivity() {

    var last_id: Int? = null
    var cashLogAdapter: CashLogAdapter? = null

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, CashLogActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_cash_log

    override fun init() {
        initTitle(iv_back, tv_title, "提现记录")

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        cashLogAdapter = CashLogAdapter()
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
        HttpManager.api?.cashLog(-1, last_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<CashModel>() {
                override fun onSuccess(code: Int, msg: String, data: CashModel?) {
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

    private fun setData(list: MutableList<CashLog>) {
        if (last_id == null) {
            cashLogAdapter?.setNewInstance(list)
        } else {
            cashLogAdapter?.addData(list)
        }
    }
}