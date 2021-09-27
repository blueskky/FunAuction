package com.`fun`.auction.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.`fun`.auction.R
import com.`fun`.auction.model.Task


class RewardAdapter : BaseQuickAdapter<Task, BaseViewHolder>(R.layout.reward_item) {

    private val resIds = arrayListOf<Int>(
        R.drawable.box_level1,
        R.drawable.box_level2,
        R.drawable.box_level3,
        R.drawable.box_level4,
        R.drawable.box_level5
    )

    override fun convert(holder: BaseViewHolder, item: Task) {
        if (holder.layoutPosition < resIds.size) {
            val view = holder.getView<ImageView>(R.id.iv_box)
            view.setImageResource(resIds[holder.layoutPosition])
        }
        val i = (holder.layoutPosition + 1) * 10
        val cover = holder.getView<View>(R.id.fl_cover)
        if (item.activate()) {
            if (item.received()) {
                //已领取
                holder.setBackgroundResource(R.id.dot, R.drawable.circle_dot_open)
                holder.setText(R.id.tv_des, "满${i}次\n已领取")
                holder.setTextColor(R.id.tv_des,Color.parseColor("#ffffff"))
                cover.visibility=View.VISIBLE
            } else {
                //可领取
                holder.setBackgroundResource(R.id.dot, R.drawable.circle_dot_received)
                val i = (holder.layoutPosition + 1) * 10
                holder.setText(R.id.tv_des, "满${i}次\n待领取")
                holder.setTextColor(R.id.tv_des,Color.parseColor("#00C853"))
                cover.visibility=View.GONE
            }
        } else {
            //未达标
            holder.setBackgroundResource(R.id.dot, R.drawable.circle_dot)
            holder.setText(R.id.tv_des, "满${i}次\n免费领取")
            holder.setTextColor(R.id.tv_des,Color.parseColor("#67707f"))
            cover.visibility=View.VISIBLE
        }
    }


}