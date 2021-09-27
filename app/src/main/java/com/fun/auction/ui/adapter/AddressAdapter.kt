package com.`fun`.auction.ui.adapter

import android.widget.CheckBox
import android.widget.CompoundButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.`fun`.auction.R
import com.`fun`.auction.model.Address

class AddressAdapter : BaseQuickAdapter<Address, BaseViewHolder>(R.layout.adress_item) {

    override fun convert(holder: BaseViewHolder, item: Address) {
        holder.setText(R.id.tv_name, item.name)
        holder.setText(R.id.tv_phone, item.phone)
        holder.setText(R.id.tv_adress, "${item.region} ${item.detail}")

        val checkBox = holder.getView<CheckBox>(R.id.checkbox)
        checkBox.isChecked = item.default

        val checked = checkBox.isChecked
        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (buttonView != null) {
                    if (buttonView?.isPressed) {
                        checkBox.isChecked = checked
                    }
                }
            }
        })
    }
}