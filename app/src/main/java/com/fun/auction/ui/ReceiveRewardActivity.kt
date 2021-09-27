package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.`fun`.auction.R
import com.`fun`.auction.model.Goods
import com.`fun`.auction.model.Task
import com.`fun`.auction.model.TaskModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.RewardAdapter
import com.`fun`.auction.util.MyToast
import kotlinx.android.synthetic.main.activity_receive_reward.*


class ReceiveRewardActivity : BaseActivity() {

    companion object {
        fun start(context: Context, mathId: Int) {
            val intent = Intent(context, ReceiveRewardActivity::class.java)
            intent.putExtra("mathId", mathId)
            context.startActivity(intent)
        }
    }

    var rewardAdapter: RewardAdapter? = null
    var mathId: Int = 0
    override fun getLayoutId() = R.layout.activity_receive_reward

    override fun init() {
        mathId = intent.getIntExtra("mathId", 0)
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        recyclerview.layoutManager = GridLayoutManager(mContext, 5)
        rewardAdapter = RewardAdapter()
        recyclerview.adapter = rewardAdapter
        rewardAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = rewardAdapter?.getItem(position)
                if (item != null) {
                    if (item.activate()) {
                        if (item.received()) {
                            MyToast.show("您已经领取过啦")
                        } else {
                            taskAward(position)
                        }
                    } else {
                        MyToast.show("暂未获取奖励资格，快去拍拍吧")
                    }
                }
            }
        })

        iv_close.setOnClickListener {
            onBackPressed()
        }
    }

    private fun taskAward(position: Int) {
        val map = mutableMapOf<String, Int>()
        map["match_id"] = mathId
        map["type"] = position + 1
        HttpManager.api?.taskAward(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<TaskModel>() {
                override fun onSuccess(code: Int, msg: String, data: TaskModel?) {
                    if (data != null) {
                        rewardAdapter?.let {
                            it.data[position].award = 0
                            it.notifyDataSetChanged()
                        }

                        data.prize?.let {
                            var list = mutableListOf(it)
                            HitActivity.start(mContext, list,true)
                        }
                    }
                }
            })
    }

    override fun initData() {
        HttpManager.api?.taskList(mathId)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<TaskModel>() {
                override fun onSuccess(code: Int, msg: String, data: TaskModel?) {
                    val jd = data?.jd
                    if (!jd.isNullOrEmpty()) {
                        val list = mutableListOf<Task>()
                        for (task in jd.values) {
                            list.add(task)
                        }
                        rewardAdapter?.setNewInstance(list)

                        val last = list.last()
                        tvNum.text = "${last.num}次"
                    }
                }
            })
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.vcertical_in, R.anim.vcertical_out)
    }
}