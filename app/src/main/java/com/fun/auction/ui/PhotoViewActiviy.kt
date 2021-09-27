package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.`fun`.auction.R
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_photo_view_activiy.*
import kotlinx.android.synthetic.main.common_title.*

class PhotoViewActiviy : BaseActivity() {


    val data by lazy {
        intent.getStringArrayListExtra("data")
    }
    val pos by lazy {
        intent.getIntExtra("pos", 0)
    }

    companion object {
        fun start(context: Context?, data: ArrayList<String>, pos: Int) {
            val intent = Intent(context, PhotoViewActiviy::class.java)
            intent.putStringArrayListExtra("data", data)
            intent.putExtra("pos", pos)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_photo_view_activiy

    override fun init() {
        initTitle(iv_back, tv_title, "${pos + 1}/${data?.size}")

        viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                initTitle(iv_back, tv_title, "${position + 1}/${data?.size}")
            }
        })

        viewpager.adapter = object : PagerAdapter() {

            override fun getCount() = data?.size ?: 0

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = LayoutInflater.from(mContext).inflate(R.layout.photo_view, container, false)
                setData(view, position)
                container.addView(view)
                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
    }

    private fun setData(view: View, position: Int) {
        val photoView = view.findViewById<ImageView>(R.id.photoView)
        data?.let {
            val path = it[position]
            Glide.with(mContext).load(path).into(photoView)
        }
        photoView.setOnClickListener {
            onBackPressed()
        }
    }
}