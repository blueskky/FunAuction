package com.`fun`.auction.ui.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.`fun`.auction.R
import com.`fun`.auction.model.BoxRecord

class BoxRecordAdapter1 : BaseQuickAdapter<BoxRecord, BaseViewHolder>(R.layout.box_item_) {


    override fun convert(holder: BaseViewHolder, item: BoxRecord) {
        val view = holder.getView<ImageView>(R.id.iv_cover)
        Glide.with(context).load(item.goods_image).into(view)
        holder.setText(R.id.tv_name, item.goods_name)
        holder.setText(R.id.tv_rule, "数量 x1")
    }
}