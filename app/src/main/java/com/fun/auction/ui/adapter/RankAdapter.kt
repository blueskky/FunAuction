package com.`fun`.auction.ui.adapter

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.`fun`.auction.R
import com.`fun`.auction.model.Goods
import com.bumptech.glide.Glide

class RankAdapter(data: MutableList<Goods>) : BaseQuickAdapter<Goods, BaseViewHolder>(R.layout.rank_item, data) {

    override fun convert(holder: BaseViewHolder, item: Goods) {
        val view = holder.getView<ImageView>(R.id.iv_last_box)
        Glide.with(context).load(item.goods_image).into(view)
        holder.setText(R.id.tv_name, item.goods_name)

        val html = "市场价 <font color='#d6273c'>¥ ${item.goods_price}</font>"
        holder.setText(R.id.tv_price, Html.fromHtml(html))

        val isHot = item.is_hot
        val tvTag = holder.getView<TextView>(R.id.tv_tag)
        tvTag.visibility = View.VISIBLE
        if (isHot == 30) {
            tvTag.text = "精品"
            tvTag.setBackgroundResource(R.drawable.chaoji_shape)
        } else if (isHot == 40) {
            tvTag.text = "超级"
            tvTag.setBackgroundResource(R.drawable.jinpin_shape)
        } else {
            tvTag.visibility = View.GONE
        }
    }
}