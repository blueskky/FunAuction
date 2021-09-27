package com.`fun`.auction.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.`fun`.auction.R
import com.`fun`.auction.line
import com.`fun`.auction.model.WelFare
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class VipAdapter : BaseQuickAdapter<WelFare, BaseViewHolder>(R.layout.vip_item) {


    override fun convert(holder: BaseViewHolder, item: WelFare) {
        val view = holder.getView<ImageView>(R.id.iv_avator)
        Glide.with(context).load(item.goods_image).into(view)
        holder.setText(R.id.tv_title, item.goods_name)
        holder.setText(R.id.tv_price, "会员价￥${item.user_prize}")
        val tvMarker = holder.getView<TextView>(R.id.tv_market)
        tvMarker.text = "市场价￥${item.goods_prize}"
        tvMarker.line()
        holder.setText(R.id.tv_point, "积分兑${item.get_prize}")
    }
}