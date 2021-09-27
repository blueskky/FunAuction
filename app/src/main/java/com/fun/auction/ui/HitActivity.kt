package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import com.`fun`.auction.R
import com.`fun`.auction.model.Goods
import kotlinx.android.synthetic.main.activity_hit.*
import java.io.Serializable

class HitActivity : BaseActivity() {


    companion object {
        fun start(context: Context?, list: MutableList<Goods>, reward: Boolean = false) {
            val intent = Intent(context, HitActivity::class.java)
            intent.putExtra("data", list as Serializable)
            intent.putExtra("reward", reward)
            context?.startActivity(intent)
        }
    }

    override fun fullScreen(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_hit
    }


    val list by lazy {
        intent.getSerializableExtra("data") as MutableList<Goods>
    }
    val reaward by lazy {
        intent.getBooleanExtra("reward", false)
    }


    override fun init() {
        viewpager.offscreenPageLimit =1

        viewpager.isUserInputEnabled = false
        viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return list.size
            }

            override fun createFragment(position: Int): Fragment {
                val goods = list[position]
                return HitFragment.newInstance(goods, position+1)
            }
        }


        tvIndex.visibility = if (list.size > 1) VISIBLE else GONE
        tvIndex.text = "1/${list.size}"

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tvIndex.text = "${position + 1}/${list.size}"
                if (position == list.size - 1) {
                    btn_next.text = "确定"
                } else {
                    btn_next.text = "下一个"
                }
            }
        })

        btn_next.text = if (list.size > 1) "下一个" else "确定"
        btn_next.setOnClickListener {
            var currentItem = viewpager.currentItem

            val findFragmentByTag = supportFragmentManager.findFragmentByTag("f${currentItem}") as BaseFragment
            val pos = findFragmentByTag.pos
            Log.d("lhq","==="+pos)

            if (currentItem < list.size - 1) {
                currentItem++
                viewpager.setCurrentItem(currentItem,false)
            } else {
                CurrentFieldActivity.start(mContext, list, reaward)
                finish()
            }
        }
    }


}

