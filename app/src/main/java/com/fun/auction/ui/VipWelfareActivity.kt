package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.AddressModel
import com.`fun`.auction.model.WelFare
import com.`fun`.auction.model.WelFareModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.VipAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.common_title.*
import kotlinx.android.synthetic.main.vip_welfare.*

class VipWelfareActivity : BaseActivity() {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, VipWelfareActivity::class.java))
        }
    }

    var page = 1
    var vipAdapter: VipAdapter? = null

    override fun getLayoutId() = R.layout.vip_welfare

    override fun init() {
        initTitle(iv_back, tv_title, "会员福利")

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        vipAdapter = VipAdapter()
        recyclerView.adapter = vipAdapter

        vipAdapter?.addChildClickViewIds(R.id.btn_receive)
        vipAdapter?.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = vipAdapter?.getItem(position)
                if (item != null) {
                    exChange(item)
                }
            }
        })

        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                initData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                initData()
            }
        })

    }

    private fun exChange(item: WelFare) {
        val map = mapOf("prize_id" to item.prize_id)
        HttpManager.api?.getPrize(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    page = 1
                    initData()
                }
            })
    }


    override fun initData() {
        HttpManager.api?.prizeList(page)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<WelFareModel>() {
                override fun onSuccess(code: Int, msg: String, data: WelFareModel?) {
                    val list = data?.list
                    if (list != null) {
                        if (page == 1) {
                            vipAdapter?.setNewInstance(list)
                        } else {
                            vipAdapter?.addData(list)
                        }
                    }
                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMore()
                    vipAdapter?.setEmptyView(R.layout.empty_view)
                }
            })
    }
}