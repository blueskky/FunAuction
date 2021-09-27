package com.`fun`.auction.ui.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.`fun`.auction.R
import com.`fun`.auction.model.Goods
import com.`fun`.auction.ui.RankActivity
import com.youth.banner.adapter.BannerAdapter

class BoxBannerAdapter(context: Context, goods: List<Goods>) :
    BannerAdapter<Goods, BoxBannerAdapter.BoxViewHolder>(goods) {

    val mContext = context

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BoxViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.box_banner_item, null, false)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return BoxViewHolder(view)
    }

    override fun onBindView(holder: BoxViewHolder?, data: Goods?, position: Int, size: Int) {
        Glide.with(mContext).load(data?.goods_image).into(holder!!.image!!)
        holder.goodsName?.text = data?.goods_name
        holder.goodsPrice?.text = Html.fromHtml("市场价: <font color='#ef5d30'>¥ ${data?.goods_price}</font>")
    }


    class BoxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView? = null
        var goodsName: TextView? = null
        var goodsPrice: TextView? = null
        var rank: TextView? = null

        init {
            image = view.findViewById(R.id.iv_img)
            goodsName = view.findViewById(R.id.tv_goods_name)
            goodsPrice = view.findViewById(R.id.tv_goods_price)
            rank = view.findViewById(R.id.tv_rank)
        }
    }
}