package com.`fun`.auction.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.`fun`.auction.R
import com.`fun`.auction.WishItem

class WisthListAdapter() : BaseQuickAdapter<WishItem, BaseViewHolder>(R.layout.wish_item) {

    override fun convert(holder: BaseViewHolder, item: WishItem) {

        if (item.check) {
            holder.getView<ImageView>(R.id.iv_check).setImageResource(R.drawable.icon_want_check)
        } else {
            holder.getView<ImageView>(R.id.iv_check).setImageResource(R.drawable.icon_want_normal)
        }
    }
}