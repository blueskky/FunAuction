package com.`fun`.auction

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.`fun`.auction.model.PeekChangeEvent
import com.`fun`.auction.model.PeekIng
import com.`fun`.auction.model.User
import com.`fun`.auction.util.MyToast
import com.baidu.mapapi.SDKInitializer
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.tencent.bugly.crashreport.CrashReport
import org.greenrobot.eventbus.EventBus
import java.util.logging.Handler

class MyApplication : Application() {

    companion object {
        lateinit var mContext: Context
        var peekList = mutableListOf<PeekIng>()

        fun addPeek(peekIng: PeekIng) {
            val contain = isInPeeking(peekIng.product_id)
            if (contain != null) {
                contain.current_price = peekIng.current_price
            } else {
                peekList.add(peekIng)
            }
            android.os.Handler().postDelayed(Runnable {
                removePeek(peekIng)
            }, 60 * 1000)

            EventBus.getDefault().post(PeekChangeEvent())
        }

        private fun removePeek(peekIng: PeekIng) {
            peekList.remove(peekIng)
            EventBus.getDefault().post(PeekChangeEvent())
        }


        fun isInPeeking(productId: Int?): PeekIng? {
            val iterator = peekList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.product_id == productId) {
                    return next
                }
            }
            return null
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        MyToast.init(this)
        PrefercencesManager.init(this)
        mContext = this

        SDKInitializer.initialize(this)
        CrashReport.initCrashReport(applicationContext, "4d1c2601e8", false)
    }


    init {

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, R.color.purple_200, R.color.purple_500)
            MaterialHeader(context)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            val ballPulseFooter = BallPulseFooter(this)
            ballPulseFooter.spinnerStyle = SpinnerStyle.Translate
            ballPulseFooter
        }
    }


}