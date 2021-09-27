package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.`fun`.auction.R
import com.`fun`.auction.model.CashConfig
import com.`fun`.auction.model.Event
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import kotlinx.android.synthetic.main.activity_logistics.tablayout
import kotlinx.android.synthetic.main.activity_logistics.viewpager
import kotlinx.android.synthetic.main.activity_sell_center.*
import kotlinx.android.synthetic.main.activity_sell_center.iv_back
import kotlinx.android.synthetic.main.activity_sell_center.tv_title
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.math.BigDecimal

class SellCenterActivity : BaseActivity(), View.OnClickListener {

    val titles = arrayListOf("转卖中", "已结束")

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, SellCenterActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_sell_center

    override fun init() {
        EventBus.getDefault().register(this)
        initTitle(iv_back, tv_title, "转卖中心")
        tv_cash.setOnClickListener(this)
        cash_log.setOnClickListener(this)
        tv_cash_account.setOnClickListener(this)

        val adapter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cash -> CashOutActivity.start(mContext)
            R.id.cash_log -> CashLogActivity.start(mContext)
            R.id.tv_cash_account -> CashAccountActivity.start(mContext)
        }
    }


    override fun initData() {
        HttpManager.api?.getCashConfig()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<CashConfig>() {
                override fun onSuccess(code: Int, msg: String, data: CashConfig?) {
                    when (data?.bingo_type) {
                        -1 -> tv_cash_account.text = "未设置 >"
                        0 -> tv_cash_account.text = data.zfb
                        2 -> tv_cash_account.text = "微信 >"
                    }
                    val amount = BigDecimal((data?.surplus?.toFloat() ?: 0).toString()).divide(BigDecimal("100"))
                    tv_amount.text = String.format("¥ %.2f", amount)
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    fun refresh(event: Event) {
        if (event.type == Event.SElECT_CASH || event.type == Event.CASH_OUT) {
            initData()
        }
    }


    inner class PagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            return SellFragment.newInstance(position)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}