package com.`fun`.auction.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.SellItem
import com.`fun`.auction.model.SellModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.SellAdapter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : BaseFragment() {

    var last_id: Int? = null
    var adapter: SellAdapter? = null

    val position by lazy {
        arguments?.getInt("position", 0)
    }

    companion object {
        fun newInstance(position: Int): Fragment {
            val sellFragment = SellFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            sellFragment.arguments = bundle
            return sellFragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_sell

    override fun init() {
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        adapter = SellAdapter(position)
        recyclerView.adapter = adapter
//        skeleton = Skeleton.bind(recyclerView).adapter(adapter).load(R.layout.sell_item_skeleton).show()

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

        var status = if (position == 0) 1 else 2
        HttpManager.api?.sellList(status, last_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<SellModel>() {
                override fun onSuccess(code: Int, msg: String, data: SellModel?) {
                    if (data?.list != null) {
                        setData(data.list)
                    }
                    last_id = data?.last_id
                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()

                    adapter?.setEmptyView(R.layout.empty_view)
                }
            })
    }


    private fun setData(list: MutableList<SellItem>) {
        if (last_id == null) {
            adapter?.setNewInstance(list)
        } else {
            adapter?.addData(list)
        }
    }
}