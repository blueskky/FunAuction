package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.`fun`.auction.R
import com.`fun`.auction.model.Address
import com.`fun`.auction.model.Event
import com.`fun`.auction.model.Event.Companion.BOX_UPDATE
import com.`fun`.auction.model.ReSellInfo
import com.`fun`.auction.model.RecordDetail
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.MyBannerAdapter
import com.`fun`.auction.ui.dialog.DialogCallback
import com.`fun`.auction.ui.dialog.ReSellDialog
import com.`fun`.auction.util.MyToast
import com.`fun`.auction.widget.NumIndicator
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.activity_box_detail.*
import kotlinx.android.synthetic.main.box_detail.*
import kotlinx.android.synthetic.main.common_title.iv_back
import kotlinx.android.synthetic.main.common_title.tv_title
import org.greenrobot.eventbus.EventBus

class BoxDetailActivity : BaseActivity(), View.OnClickListener {


    companion object {
        fun start(context: Context?, box_id: Int?, canSell: Boolean) {
            val intent = Intent(context, BoxDetailActivity::class.java)
            intent.putExtra("box_id", box_id)
            intent.putExtra("can_sell", canSell)
            context?.startActivity(intent)
        }
    }

    val boxId by lazy {
        intent.getIntExtra("box_id", 0)
    }
    val canSell by lazy {
        intent.getBooleanExtra("can_sell", false)
    }
    var recordData: RecordDetail? = null

    override fun getLayoutId() = R.layout.activity_box_detail

    override fun init() {
        initTitle(iv_back, tv_title, "商品详情")
        tv_entity.setOnClickListener(this)
        tv_resell.setOnClickListener(this)
        tv_share.setOnClickListener(this)
        ll_bottom.visibility = if (canSell) View.VISIBLE else View.GONE

    }


    override fun onClick(v: View?) {
        recordData?.let {
            when (v?.id) {
                R.id.tv_entity -> selectAddress()
                R.id.tv_resell -> getSellInfo(it)
                R.id.tv_share -> "share(product)"
                else -> {}
            }
        }
    }

    override fun initData() {
        HttpManager.api?.recordDetail(boxId)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<RecordDetail>() {
                override fun onSuccess(code: Int, msg: String, data: RecordDetail?) {
                    data?.let {
                        recordData = it
                        setBanner(it.images)
                        tvName.text = it.goods_name
                        box_no.text = "宝盒单号：${it.box_no}"
                    }
                }
            })
    }


    private fun setBanner(list: ArrayList<String>?) {
        if(list!=null){
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
    }

    private fun selectAddress() {
        AddressActivitiy.startResult(this@BoxDetailActivity, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val address = data?.getSerializableExtra("data") as Address?
            if (address != null) {
                enity(recordData, address)
            }
        }
    }

    private fun enity(item: RecordDetail?, address: Address) {
        val map = mapOf("box_id" to item?.box_id, "address_id" to address.address_id)
        HttpManager.api?.enity(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(Event(Event.BOX_UPDATE))
                    finish()
                }
            })
    }

    private fun getSellInfo(item: RecordDetail) {
        HttpManager.api?.resell(item.box_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<ReSellInfo>() {
                override fun onSuccess(code: Int, msg: String, data: ReSellInfo?) {
                    val reSellDialog = ReSellDialog(mContext, data)
                    reSellDialog.show()
                    reSellDialog.dialogCallback = object : DialogCallback {
                        override fun onConfirm() {
                            reSell(item)
                        }
                    }
                }
            })
    }

    private fun reSell(item: RecordDetail) {
        val map = mapOf("box_id" to item?.box_id)
        HttpManager.api?.sell(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(Event(Event.BOX_UPDATE))
                    finish()
                }
            })
    }


}