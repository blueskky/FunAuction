package com.`fun`.auction.ui.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.iwgang.countdownview.CountdownView
import com.`fun`.auction.PrefercencesManager
import com.`fun`.auction.R
import com.`fun`.auction.formatTime
import com.`fun`.auction.line
import com.`fun`.auction.model.PatRecord
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class PatRecordAdapter() : BaseQuickAdapter<PatRecord, BaseViewHolder>(R.layout.pat_item) {

    val limitTime = 24 * 60 * 60 * 1000

    override fun convert(holder: BaseViewHolder, item: PatRecord) {
        //角标状态
        val tvPatStatus = holder.getView<TextView>(R.id.tv_pat_status)
        when (item.store_status) {
            0 -> {
                tvPatStatus.text = "偷瞄过"
                tvPatStatus.setBackgroundResource(R.drawable.marker_bg)
            }
            1 -> {
                tvPatStatus.text = "已拍出"
                tvPatStatus.setBackgroundResource(R.drawable.pat_end)
            }
            2 -> {
                tvPatStatus.text = "拍成功"
                tvPatStatus.setBackgroundResource(R.drawable.pat_succ)
            }
            3 -> {
                tvPatStatus.text = "已出价"
                tvPatStatus.setBackgroundResource(R.drawable.pat_price)
            }
            4 -> {
                tvPatStatus.text = "待重拍"
                tvPatStatus.setBackgroundResource(R.drawable.pat_re)
            }
        }

        val view = holder.getView<ImageView>(R.id.iv_avator)
        Glide.with(context).load(item.goods_image).into(view)
        holder.setText(R.id.tv_title, item.goods_name)

        if (item.store_status == 1) {
            holder.setText(R.id.tv_price, "成交价格 ¥ ${item.auction_price}")
        } else if (item.store_status == 3 || item.store_status == 4) {
            val userId = PrefercencesManager.getInstance()?.getUserId()
            if (item.store_user == userId && !TextUtils.isEmpty(item.my_store_money) && item.my_store_money.toDouble() > 0) {
                holder.setText(R.id.tv_price, "我的出价 ¥ ${item.my_store_money}")
            } else {
                holder.setText(R.id.tv_price, "现价 ¥ ???")
            }
        } else {
            holder.setText(R.id.tv_price, "现价 ¥ ???")
        }

        holder.setText(R.id.tv_market, "市场价 ¥ ${item.goods_price}")
        holder.getView<TextView>(R.id.tv_market).line()

        if (item.store_status == 1) {
            holder.getView<View>(R.id.ll_down_price).visibility = View.GONE
            holder.setText(R.id.tv_pat_user, item.auction_phone)
            holder.setText(R.id.tv_price_user, "中拍用户")
        } else {
            holder.getView<View>(R.id.ll_down_price).visibility = View.VISIBLE
            holder.setText(R.id.tv_cut_time, context.formatTime(item.store_down_time))

            holder.setText(R.id.tv_price_user, "出价用户")
            val user = if (item.store_user_phone.isNullOrEmpty()) "暂无" else item.store_user_phone
            holder.setText(R.id.tv_pat_user, user)
        }

        holder.setText(R.id.tv_peek_num, "${item.peek_num}次")
        val countDownLayout = holder.getView<View>(R.id.ll_down)
        if (item.is_over == 1) {
            countDownLayout.visibility = View.GONE
        } else {
            val countdownView = holder.getView<CountdownView>(R.id.tv_count_down)
            val priceTime = item.store_price_time * 1000
            val delay = limitTime - (System.currentTimeMillis() - priceTime)
            if (priceTime > 0 && delay > 0) {
                countDownLayout.visibility = View.VISIBLE
                countdownView.start(delay)
            } else {
                countDownLayout.visibility = View.GONE
            }
        }

    }

}