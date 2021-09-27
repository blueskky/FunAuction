package com.`fun`.auction.ui


import android.media.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.`fun`.auction.R
import com.`fun`.auction.model.Goods
import com.`fun`.auction.model.PrizeMessage
import com.`fun`.auction.model.Session
import com.`fun`.auction.model.Task
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.BoxBannerAdapter
import com.`fun`.auction.ui.adapter.PrizeBannerAdapter
import com.`fun`.auction.util.MyToast
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.fragment_box.*
import kotlinx.android.synthetic.main.fragment_box.banner


class BoxFragment : BaseFragment(), View.OnClickListener {

    var boxAnim: Animation? = null
    var ringTone: Ringtone? = null
    var mSoundPoll: SoundPool? = null
    var mathId: Int? = null
    val type by lazy { arguments?.getInt("type") }

    val level1 = intArrayOf(R.drawable.level1_1, R.drawable.level1_2, R.drawable.level1_3)
    val level2 = intArrayOf(R.drawable.level2_1, R.drawable.level2_2, R.drawable.level2_3)
    val level3 = intArrayOf(R.drawable.level3_1, R.drawable.level3_2, R.drawable.level3_3)
    var resIndex: Int = 0


    companion object {
        fun newInstance(position: Int): BoxFragment {
            val arg = Bundle()
            arg.putInt("type", position)
            val fragment = BoxFragment()
            fragment.arguments = arg
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_box


    override fun init() {
        boxAnim = AnimationUtils.loadAnimation(mContext, R.anim.box_translate)
        iv_box.startAnimation(boxAnim)

        ll_pat1.setOnClickListener(this)
        ll_pat10.setOnClickListener(this)
        iv_charge.setOnClickListener(this)
        iv_collection.setOnClickListener(this)
        ll_reward.setOnClickListener(this)
        iv_box.setOnClickListener(this)
        Glide.with(mContext).load(R.drawable.window_normal).into(iv_window)

        iv_box.setImageResource(getRes())

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_pat1 -> singleDraw() //单拍
            R.id.ll_pat10 -> moreDraw()  //连拍
            R.id.iv_charge -> ChargeActivity.start(mContext)

            R.id.ll_reward -> {
                val id = mathId
                if (id == null) {
                    MyToast.show("未获取到场次信息")
                    return
                }
                ReceiveRewardActivity.start(mContext, id)
                activity?.overridePendingTransition(R.anim.vcertical_in, R.anim.vcertical_out);
            }

            R.id.iv_collection -> CollectionBoxActivity.start(mContext)

            R.id.iv_box -> {
                iv_box.setImageResource(getRes())
            }
        }
    }

    private fun moreDraw() {
        val id = mathId
        if (id == null) {
            MyToast.show("未获取到场次信息")
            return
        }
        ll_pat1.isEnabled = false
        ll_pat10.isEnabled = false
        iv_box.setImageResource(getRes())
        val map = mapOf("match_id" to id)
        HttpManager.api?.moreDraw(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<MutableList<Goods>>() {
                override fun onSuccess(code: Int, msg: String, data: MutableList<Goods>?) {
                    if (!data.isNullOrEmpty()) {
                        openHitActivity(data)
                        //palySound()
                        playSound2()

                        getTaskNum()
                    }
                }

                override fun onFail(code: Int, msg: String) {
                    MyToast.show(msg)
                    ll_pat1.isEnabled = true
                    ll_pat10.isEnabled = true
                }
            })

    }

    private fun singleDraw() {
        val id = mathId
        if (id == null) {
            MyToast.show("未获取到场次信息")
            return
        }

        ll_pat1.isEnabled = false
        ll_pat10.isEnabled = false
        iv_box.setImageResource(getRes())
        val map = mapOf("match_id" to id)
        HttpManager.api?.singleDraw(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Goods>() {
                override fun onSuccess(code: Int, msg: String, data: Goods?) {
                    if (data != null) {
                        var list = mutableListOf(data)
                        openHitActivity(list)
                        //palySound()
                        playSound2()

                        getTaskNum()
                    }
                }

                override fun onFail(code: Int, msg: String) {
                    MyToast.show(msg)
                    ll_pat1.isEnabled = true
                    ll_pat10.isEnabled = true
                }
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        boxAnim?.cancel()
    }


    private fun openHitActivity(list: MutableList<Goods>) {
        Glide.with(mContext).asGif()
            .load(R.drawable.window_active)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(iv_window)

        Handler().postDelayed(Runnable {
            HitActivity.start(mContext, list)
        }, 3360)

        Handler().postDelayed(Runnable {
            ll_pat1.isEnabled = true
            ll_pat10.isEnabled = true
            Glide.with(mContext).asGif()
                .load(R.drawable.window_normal)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv_window)

        }, 4000)
    }


    private fun palySound() {
        if (ringTone == null) {
            val path = "android.resource://" + mContext!!.packageName.toString() + "/" + R.raw.box_active
            val uri = Uri.parse(path)
            ringTone = RingtoneManager.getRingtone(mContext!!.applicationContext, uri)
        }
        ringTone?.play()
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
        val soundId = mSoundPoll?.load(mContext, R.raw.box_active, 1)
        mSoundPoll?.setOnLoadCompleteListener { _, _, _ ->
            mSoundPoll?.play(soundId!!, 1f, 1f, 1, 0, 1f)
        }
    }


    fun setData(session: Session) {
        if (isRuning) {
            mathId = session.match_id
            tv_single_price.text = session.single_price.toString()
            tv_more_price.text = session.more_price.toString()

            val goods = session.goods
            setTopBanner(goods)

            getPrizeMessage(session.match_id)

            getTaskNum()
        }
    }

    private fun getTaskNum() {
        mathId?.let {
            HttpManager.api?.taskNum(it)
                ?.compose(SchedulersUtils.applySchedulers())
                ?.compose(this.bindToLifecycle())
                ?.subscribe(object : SimpleObserver<Task>() {
                    override fun onSuccess(code: Int, msg: String, data: Task?) {
                        tv_num.text = "${data?.num}/${data?.limit}"
                    }
                })
        }
    }

    private fun getPrizeMessage(matchId: Int) {
        HttpManager.api?.homeMessage(matchId)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<List<PrizeMessage>>() {
                override fun onSuccess(code: Int, msg: String, data: List<PrizeMessage>?) {
                    if (data!=null) {
                        setBottomBanner(data)
                    }
                }
            })
    }

    private fun setBottomBanner(data: List<PrizeMessage>) {
        banner_bot.adapter = PrizeBannerAdapter(mContext, data)
        banner_bot.addBannerLifecycleObserver(this)
        banner_bot.setLoopTime(4000)
//        banner_bot.setPageTransformer(ScaleInTransformer())
    }

    private fun setTopBanner(goods: MutableList<Goods>?) {
        goods?.let {
            val boxBannerAdapter = BoxBannerAdapter(mContext, it)
            banner.adapter = boxBannerAdapter
            banner.addBannerLifecycleObserver(this)
            banner.setLoopTime(4000)
//            banner.setPageTransformer(ScaleInTransformer())

            banner.setOnBannerListener(object : OnBannerListener<Goods> {
                override fun OnBannerClick(data: Goods?, position: Int) {
                    RankActivity.start(mContext, goods)
                }
            })
        }
    }


    fun getRes(): Int {
        val i = resIndex % 3
        resIndex++
        return when (type) {
            0 -> level1[i]
            1 -> level2[i]
            else -> level3[i]
        }
    }
}