package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.activity_logistics.*
import kotlinx.android.synthetic.main.common_title.*

class LogisticsCenterActivity : BaseActivity() {


    val titles = arrayListOf("进行中", "已结束")

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, LogisticsCenterActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_logistics

    override fun init() {
        initTitle(iv_back, tv_title, "物流中心")

        val adapter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)
    }


    inner class PagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, 1) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
          return  LogisticsFragment.newInstance(position)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}