package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.Address
import com.`fun`.auction.model.Goods
import com.`fun`.auction.model.ReSellInfo
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.CurrentHitAdapter
import com.`fun`.auction.ui.dialog.DialogCallback
import com.`fun`.auction.ui.dialog.ReSellDialog
import com.`fun`.auction.util.MyToast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_current_field.*
import java.io.Serializable

class CurrentFieldActivity : BaseActivity(), View.OnClickListener {


    companion object {
        fun start(context: Context?, list: MutableList<Goods>, reward: Boolean) {
            val intent = Intent(context, CurrentFieldActivity::class.java)
            intent.putExtra("data", list as Serializable)
            intent.putExtra("reward", reward)
            context?.startActivity(intent)
        }
    }

    var selectPosition: Int? = null
    var item: Goods? = null
    var currentAdapter: CurrentHitAdapter? = null
    val reaward by lazy {
        intent.getBooleanExtra("reward", false)
    }

    override fun getLayoutId() = R.layout.activity_current_field


    override fun fullScreen(): Boolean {
        return true
    }


    override fun init() {
        tvSkip.setOnClickListener(this)
        iv_back.setOnClickListener(this)

        val reward = intent.getBooleanExtra("reward", false)
        val list = intent.getSerializableExtra("data") as MutableList<Goods>

        if (reward) {
            tv_title.text = "本次奖励记录"
            tv_tips.text = "奖励宝盒24小时未处理将失效"
        }

        recyclerview.layoutManager = LinearLayoutManager(mContext)
        currentAdapter = CurrentHitAdapter()
        recyclerview.adapter = currentAdapter
        currentAdapter?.setNewInstance(list)


        currentAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item1 = currentAdapter?.getItem(position)
                BoxDetailActivity.start(mContext, item1?.box_id, true)
            }
        })

        currentAdapter?.addChildClickViewIds(R.id.tv_entity, R.id.btn_sell)
        currentAdapter?.setOnItemChildClickListener { adapter, view, position ->
            selectPosition = position
            item = currentAdapter?.getItem(position)
            item?.let {
                when (view.id) {
                    R.id.tv_entity -> selectAddress()
                    R.id.btn_sell -> getSellInfo(it.box_id)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSkip, R.id.iv_back -> finish()
            R.id.tvSell -> ""
        }
    }


    private fun getSellInfo(box_id: Int) {
        HttpManager.api?.resell(box_id)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<ReSellInfo>() {
                override fun onSuccess(code: Int, msg: String, data: ReSellInfo?) {
                    val reSellDialog = ReSellDialog(mContext, data)
                    reSellDialog.show()
                    reSellDialog.dialogCallback = object : DialogCallback {
                        override fun onConfirm() {
                            reSell(box_id)
                        }
                    }
                }
            })
    }

    private fun reSell(box_id: Int) {
        val map = mapOf("box_id" to box_id)
        HttpManager.api?.sell(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    currentAdapter?.data?.remove(item)
                    currentAdapter?.notifyDataSetChanged()
                }
            })
    }


    private fun selectAddress() {
        AddressActivitiy.startResult(this, 100)
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

    private fun enity(item: Goods?, address: Address) {
        val map = mapOf("box_id" to item?.box_id, "address_id" to address.address_id)
        HttpManager.api?.enity(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    currentAdapter?.data?.remove(item)
                    currentAdapter?.notifyDataSetChanged()

                }
            })
    }

}