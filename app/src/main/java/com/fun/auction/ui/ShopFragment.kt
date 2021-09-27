package com.`fun`.auction.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.MyApplication
import com.`fun`.auction.PrefercencesManager
import com.`fun`.auction.R
import com.`fun`.auction.model.*
import com.`fun`.auction.model.Event.Companion.PEEK_SUCCESS
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.presenter.PatPresenter
import com.`fun`.auction.ui.adapter.ShopAdapter
import com.`fun`.auction.ui.dialog.*
import com.`fun`.auction.util.MyToast
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_shop.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ShopFragment : BaseFragment(), View.OnClickListener, PatPresenter.PatView {


    companion object {
        fun newInstance(): Fragment {
            return ShopFragment()
        }
    }

    var shopAdapter: ShopAdapter? = null
    var order: Int = 0
    var page: Int = 1
    var patPresenter: PatPresenter? = null

    override fun getLayoutId() = R.layout.fragment_shop

    override fun init() {
        EventBus.getDefault().register(this)
        wish_bottle.setOnClickListener(this)
        tv_rule.setOnClickListener(this)
        tv_free.setOnClickListener(this)

        val list = listOf("默认排序", "市场价↓", "市场价↑")//高到低  低到高
        spinner.attachDataSource(list)
        spinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            order = position
            page = 1
            getGoodsList()
        }

        recyclerview.layoutManager = LinearLayoutManager(mContext)
        shopAdapter = ShopAdapter()
        recyclerview.adapter = shopAdapter


        shopAdapter?.setOnItemClickListener { _, _, position ->
            val item = shopAdapter?.getItem(position)
            GoodsDetailActivity.start(mContext, item?.product_id)
        }

        shopAdapter?.addChildClickViewIds(R.id.tvPat, R.id.rl_peek)
        shopAdapter?.setOnItemChildClickListener(OnItemChildClickListener { _, view, position ->
            val item = shopAdapter?.getItem(position)
            when (view.id) {
                R.id.tvPat -> patDialog(item) //直接拍
                R.id.rl_peek -> {
                    val currentPrice = item?.current_price
                    if (currentPrice == null) {
                        //偷瞄
                        patPresenter?.peek(item?.product_id, item?.goods_name)
                    } else {
                        MyToast.show("60s内只能偷瞄一次")
                    }
                }
            }
        })

        refresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                getGoodsList()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getGoodsList()
            }
        })

        patPresenter = PatPresenter(mContext, this, this)
    }


    override fun onResume() {
        super.onResume()
        getGoodsList()
    }

    fun getGoodsList() {
        HttpManager.api?.storePage(order, page)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<StoreData>() {
                override fun onSuccess(code: Int, msg: String, data: StoreData?) {
                    val list = data?.list
                    if (list!=null) {
                        if (page == 1) {
                            shopAdapter?.setNewInstance(list)
                        } else {
                            shopAdapter?.addData(list)
                        }
                    }
                    page = data?.page ?: 1

                    if (!list.isNullOrEmpty()) {
                        for (it in list) {
                            if (it.store_status == 1) {
                                val patSuccessDialog = PatSuccessDialog(mContext)
                                patSuccessDialog.show()
                                break
                            }
                        }


                    }

                    tv_free.text = "剩余免费偷瞄次数 ${data?.peek}"
                }

                override fun onFinish() {
                    refresh.finishRefresh()
                    refresh.finishLoadMore()
                }
            })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.wish_bottle -> WishListActvity.start(mContext)
            R.id.tv_rule -> {
                RuleActivity.start(mContext, -1)
            }
            R.id.tv_free -> {
                val dialog = InvitationDialog(mContext)
                dialog.show()
            }
        }
    }


    private fun patDialog(item: Product?) {
        val dialog = PatConfirmDialog(mContext, item?.goods_name)
        dialog.show()
        dialog.callback = object : DialogCallback {
            override fun onConfirm() {
                patPresenter?.pat(item?.product_id)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    fun onPeekChange(event: PeekChangeEvent) {
        shopAdapter?.notifyDataSetChanged()
    }

    @Subscribe
    fun onPeekChange(event: Event) {
        if (event.type == PEEK_SUCCESS) {
            HttpManager.api?.storePage(order, 1)
                ?.compose(SchedulersUtils.applySchedulers())
                ?.compose(this.bindToLifecycle())
                ?.subscribe(object : SimpleObserver<StoreData>() {
                    override fun onSuccess(code: Int, msg: String, data: StoreData?) {
                        tv_free.text = "剩余免费偷瞄次数 ${data?.peek}"
                    }
                })
        }
    }

    override fun onPrePatSuccess(productId: Int?) {
        val data = shopAdapter?.data
        data?.let {
            for (i in it.indices) {
                val product = it[i]
                if (product.product_id == productId) {
                    product.auction_time = System.currentTimeMillis() / 1000
                    shopAdapter?.notifyItemChanged(i)
                    break
                }
            }
        }

    }

    override fun onPeekSucc(productId: Int?) {
        val data = shopAdapter?.data
        data?.let {
            for (i in it.indices) {
                val product = it[i]
                if (product.product_id == productId) {
                    product.update_time = System.currentTimeMillis() / 1000
                    shopAdapter?.notifyItemChanged(i)
                    break
                }
            }
        }
    }


}