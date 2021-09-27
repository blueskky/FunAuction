package com.`fun`.auction.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.Address
import com.`fun`.auction.model.AddressModel
import com.`fun`.auction.model.RefreshAddressEvent
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.AddressAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_address_activitiy.*
import kotlinx.android.synthetic.main.activity_address_activitiy.recyclerview
import kotlinx.android.synthetic.main.common_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class AddressActivitiy : BaseActivity(), View.OnClickListener {


    var addressAdapter: AddressAdapter? = null

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, AddressActivitiy::class.java))
        }

        fun startResult(context: Context?, fragment: Fragment, i: Int) {
            fragment.startActivityForResult(Intent(context, AddressActivitiy::class.java), i)
        }

        fun startResult(activity: Activity,  i: Int) {
            activity.startActivityForResult(Intent(activity, AddressActivitiy::class.java), i)
        }
    }

    override fun getLayoutId() = R.layout.activity_address_activitiy
    override fun init() {
        EventBus.getDefault().register(this)
        initTitle(iv_back, tv_title, "收货地址")

        recyclerview.layoutManager = LinearLayoutManager(mContext)
        addressAdapter = AddressAdapter()
        recyclerview.adapter = addressAdapter

        tv_add.setOnClickListener(this)

        addressAdapter?.addChildClickViewIds(R.id.tv_edit, R.id.tv_delete, R.id.checkbox)
        addressAdapter?.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = addressAdapter?.getItem(position)
                when (view?.id) {
                    R.id.tv_edit -> EditAdressActivity.start(mContext, item)
                    R.id.tv_delete -> deleteAdress(item)
                    R.id.checkbox -> {
                        if (item != null && !item.default) {
                            setDefaultAddrees(item)
                        }
                    }
                }
            }
        })

        addressAdapter?.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = addressAdapter?.getItem(position)
                setResult(RESULT_OK,Intent().putExtra("data",item))
                finish()
            }
        })

    }

    private fun setDefaultAddrees(item: Address) {
        val map = mutableMapOf<String, String>()
        map["address_id"] = item?.address_id.toString()

        HttpManager.api?.defaultAddress(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    initData()
                }
            })

    }

    private fun deleteAdress(item: Address?) {
        val map = mutableMapOf<String, String>()
        map["address_id"] = item?.address_id.toString()

        HttpManager.api?.deleteAddress(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    initData()
                }
            })
    }


    override fun initData() {
        HttpManager.api?.addressList()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<AddressModel>() {
                override fun onSuccess(code: Int, msg: String, data: AddressModel?) {
                    val list = data?.list
                    if (!list.isNullOrEmpty()) {
                        for (item in list) {
                            if (item.address_id == data.default_id) {
                                item.default = true
                            }
                        }
                        addressAdapter?.setNewInstance(list)
                    }

                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_add -> EditAdressActivity.start(mContext, null)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun refresh(event: RefreshAddressEvent) {
        initData()
    }
}