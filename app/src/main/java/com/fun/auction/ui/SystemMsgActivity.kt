package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.MessageModel
import com.`fun`.auction.model.SystemMsg
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.MessageAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_system_msg.*
import kotlinx.android.synthetic.main.common_title.*

class SystemMsgActivity : BaseActivity() {

    var last_id: Int? = null
    var messageAdapter: MessageAdapter? = null

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, SystemMsgActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_system_msg

    override fun init() {
        initTitle(iv_back, tv_title, "消息中心")

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        messageAdapter = MessageAdapter()
        recyclerView.adapter = messageAdapter

        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                last_id = null
                initData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                initData()
            }
        })

        messageAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = messageAdapter?.getItem(position)
                MessageDetailActivity.start(mContext, item?.id)
                item?.status = 1
                messageAdapter?.notifyDataSetChanged()
            }
        })
    }

    override fun initData() {
        HttpManager.api?.message(0, last_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<MessageModel>() {
                override fun onSuccess(code: Int, msg: String, data: MessageModel?) {
                    if (data?.list != null) {
                        setData(data.list)
                    }
                    last_id = data?.last_id
                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    messageAdapter?.setEmptyView(R.layout.empty_view)
                }
            })
    }

    private fun setData(list: MutableList<SystemMsg>) {
        if (last_id == null) {
            messageAdapter?.setNewInstance(list)
        } else {
            messageAdapter?.addData(list)
        }
    }
}