package com.`fun`.auction.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.holder.BannerImageHolder

class MyBannerAdapter(context: Context, data: List<String>) :  BannerAdapter<String, BannerImageHolder>(data) {

    private val context = context

    override fun onBindView(holder: BannerImageHolder?, data: String?, position: Int, size: Int) {
        if (holder?.itemView != null) {
            Glide.with(context).load(data).into(holder?.imageView)
        }
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerImageHolder {
        val imageView = ImageView(parent!!.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerImageHolder(imageView)
    }
}