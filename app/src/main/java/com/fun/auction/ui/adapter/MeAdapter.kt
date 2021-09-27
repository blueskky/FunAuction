package com.`fun`.auction.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.`fun`.auction.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class MeAdapter(data: MutableList<Any?>) : BaseQuickAdapter<Any?, BaseViewHolder>(R.layout.setting_item, data) {


    val icons = listOf(
        R.drawable.box_record, R.drawable.pat_record, R.drawable.icon_transf_, R.drawable.wu_liu,
        R.drawable.icon_welfare, R.drawable.icon_invitation, R.drawable.dai_li, R.drawable.sys_msg
    )
    val names = listOf("宝盒记录", "拍拍记录", "转卖中心", "物流中心", "会员福利", "邀请好友", "代理合作", "系统消息")

    var haveMessage: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun convert(holder: BaseViewHolder, item: Any?) {
        val view = holder.getView<ImageView>(R.id.iv_pic)
        view.setImageResource(icons[holder.layoutPosition])
        holder.setText(R.id.tv_name, names[holder.layoutPosition])

        val dot = holder.getView<TextView>(R.id.tv_dot)
        if (holder.layoutPosition == 7 && haveMessage > 0) {
            dot.visibility = View.VISIBLE
            dot.text = "$haveMessage"
        } else {
            dot.visibility = View.GONE
        }
    }
}