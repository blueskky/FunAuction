package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.`fun`.auction.MyApplication
import com.`fun`.auction.R
import com.`fun`.auction.formatTime
import com.`fun`.auction.line
import com.`fun`.auction.model.*
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.presenter.PatPresenter
import com.`fun`.auction.ui.adapter.MyBannerAdapter
import com.`fun`.auction.ui.dialog.*
import com.`fun`.auction.util.MyToast
import com.`fun`.auction.widget.NumIndicator
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.activity_goods_detail.*
import kotlinx.android.synthetic.main.goods_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class GoodsDetailActivity : BaseActivity(), View.OnClickListener, PatPresenter.PatView {

    companion object {
        fun start(context: Context?, productId: Int?) {
            val intent = Intent(context, GoodsDetailActivity::class.java)
            intent.putExtra("data", productId)
            context?.startActivity(intent)
        }
    }

    val productId by lazy {
        intent.getIntExtra("data", 0)
    }
    var product: GoodsDetail? = null
    val limitTime = 24 * 60 * 60 * 1000
    var patPresenter: PatPresenter? = null

    override fun getLayoutId() = R.layout.activity_goods_detail

    override fun init() {
        EventBus.getDefault().register(this)
        initTitle(iv_back, tv_title, "商品详情")
        tv_pat.setOnClickListener(this)
        tv_peek_watch_.setOnClickListener(this)
        tv_share.setOnClickListener(this)

        refreshLayout.setOnRefreshListener {
            initData()
        }

        patPresenter = PatPresenter(mContext, this, activityLife = this)
    }


    override fun initData() {
        HttpManager.api?.goodsdDtail(productId)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<GoodsDetail>() {
                override fun onSuccess(code: Int, msg: String, data: GoodsDetail?) {
                    product = data
                    data?.let {
                        onPeekChange(PeekChangeEvent())

                        setBanner(it.images)
                        tv_cut_time.text = formatTime(it.update_time)

                        val time = it.auction_time * 1000
                        if (time > 0) {
                            val delay = limitTime - (System.currentTimeMillis() - time)
                            if (delay >= 0) {
                                //已拍下
                                ll_time.visibility = View.VISIBLE
                                count_view.start(delay)
                            } else {
                                //已结束
                                ll_time.visibility = View.GONE
                                ll_bottom.visibility = View.GONE
                            }
                        }

                        tvName.text = it.goods_name
                        tvMarket.text = "市场价 ¥ ${it.goods_price}"
                        tvMarket.line()
                        tv_hot.visibility = if (it.flag == 1) View.VISIBLE else View.GONE
                        tv_cut.visibility = if (it.flag_tag == 1) View.VISIBLE else View.GONE
                        tv_pat_user.text = if (it.auction_user_id == 0) "暂无" else "${data?.auction_phone}"
                        tv_pin.isSelected = true
                        tv_order_no.text = it.product_id.toString()
                    }

                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                }
            })

    }

    private fun setBanner(list: ArrayList<String>) {
        banner.adapter = MyBannerAdapter(mContext, list)
        banner.indicator = NumIndicator(this)
        banner.setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
        banner.addBannerLifecycleObserver(this)
        banner.setOnBannerListener(object : OnBannerListener<String> {
            override fun OnBannerClick(data: String?, position: Int) {
                PhotoViewActiviy.start(mContext, list, position)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_pat -> patDialog(product)
            R.id.tv_peek_watch_ -> {
                if (product?.current_price == null) {
                    patPresenter?.peek(productId, product?.goods_name)
                } else {
                    MyToast.show("60s内只能偷瞄一次")
                }
            }
            R.id.tv_share -> share(product)
        }
    }

    private fun share(product: GoodsDetail?) {
        product?.let {
            val dialog = GoodsShareDialog(mContext, product)
            dialog.show()
        }
    }

    private fun patDialog(item: GoodsDetail?) {
        val dialog = PatConfirmDialog(mContext, item?.goods_name)
        dialog.show()
        dialog.callback = object : DialogCallback {
            override fun onConfirm() {
                patPresenter?.pat(productId)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    fun onPeekChange(event: PeekChangeEvent) {
        val inPeeking = MyApplication.isInPeeking(product?.product_id)
        if (inPeeking != null) {
            product?.current_price = inPeeking.current_price
            tv_current_price.text = "现价¥ ${inPeeking.current_price}"
        } else {
            product?.current_price = null
            tv_current_price.text = "现价¥ ???"
        }
    }


    override fun onPrePatSuccess(productId: Int?) {
        initData()
    }

    override fun onPeekSucc(productId: Int?) {
        initData()
    }


}