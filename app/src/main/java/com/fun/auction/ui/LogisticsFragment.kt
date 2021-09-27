package com.`fun`.auction.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.LogisticsItem
import com.`fun`.auction.model.LogisticsModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.LogisticsListAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_logistics.*


class LogisticsFragment : BaseFragment() {

    var last_id: Int? = null
    var logisticsAdapter: LogisticsListAdapter? = null

    companion object {
        fun newInstance(position: Int): Fragment {
            val fragment = LogisticsFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_logistics

    override fun init() {
        recyclerview.layoutManager = LinearLayoutManager(mContext)
        logisticsAdapter = LogisticsListAdapter()
        recyclerview.adapter = logisticsAdapter

        logisticsAdapter?.addChildClickViewIds(R.id.tv_logistics)
        logisticsAdapter?.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = logisticsAdapter?.getItem(position)
                if (item != null) {
                    LogisticsActivity.start(mContext, item.order_id)
                }
            }
        })

        logisticsAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = logisticsAdapter?.getItem(position)
                BoxOrderActivity.start(mContext, item?.box_id)
            }
        })

        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                last_id == null
                initData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                initData()
            }
        })
    }

    override fun initData() {
        val position = arguments?.getInt("position")
        var status = if (position == 0) 1 else 3

        HttpManager.api?.logisticsList(status, last_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<LogisticsModel>() {
                override fun onSuccess(code: Int, msg: String, data: LogisticsModel?) {
                    val list = data?.list
                    if (list != null) {
                        setData(list)
                    }
                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    logisticsAdapter?.setEmptyView(R.layout.empty_view)
                }


            })
    }

    private fun setData(list: MutableList<LogisticsItem>) {
        if (last_id == null) {
            logisticsAdapter?.setNewInstance(list)
        } else {
            logisticsAdapter?.addData(list)
        }
    }
}