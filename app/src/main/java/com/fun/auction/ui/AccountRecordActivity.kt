package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.ui.adapter.AccountAdapter
import kotlinx.android.synthetic.main.activity_account_record.recyclerview
import kotlinx.android.synthetic.main.common_title.*

class AccountRecordActivity : BaseActivity(){


    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, AccountRecordActivity::class.java))
        }
    }

    override fun getLayoutId()=R.layout.activity_account_record

    override fun init() {
        initTitle(iv_back, tv_title, "转卖中心")


        recyclerview.layoutManager=LinearLayoutManager(mContext)
        val accountAdapter= AccountAdapter()
        recyclerview.adapter=accountAdapter

        accountAdapter.setNewInstance(getData())

    }

}