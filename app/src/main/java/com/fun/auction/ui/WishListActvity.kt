package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.WishItem
import com.`fun`.auction.ui.adapter.WisthListAdapter
import kotlinx.android.synthetic.main.activity_wish_list_actvity.*
import kotlinx.android.synthetic.main.common_title.*

class WishListActvity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, WishListActvity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_wish_list_actvity

    override fun init() {
        initTitle(iv_back, tv_title, "许愿瓶")
        btn_wish.setOnClickListener(this)

        recyclerview.layoutManager = LinearLayoutManager(mContext)
        val adapter = WisthListAdapter()
        recyclerview.adapter = adapter

        val list = mutableListOf<WishItem>()
        for (i in 0..3) {
            list.add(WishItem())
        }
        adapter.setNewInstance(list)

        adapter.setOnItemClickListener { _, _, position ->
            val item = adapter.getItem(position)
            item.check = !item.check
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> onBackPressed()
            R.id.btn_wish -> WishActivity.start(mContext)
        }
    }
}