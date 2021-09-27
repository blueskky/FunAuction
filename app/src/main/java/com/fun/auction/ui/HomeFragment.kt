package com.`fun`.auction.ui


import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.`fun`.auction.BuildConfig
import com.`fun`.auction.PrefercencesManager
import com.`fun`.auction.R
import com.`fun`.auction.model.Session
import com.`fun`.auction.model.SocketMessage
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.service.MyService
import com.trello.rxlifecycle4.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeFragment : BaseFragment() {


    companion object {
        fun newInstance(): Fragment {
            return HomeFragment()
        }
    }

    lateinit var box1: BoxFragment
    lateinit var box2: BoxFragment
    lateinit var box3: BoxFragment
    var sessionData: List<Session>? = null
    var myBind: MyService.MyBind? = null
    val messageList = mutableListOf<String>()
    var looper: Boolean = false

    override fun getLayoutId() = R.layout.fragment_home


    override fun init() {
        EventBus.getDefault().register(this)
        tv_rule.setOnClickListener {
            val currentItem = viewpager.currentItem
            sessionData?.let {
                if (currentItem < it.size) {
                    val matchId = it[currentItem].match_id
                    RuleActivity.start(mContext, matchId)
                }
            }

        }

        box1 = BoxFragment.newInstance(0)
        box2 = BoxFragment.newInstance(1)
        box3 = BoxFragment.newInstance(2)

        radio_group.apply {
            setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radio1 -> viewpager.currentItem = 0
                    R.id.radio2 -> viewpager.currentItem = 1
                    R.id.radio3 -> viewpager.currentItem = 2
                }
            }
        }

        viewpager.offscreenPageLimit = 2
        viewpager.apply {
            adapter = object : FragmentPagerAdapter(childFragmentManager, 1) {
                override fun getCount(): Int {
                    return 3
                }

                override fun getItem(position: Int): Fragment {
                    return when (position) {
                        0 -> box1
                        1 -> box2
                        else -> box3
                    }
                }
            }

            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> radio1.isChecked = true
                        1 -> radio2.isChecked = true
                        2 -> radio3.isChecked = true
                    }
                }
            })
        }

        radio1.isChecked = true


        val authorization = PrefercencesManager.getInstance()?.getAuthorization()
        val websocketUrl = "${BuildConfig.SOCKET_HOST}/?token=${HttpManager.token}&jwt=${authorization}"
//        WebSocketManagerKt.init(websocketUrl)

        bindService()

    }

    private fun bindService() {
        mContext.startService(Intent(mContext, MyService::class.java))
        mContext.bindService(Intent(mContext, MyService::class.java), mServiceConnection, BIND_AUTO_CREATE)
    }


    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myBind = service as MyService.MyBind
            val authorization = PrefercencesManager.getInstance()?.getAuthorization()
            val websocketUrl = "${BuildConfig.SOCKET_HOST}/?token=${HttpManager.token}&jwt=${authorization}"
            myBind?.openSocket(websocketUrl)
            Log.d("lhq", "Service  onServiceConnected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }


    override fun initData() {
        HttpManager.api?.home()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            ?.subscribe(object : SimpleObserver<List<Session>>() {
                override fun onSuccess(code: Int, msg: String, data: List<Session>?) {
                    sessionData = data
                    data?.let {
                        if (it.isNotEmpty()) {
                            box1.setData(data[0])
                            radio1.text = data[0].match_name
                        }
                        if (it.size > 1) {
                            box2.setData(data[1])
                            radio2.text = data[1].match_name
                        }
                        if (it.size > 2) {
                            box3.setData(data[2])
                            radio3.text = data[2].match_name
                        }
                    }
                }
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        myBind?.stopSocket()
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onMessageReceived(event: SocketMessage) {
        val content = event.data.content
        messageList.add(content)

        if (!looper) {
            loopMessage()
        }
    }

    fun loopMessage() {
        val iterator = messageList.iterator()
        if (iterator.hasNext()) {
            looper = true
            ll_notice.visibility = View.VISIBLE
            val content = iterator.next()
            val shipAnim = AnimationUtils.loadAnimation(mContext, R.anim.ship_translate)
            tv_message.text = content
            ll_notice.startAnimation(shipAnim)
            iterator.remove()
            shipAnim?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    Log.d("lhq", "onAnimationStart")
                }

                override fun onAnimationEnd(animation: Animation?) {
                    Log.d("lhq", "onAnimationEnd")
                    loopMessage()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    Log.d("lhq", "onAnimationRepeat")
                }
            })
        } else {
            ll_notice.clearAnimation()
            ll_notice.visibility = View.GONE
            looper = false
        }
    }

}