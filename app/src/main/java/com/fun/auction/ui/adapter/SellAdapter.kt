package com.`fun`.auction.ui.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.`fun`.auction.R
import com.`fun`.auction.model.SellItem
import com.bumptech.glide.Glide

class SellAdapter(position: Int?) : BaseQuickAdapter<SellItem, BaseViewHolder>(R.layout.sell_item) {

    var pos = position

    override fun convert(holder: BaseViewHolder, item: SellItem) {
        val view = holder.getView<ImageView>(R.id.iv_img)
        Glide.with(context).load(item.goods_image).into(view)
        holder.setText(R.id.tv_name, item.goods_name)
        holder.setText(R.id.tv_sell_price, "¥ ${item.goods_price}")

        var tvStatus = holder.getView<TextView>(R.id.tv_status)
        if (pos == 0) {
            tvStatus.text = "转卖中"
        } else {
            tvStatus.setTextColor(Color.parseColor("#999999"))
            tvStatus.text = "已关闭"
        }
    }
}