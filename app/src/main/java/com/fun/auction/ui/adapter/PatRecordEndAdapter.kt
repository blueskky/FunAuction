package com.`fun`.auction.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.`fun`.auction.R
import com.`fun`.auction.line
import com.`fun`.auction.model.PatEnd
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class PatRecordEndAdapter(type: Int?) : BaseQuickAdapter<PatEnd, BaseViewHolder>(R.layout.pat_item_end) {

    val mType = type
    val limitTime = 24 * 60 * 60 * 1000

    override fun convert(holder: BaseViewHolder, item: PatEnd) {
        val view = holder.getView<ImageView>(R.id.iv_avator)
        Glide.with(context).load(item.goods_image).into(view)
        holder.setText(R.id.tv_title, item.goods_name)

        holder.setText(R.id.tv_price, "成交价¥ ${item.total_price}")
        holder.setText(R.id.tv_market, "市场价 ¥ ${item.goods_price}")
        holder.getView<TextView>(R.id.tv_market).line()

        holder.setText(R.id.tv_cut_time, "${item.peek_num}")
        holder.setText(R.id.tv_pat_user, "${item.store_num}")


        val tvCover = holder.getView<TextView>(R.id.tv_end)
        val tvOrderDetail = holder.getView<View>(R.id.tv_order_detail)
        val btnReceive = holder.getView<View>(R.id.btn_receive)
        if (mType == 1) {
            tvOrderDetail.visibility = View.GONE
            btnReceive.visibility = View.VISIBLE
        } else {
            tvCover.visibility = View.VISIBLE
            if (item.box_status == 3) {
                tvCover.text = "已过期"
            } else {
                tvCover.text = "已结束"
            }
        }
    }

}