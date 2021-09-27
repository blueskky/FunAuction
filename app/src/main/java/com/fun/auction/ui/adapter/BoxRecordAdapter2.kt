package com.`fun`.auction.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.`fun`.auction.R
import com.`fun`.auction.model.BoxRecord
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class BoxRecordAdapter2 : BaseQuickAdapter<BoxRecord, BaseViewHolder>(R.layout.box_item) {

    override fun convert(holder: BaseViewHolder, item: BoxRecord) {
        val view = holder.getView<ImageView>(R.id.iv_cover)
        Glide.with(context).load(item.goods_image).into(view)
        holder.setText(R.id.tv_name, item.goods_name)
        holder.setText(R.id.tv_rule, "数量 x1")

        val tvStatus = holder.getView<TextView>(R.id.tv_order_status)
        val boxStatus = item.box_status  //待售卖未处理(0) 售卖(1) 邮寄(2) 过期单子(3) -1代表已处理
        if (boxStatus == 1) {
            tvStatus.text = "已申请转卖,去查看 >"
        } else if (boxStatus == 2) {
            tvStatus.text = "查看订单 >"
        }
    }
}