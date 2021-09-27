package com.`fun`.auction.ui.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.`fun`.auction.R
import com.`fun`.auction.model.ChargeItem

class ChargeAdapter : BaseQuickAdapter<ChargeItem, BaseViewHolder>(R.layout.charge_item) {

    var selected: Int = 2

    override fun convert(holder: BaseViewHolder, item: ChargeItem) {
        if (holder.adapterPosition == selected) {
            holder.getView<View>(R.id.ll_item).setBackgroundResource(R.drawable.charge_item_check)
        } else {
            holder.getView<View>(R.id.ll_item).setBackgroundResource(R.drawable.charge_item)
        }


        holder.setText(R.id.tv_coin, item.qty.toString())
        if (item.give_qty > 0) {
            holder.setText(R.id.tv_give, "赠送${item.give_qty}")
        } else {
            holder.setText(R.id.tv_give, "")
        }
        holder.setText(R.id.tv_price, "¥ ${item.amount.toInt()}")
    }


    fun setSelect(pos: Int) {
        selected = pos
        notifyDataSetChanged()
    }


    fun getSelectItem(): ChargeItem? {
        if (data.size > selected) {
            return data[selected]
        }
        return null
    }
}