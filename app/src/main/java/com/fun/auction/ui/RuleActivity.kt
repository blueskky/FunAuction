package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import cn.jzvd.Jzvd
import com.`fun`.auction.R
import com.`fun`.auction.model.HomeRule
import com.`fun`.auction.model.StoreRule
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import kotlinx.android.synthetic.main.activity_rule.*
import kotlinx.android.synthetic.main.common_title.*


class RuleActivity : BaseActivity() {

    companion object {
        fun start(context: Context?, matchId: Int) {
            val intent = Intent(context, RuleActivity::class.java)
            intent.putExtra("mathId", matchId)
            context?.startActivity(intent)
        }
    }

    val mathId by lazy {
        intent.getIntExtra("mathId", 1)
    }

    override fun getLayoutId() = R.layout.activity_rule

    override fun init() {
        initTitle(iv_back, tv_title, "规则")

        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
//        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
//        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
//        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL);

        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT   // 非全屏是横屏
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT     // 进入全屏后是竖屏
    }


    override fun initData() {
        if (mathId == -1) {
            HttpManager.api?.storeRule()
                ?.compose(SchedulersUtils.applySchedulers())
                ?.compose(this.bindToLifecycle())
                ?.subscribe(object : SimpleObserver<StoreRule>() {
                    override fun onSuccess(code: Int, msg: String, data: StoreRule?) {
                        tvRule.text = data?.content
                        videoplayer.setUp(data?.video_url, "")
//                        videoplayer.startPreloading()
                    }
                })
        } else {
            HttpManager.api?.homeRule(mathId)
                ?.compose(SchedulersUtils.applySchedulers())
                ?.compose(this.bindToLifecycle())
                ?.subscribe(object : SimpleObserver<StoreRule>() {
                    override fun onSuccess(code: Int, msg: String, data: StoreRule?) {
                        tvRule.text = data?.content
                        videoplayer.setUp(data?.video_url, "")
//                        videoplayer.startPreloading()
                    }
                })
        }
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }


    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }


}