package com.`fun`.auction.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.Event
import com.`fun`.auction.model.PatModel
import com.`fun`.auction.model.PatRecord
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.PatRecordAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_pat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PatRecordFragment : BaseFragment() {

    companion object {
        fun instance(): Fragment {
            val fragment = PatRecordFragment()
            return fragment
        }
    }

    var patRecordAdapter: PatRecordAdapter? = null
    var last_id: Int? = null

    override fun getLayoutId() = R.layout.fragment_pat


    override fun init() {
        recyclerview.layoutManager = LinearLayoutManager(mContext)
        patRecordAdapter = PatRecordAdapter()
        recyclerview.adapter = patRecordAdapter


        patRecordAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = patRecordAdapter?.getItem(position)
                GoodsDetailActivity.start(mContext, item?.product_id)
            }
        })

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
        HttpManager.api?.patIng(last_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<PatModel>() {
                override fun onSuccess(code: Int, msg: String, data: PatModel?) {
                    if (data?.list != null) {
                        setData(data.list)
                    }
                    last_id = data?.last_id
                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    patRecordAdapter?.setEmptyView(R.layout.empty_view)
                }
            })
    }

    private fun setData(list: MutableList<PatRecord>) {
        if (last_id == null) {
            patRecordAdapter?.setNewInstance(list)
        } else {
            patRecordAdapter?.addData(list)
        }
    }


}