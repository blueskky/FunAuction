package com.`fun`.auction.ui

import android.media.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.`fun`.auction.R
import com.`fun`.auction.model.Goods
import kotlinx.android.synthetic.main.fragment_hit.*


class HitFragment : BaseFragment() {

    var buttonRingTone: Ringtone? = null
    var mSoundPoll: SoundPool? = null

    companion object {
        fun newInstance(goods: Goods?, position: Int): HitFragment {
            val arg = Bundle()
            arg.putSerializable("goods", goods)
            arg.putInt("position", position)
            val fragment = HitFragment()
            fragment.arguments = arg
            return fragment
        }
    }


    override val pos: Int? by lazy {
        arguments?.getInt("position", 0)

    }


    override fun getLayoutId() = R.layout.fragment_hit

    override fun init() {
        val goods = arguments?.getSerializable("goods") as Goods?

        if (goods != null) {

            tvTitle.text = "恭喜你\n抽中${goods.goods_name}"
            Glide.with(mContext.applicationContext)
                .load(goods.goods_image)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .into(iv_cover)
        }
    }

    override fun onResume() {
        super.onResume()
        iv_cover.visibility = View.VISIBLE
        val animation: Animation = AnimationUtils.loadAnimation(mContext.applicationContext, R.anim.scale)
        iv_cover.startAnimation(animation) //開始动画
        playSound2()
    }


    private fun playSound() {
        if (buttonRingTone == null) {
            val path =
                "android.resource://" + mContext.packageName.toString() + "/" + R.raw.open_box
            val uri = Uri.parse(path)
            buttonRingTone = RingtoneManager.getRingtone(mContext.applicationContext, uri)
        }
        if (!buttonRingTone!!.isPlaying) {
            buttonRingTone?.play()
        }
    }


    private fun playSound2() {
        //当前系统的SDK版本大于等于21(Android 5.0)时
        if (mSoundPoll == null) {
            if (Build.VERSION.SDK_INT > 21) {
                //设置描述音频流信息的属性
                val abs = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                mSoundPoll = SoundPool.Builder()
                    .setMaxStreams(10) //设置允许同时播放的流的最大值
                    .setAudioAttributes(abs) //完全可以设置为null
                    .build()

            } else {
                mSoundPoll = SoundPool(10, AudioManager.STREAM_MUSIC, 0)
            }
        }
        val soundId = mSoundPoll?.load(mContext, R.raw.open_box, 1)
        mSoundPoll?.setOnLoadCompleteListener { _, _, _ ->
            mSoundPoll?.play(soundId!!, 1f, 1f, 1, 0, 1f)
        }
    }

    override fun onPause() {
        super.onPause()
        iv_cover.visibility = View.GONE
    }
}