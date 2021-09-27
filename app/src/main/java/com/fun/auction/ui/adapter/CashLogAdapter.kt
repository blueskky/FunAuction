package com.`fun`.auction.ui.adapter

import com.`fun`.auction.R
import com.`fun`.auction.model.CashLog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.downloader.PRDownloader.getStatus
import java.text.SimpleDateFormat

class CashLogAdapter : BaseQuickAdapter<CashLog, BaseViewHolder>(R.layout.cash_log) {


    override fun convert(holder: BaseViewHolder, item: CashLog) {
        holder.setText(R.id.tv_order, "订单号-${item.order_m}")
        val money = item.money / 100
        holder.setText(R.id.tv_amount, "${item.getType()}  ¥ $money")
        holder.setText(R.id.tv_time, time(item.create_time))
        holder.setText(R.id.tv_status, getStatus(item.status))
    }


    fun time(time: Long): String {
        var formatTime = time * 1000
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return simpleDateFormat.format(formatTime)
    }


    ////0待审核 1审核通过等待打款  2打款成功 3未确定是否打款成功
    fun getStatus(status: Int): String {
        return when (status) {
            0 -> "待审核"
            1 -> "待打款"
            2 -> "已打款"
            3 -> "未知"
            4 -> "提现失败"
            else -> ""
        }
    }
}