package com.`fun`.auction.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.*
import com.`fun`.auction.model.Event.Companion.BOX_UPDATE
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.BoxRecordAdapter1
import com.`fun`.auction.ui.adapter.BoxRecordAdapter2
import com.`fun`.auction.ui.dialog.DialogCallback
import com.`fun`.auction.ui.dialog.ReSellDialog
import com.`fun`.auction.util.MyToast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_box_record.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BoxRecordFragment : BaseFragment() {

    var lastId: Int? = null
    val type by lazy {
        arguments?.getInt("position", 0) ?: 0
    }
    var recordAdapter: BaseQuickAdapter<BoxRecord, BaseViewHolder>? = null
    var item: BoxRecord? = null

    companion object {
        fun newInstance(position: Int): Fragment {
            val arg = Bundle()
            arg.putInt("position", position)
            val fragment = BoxRecordFragment()
            fragment.arguments = arg
            return fragment
        }
    }


    override fun getLayoutId() = R.layout.fragment_box_record

    override fun init() {
        EventBus.getDefault().register(this)
        if (type == 0) {
            recyclerview.layoutManager = LinearLayoutManager(mContext)
            recordAdapter = BoxRecordAdapter1()
            recyclerview.adapter = recordAdapter
        } else {
            recyclerview.layoutManager = LinearLayoutManager(mContext)
            recordAdapter = BoxRecordAdapter2()
            recyclerview.adapter = recordAdapter
        }

        recordAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = recordAdapter?.getItem(position)
                BoxDetailActivity.start(mContext, item?.box_id, type == 0)
            }
        })

        recordAdapter?.addChildClickViewIds(R.id.btn_sell, R.id.tv_entity, R.id.tv_order_status)
        recordAdapter?.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                item = recordAdapter?.getItem(position)
                item?.let {
                    when (view.id) {
                        R.id.tv_entity -> selectAddress()
                        R.id.btn_sell -> getSellInfo(it)
                        R.id.tv_order_status -> {
                            if (it.box_status == 1) {
                                SellCenterActivity.start(mContext)
                            } else if (item?.box_status == 2) {
                                BoxOrderActivity.start(mContext, it.box_id)
                            }
                        }
                    }
                }
            }
        })


        refresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                lastId = null
                lazyInit()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                lazyInit()
            }
        })
    }


    override fun lazyInit() {
        var status = if (type == 0) 0 else -1
        HttpManager.api?.boxRecord(status, lastId)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<RecordModel>() {
                override fun onSuccess(code: Int, msg: String, data: RecordModel?) {
                    val list = data?.list
                    if (list!=null) {
                        setData(list)
                    }
                    lastId = data?.last_id
                }

                override fun onFinish() {
                    refresh.finishRefresh()
                    refresh.finishLoadMore()
                    recordAdapter?.setEmptyView(R.layout.empty_view)
                }
            })

    }

    private fun setData(list: MutableList<BoxRecord>) {
        if (lastId == null) {
            recordAdapter?.setNewInstance(list)
        } else {
            recordAdapter?.addData(list)
        }
    }


    private fun selectAddress() {
        AddressActivitiy.startResult(mContext, this, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val address = data?.getSerializableExtra("data") as Address?
            if (address != null) {
                enity(item, address)
            }
        }
    }

    private fun enity(item: BoxRecord?, address: Address) {
        val map = mapOf("box_id" to item?.box_id, "address_id" to address.address_id)
        HttpManager.api?.enity(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(Event(BOX_UPDATE))
                }
            })
    }


    private fun getSellInfo(item: BoxRecord) {
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

    private fun reSell(item: BoxRecord?) {
        val map = mapOf("box_id" to item?.box_id)
        HttpManager.api?.sell(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    EventBus.getDefault().post(Event(BOX_UPDATE))
                }
            })
    }


    @Subscribe
    fun refresh(event: Event){
        if(event.type==Event.BOX_UPDATE){
            lastId = null
            lazyInit()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}