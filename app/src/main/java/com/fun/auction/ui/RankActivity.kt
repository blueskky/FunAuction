package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.model.Goods
import com.`fun`.auction.ui.adapter.RankAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_rank.*
import java.io.Serializable

class RankActivity : BaseActivity() {


    companion object {
        fun start(context: Context?, mDatas: MutableList<Goods>) {
            val intent = Intent(context, RankActivity::class.java)
            intent.putExtra("data", mDatas as Serializable)
            context?.startActivity(intent)
        }
    }


    override fun getLayoutId() = R.layout.activity_rank
    override fun init() {

        val list = intent.getSerializableExtra("data") as MutableList<Goods>

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        recyclerview.layoutManager = LinearLayoutManager(mContext)

        val rankAdapter = RankAdapter(list)
        recyclerview.adapter = rankAdapter

        rankAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = rankAdapter.getItem(position)
//                GoodsDetailActivity.start(mContext, item.match_goods_id)
            }
        })

        iv_close.setOnClickListener {
            onBackPressed()
        }
    }

}