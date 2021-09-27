package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.PartnerModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.PartnerAdapter
import com.`fun`.auction.ui.dialog.InvitationDialog
import com.`fun`.auction.util.MyToast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_partner.*

class PartnerActivity : BaseActivity(), View.OnClickListener {

    var mData: PartnerModel? = null
    var partnerAdapter: PartnerAdapter? = null

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, PartnerActivity::class.java))
        }
    }

    override fun fullScreen(): Boolean {
        return true
    }

    override fun getLayoutId() = R.layout.activity_partner

    override fun init() {
        iv_back.setOnClickListener(this)
        tv_apply.setOnClickListener(this)

        recyclerView.layoutManager = GridLayoutManager(mContext, 5)
        partnerAdapter = PartnerAdapter()
        recyclerView.adapter = partnerAdapter

        partnerAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = partnerAdapter?.getItem(position)
                if (item == -1L) {
                    var dialog = InvitationDialog(mContext)
                    dialog.show()
                }
            }
        })

    }

    override fun initData() {
        HttpManager.api?.getPartenr()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<PartnerModel>() {
                override fun onSuccess(code: Int, msg: String, data: PartnerModel?) {
                    val tenList = getTenList(data)
                    partnerAdapter?.setNewInstance(tenList)
                    data?.let {
                        mData = data
                        when (it.is_hhr) {
                            -1 -> tv_apply.text = "立即申请"
                            0 -> tv_apply.text = "审核中"
                            1 -> tv_apply.text = "进入合伙人后台"
                            2 -> tv_apply.text = "审核驳回"
                        }
                    }
                }
            })
    }

    private fun getTenList(data: PartnerModel?): MutableList<Long> {
        var index = data?.listleng ?: 10
        val source = data?.list

        val list = mutableListOf<Long>()
        for (i in 0 until index) {
            if (!source.isNullOrEmpty() && (i < source.size)) {
                list.add(source[i])
            } else {
                list.add(-1L)
            }
        }
        return list
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> onBackPressed()
            R.id.tv_apply -> {
                mData?.let {
                    if (it.is_hhr == -1 || it.is_hhr == 2) {
                        if (it.invite_num >= it.listleng) {
                            applyPartner()
                        } else {
                            MyToast.show("请先完成邀请任务")
                        }
                    } else if (it.is_hhr == 0) {
                        MyToast.show("审核中")
                    } else if (it.is_hhr == 1) {
//                        WebViewActivity.start(mContext, "合伙人后台", it.hhr_url)
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        val uri = Uri.parse(it.hhr_url)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun applyPartner() {
        HttpManager.api?.applyPartenr()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show("申请成功")
                    initData()
                }
            })
    }
}