package com.`fun`.auction.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.`fun`.auction.R
import com.`fun`.auction.model.SystemMsg
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.DataFormatException

class MessageAdapter() : BaseQuickAdapter<SystemMsg, BaseViewHolder>(R.layout.item_msg) {
    override fun convert(holder: BaseViewHolder, item: SystemMsg) {

        holder.setText(R.id.tv_message, item.msg)
        val createTime = item.create_time * 1000
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val format = simpleDateFormat.format(Date(createTime))
        holder.setText(R.id.tv_time, format)

        val tvStatus = holder.getView<TextView>(R.id.tv_status)
        tvStatus.text = if (item.status == 0) "未读 >" else "已读 >"
        tvStatus.setTextColor(if (item.status == 0) Color.parseColor("#FF605D") else Color.parseColor("#00FFDE"))
    }
}