package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import com.`fun`.auction.R
import com.blankj.utilcode.util.ScreenUtils
import com.youth.banner.util.BannerUtils
import kotlinx.android.synthetic.main.activity_collection_box.*

class CollectionBoxActivity : BaseActivity() {


    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, CollectionBoxActivity::class.java))
        }
    }

    override fun fullScreen(): Boolean {
        return true
    }

    override fun getLayoutId() = R.layout.activity_collection_box

    override fun init() {
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        val params = ll_root.layoutParams as LinearLayout.LayoutParams
        params.height = 1642 * ScreenUtils.getScreenWidth() / 1080
        params.width = 883 * ScreenUtils.getScreenWidth() / 1080
        ll_root.layoutParams = params

    }
}