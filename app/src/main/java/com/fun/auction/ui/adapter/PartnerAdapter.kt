package com.`fun`.auction.ui.adapter

import android.view.View
import com.`fun`.auction.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class PartnerAdapter : BaseQuickAdapter<Long, BaseViewHolder>(R.layout.partner_item) {
    override fun convert(holder: BaseViewHolder, item: Long) {
        if (item == -1L) {
            holder.setBackgroundResource(R.id.rl_avator, R.drawable.dot_circle)
            holder.getView<View>(R.id.iv_cover).visibility = View.GONE
            holder.setText(R.id.tv_des, "去邀请")
        } else {
            holder.setBackgroundResource(R.id.rl_avator, R.drawable.dot_circle_trans)
            holder.getView<View>(R.id.iv_cover).visibility = View.VISIBLE
            holder.setText(R.id.tv_des, "用户${item}")
        }

    }
}