package com.`fun`.auction.ui.adapter

import android.widget.ImageView
import com.`fun`.auction.R
import com.`fun`.auction.model.LogisticsItem
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class LogisticsListAdapter : BaseQuickAdapter<LogisticsItem, BaseViewHolder>(R.layout.logis_item) {

    override fun convert(holder: BaseViewHolder, item: LogisticsItem) {
        val view = holder.getView<ImageView>(R.id.iv_img)
        Glide.with(context).load(item.box_info?.goods_image).into(view)
        holder.setText(R.id.tv_name, item.box_info?.goods_name)
    }
}