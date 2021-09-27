package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.activity_box_record.*
import kotlinx.android.synthetic.main.common_title.*

class PatRecordActivity : BaseActivity() {

    val titles = arrayListOf("参与中", "拍成功", "已结束")

    companion object {
        fun start(context: Context?, i: Int = 0) {
            val intent = Intent(context, PatRecordActivity::class.java)
            intent.putExtra("position", i)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_pat_record

    override fun init() {
        initTitle(iv_back, tv_title, "拍拍记录")

        val adapter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)
        viewpager.offscreenPageLimit = 3

        val intExtra = intent.getIntExtra("position", 0)
        viewpager.setCurrentItem(intExtra, false)
    }


    inner class PagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, 1) {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                return PatRecordFragment.instance()
            } else {
                return PatRecordFragment2.instance(position)
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }


}