package com.`fun`.auction.ui.adapter

import android.text.Html
import com.`fun`.auction.R
import com.`fun`.auction.model.CashLog
import com.`fun`.auction.model.ChargeLog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.downloader.PRDownloader.getStatus
import java.text.SimpleDateFormat

class ChargeLogAdapter : BaseQuickAdapter<ChargeLog, BaseViewHolder>(R.layout.charge_log) {


    override fun convert(holder: BaseViewHolder, item: ChargeLog) {
        holder.setText(R.id.tv_order, "订单号-${item.order_no}")
        val money = item.amount / 100
        holder.setText(R.id.tv_amount, "${item.getPayWay()} ¥ $money")
        holder.setText(R.id.tv_time, time(item.order_time))
        val total = item.qty + item.give_qty
        holder.setText(R.id.tv_count, "+${total}")
    }


    fun time(time: Long): String {
        var formatTime = time * 1000
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return simpleDateFormat.format(formatTime)
    }

}