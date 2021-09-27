package com.`fun`.auction.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.Event
import com.`fun`.auction.model.PatEnd
import com.`fun`.auction.model.PatEndModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.PatRecordEndAdapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_pat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PatRecordFragment2 : BaseFragment() {

    companion object {
        fun instance(position: Int): Fragment {
            val fragment = PatRecordFragment2()
            val bundle = Bundle()
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }

    var patRecordAdapter: PatRecordEndAdapter? = null
    var last_id: Int? = null
    val type by lazy {
        arguments?.getInt("position")
    }

    override fun getLayoutId() = R.layout.fragment_pat


    override fun init() {
        EventBus.getDefault().register(this)
        recyclerview.layoutManager = LinearLayoutManager(mContext)
        patRecordAdapter = PatRecordEndAdapter(type)
        recyclerview.adapter = patRecordAdapter


        patRecordAdapter?.addChildClickViewIds(R.id.btn_receive)
        patRecordAdapter?.addChildClickViewIds(R.id.tv_order_detail)
        patRecordAdapter?.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = patRecordAdapter?.getItem(position)
                when (view?.id) {
                    R.id.tv_order_detail -> {
                        BoxOrderActivity.start(mContext, item?.box_id)
                    }
                    R.id.btn_receive -> {
                        BoxOrderActivity.start(mContext, item?.box_id)
                    }
                }
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
        var status = if (type == 1) 1 else 2
        HttpManager.api?.patLog(status, last_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<PatEndModel>() {
                override fun onSuccess(code: Int, msg: String, data: PatEndModel?) {
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

    private fun setData(list: MutableList<PatEnd>) {
        if (last_id == null) {
            patRecordAdapter?.setNewInstance(list)
        } else {
            patRecordAdapter?.addData(list)
        }
    }


    @Subscribe
    fun refresh(event: Event){
        if(event.type==Event.BOX_UPDATE){
            last_id = null
            initData()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}