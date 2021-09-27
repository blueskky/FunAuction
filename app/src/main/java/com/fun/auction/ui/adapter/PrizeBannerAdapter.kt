package com.`fun`.auction.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.`fun`.auction.R
import com.`fun`.auction.model.PrizeMessage
import com.youth.banner.adapter.BannerAdapter

class PrizeBannerAdapter(context: Context, goods: List<PrizeMessage>) :
    BannerAdapter<PrizeMessage, PrizeBannerAdapter.ViewHolder>(goods) {

    val mContext = context

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): PrizeBannerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.prize_banner_item, null, false)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return PrizeBannerAdapter.ViewHolder(view)
    }

    override fun onBindView(holder: PrizeBannerAdapter.ViewHolder?, data: PrizeMessage?, position: Int, size: Int) {
        holder?.tvMessage?.text = "恭喜${data?.phone}抽中${data?.goods_name}"
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvMessage: TextView? = null

        init {
            tvMessage = view.findViewById(R.id.tvMessage)

        }
    }
}