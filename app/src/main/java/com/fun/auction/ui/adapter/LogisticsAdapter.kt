package com.`fun`.auction.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.`fun`.auction.R
import com.`fun`.auction.model.Info
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.text.SimpleDateFormat

class LogisticsAdapter : BaseQuickAdapter<Info, BaseViewHolder>(R.layout.logis_progress_item) {
    override fun convert(holder: BaseViewHolder, item: Info) {
        val tv_day = holder.getView<TextView>(R.id.tv_day)
        val tv_time = holder.getView<TextView>(R.id.tv_time)
        val tv_content = holder.getView<TextView>(R.id.tv_content)
        val topLine = holder.getView<View>(R.id.top_line)
        val tvDot = holder.getView<View>(R.id.tv_dot)
        val bottomLine = holder.getView<View>(R.id.bottom_line)

        val split = item.time.split(" ")
        if (split != null && split.size > 1) {
            tv_day.text = split[0]
            tv_time.text = split[1]
        }
        tv_content.text = item.content


        val position = holder.layoutPosition
        if (position == 0) {
            topLine.visibility = View.INVISIBLE
            bottomLine.visibility = View.VISIBLE
        } else if (position == itemCount - 1) {
            topLine.visibility = View.VISIBLE
            bottomLine.visibility = View.INVISIBLE
        } else {
            topLine.visibility = View.VISIBLE
            bottomLine.visibility = View.VISIBLE
        }


        if (position == 0) {
            tv_day.setTextColor(Color.parseColor("#ffffff"))
            tv_time.setTextColor(Color.parseColor("#ffffff"))
            tvDot.setBackgroundResource(R.drawable.dot)
            bottomLine.setBackgroundColor(Color.parseColor("#ffffff"))
            tv_content.setTextColor(Color.parseColor("#ffffff"))
        } else {
            tv_day.setTextColor(Color.parseColor("#666666"))
            tv_time.setTextColor(Color.parseColor("#666666"))
            tv_content.setTextColor(Color.parseColor("#666666"))
            tvDot.setBackgroundResource(R.drawable.dot_gray)
            topLine.setBackgroundColor(Color.parseColor("#666666"))
            bottomLine.setBackgroundColor(Color.parseColor("#666666"))
        }

        if (position == 1) {
            topLine.setBackgroundColor(Color.parseColor("#ffffff"))
        }else{
            topLine.setBackgroundColor(Color.parseColor("#666666"))
        }
    }

}