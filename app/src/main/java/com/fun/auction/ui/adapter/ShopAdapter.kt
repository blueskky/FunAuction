package com.`fun`.auction.ui.adapter

import android.text.Html
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.iwgang.countdownview.CountdownView
import com.`fun`.auction.MyApplication
import com.`fun`.auction.R
import com.`fun`.auction.formatTime
import com.`fun`.auction.line
import com.`fun`.auction.model.Product
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ShopAdapter : BaseQuickAdapter<Product, ShopAdapter.MyBaseViewHolder>(R.layout.shop_item) {

    val limitTime = 24 * 60 * 60 * 1000

    override fun createBaseViewHolder(view: View): MyBaseViewHolder {
        return MyBaseViewHolder(view)
    }

    override fun convert(holder: ShopAdapter.MyBaseViewHolder, item: Product) {

        val inPeeking = MyApplication.isInPeeking(item.product_id)
        if (inPeeking != null) {
            item.current_price = inPeeking.current_price
        } else {
            item.current_price = null
        }


        with(holder) {
            Glide.with(context).load(item.goods_image).into(cover)
            tv_name.text = item.goods_name
            tvMarket.text = "¥ " + item.goods_price.toDoubleOrNull()?.toInt()
            tvMarket.line()

            tvCutTime.text = context.formatTime(item?.update_time)

            tvHot.visibility = if (item.flag == 1) VISIBLE else GONE
            tvDiscount.visibility = if (item.flag_tag == 1) VISIBLE else GONE


            llTime.visibility = INVISIBLE
            tv_trans_price.visibility = GONE
            tv_user.visibility = GONE
            tvEnd.visibility = GONE
            llPat.visibility = VISIBLE

            val auctionTime = item?.auction_time * 1000
            if (auctionTime > 0) {
                val delay = limitTime - (System.currentTimeMillis() - auctionTime)
                if (delay > 0) {
                    //倒计时
                    llTime.visibility = VISIBLE
                    count_view.start(delay)

                } else {
                    //已结束
                    llPat.visibility = GONE
                    tv_trans_price.visibility = VISIBLE
                    tv_user.visibility = VISIBLE
                    tv_trans_price.text = Html.fromHtml("成交价格 <font color='#4ebd60'>¥ ${item.auction_price}</font>")
                    tv_user.text = Html.fromHtml("中拍用户 <font color='#4ebd60'>${item.auction_phone}</font>")
                    tvEnd.visibility = VISIBLE
                }
            }

            var currentPrice = item.current_price
            if (currentPrice != null) {
                tvCurrentPrice.text = "现价¥ ${currentPrice}"
            } else {
                tvCurrentPrice.text = "现价¥ ？？？"
            }

        }

    }


    class MyBaseViewHolder(view: View) : BaseViewHolder(view) {
        var cover: ImageView
        var tvMarket: TextView
        var tv_name: TextView
        var tvCutTime: TextView
        var tvPat: TextView
        var llPat: View
        var tv_trans_price: TextView
        var tv_user: TextView
        var llTime: View
        var count_view: CountdownView
        var tvDiscount: View
        var tvHot: TextView
        var tvEnd: TextView
        var tvCurrentPrice: TextView
        var rlPeek: RelativeLayout

        init {
            cover = view.findViewById(R.id.iv_cover)
            tvMarket = view.findViewById(R.id.tvMarket)
            tv_name = view.findViewById(R.id.tv_name)
            tvDiscount = view.findViewById(R.id.tv_cut)
            tvHot = view.findViewById(R.id.tv_hot)


            //降价时间 直接拍
            llPat = view.findViewById(R.id.ll_pat)
            tvCutTime = view.findViewById(R.id.tv_cut_time)
            tvPat = view.findViewById(R.id.tvPat)


            //成交价格 用户
            tv_trans_price = view.findViewById(R.id.tv_trans_price)
            tv_user = view.findViewById(R.id.tv_user)

            //倒计时
            llTime = view.findViewById(R.id.ll_time)
            count_view = view.findViewById(R.id.count_view)

            //偷瞄
            rlPeek = view.findViewById(R.id.rl_peek)
            tvCurrentPrice = view.findViewById(R.id.tv_current_price)
            tvEnd = view.findViewById(R.id.tv_end)
        }
    }
}