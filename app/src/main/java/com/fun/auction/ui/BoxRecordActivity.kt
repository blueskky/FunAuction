package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.activity_box_record.*
import kotlinx.android.synthetic.main.common_title.*

class BoxRecordActivity : BaseActivity() {

    val titles = arrayListOf("待处理", "已处理")

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, BoxRecordActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_box_record

    override fun init() {
        initTitle(iv_back, tv_title, "宝盒记录")

        val adapter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)
    }


    inner class PagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, 1) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            return BoxRecordFragment.newInstance(position)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}