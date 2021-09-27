package com.`fun`.auction.ui

import android.app.ActivityManager
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.`fun`.auction.PrefercencesManager
import com.`fun`.auction.R
import com.`fun`.auction.model.LoginAuth
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.util.ClickUtil
import com.`fun`.auction.util.MyToast
import com.blankj.utilcode.util.RegexUtils
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.observers.DefaultObserver
import kotlinx.android.synthetic.main.activity_regiest.*
import okhttp3.ResponseBody
import java.util.*

class PhoneLoginActivity : BaseActivity(), View.OnClickListener {

    val user_policy = "file:///android_asset/user_policy.html"
    var timer: Timer? = null
    private val rule: Spanned by lazy {
        Html.fromHtml("<font color='#8a8cc9'>未注册的手机号验证后将自动创建趣拍拍账号，登录即代表您已经同意</font> <font color='#ffffffff'>《趣拍拍用户协议》</font>")
    }
    var invite_id: String? = null

    override fun initStatusBar() {
        ImmersionBar.with(this)
            //使用该属性,必须指定状态栏颜色
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_4C4899)
            .keyboardEnable(true)
            .init()
    }

    override fun getLayoutId() = R.layout.activity_regiest


    override fun init() {
        iv_back.setOnClickListener {
            onBackPressed()
        }


        val spanString = SpannableStringBuilder(rule)
        spanString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                WebViewActivity.start(mContext, "用户协议", user_policy)
            }
        }, rule.length - 9, rule.length, 0)

        //设置部分文字颜色
        val foregroundColorSpan2 = ForegroundColorSpan(Color.parseColor("#ffffff"))
        spanString.setSpan(foregroundColorSpan2, rule.length - 9, rule.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tv_policy.movementMethod = LinkMovementMethod.getInstance()
        tv_policy.text = spanString

        tv_get_code.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        tv_other_login.setOnClickListener(this)

        val account = PrefercencesManager.getInstance()?.getAccount()
        if (!TextUtils.isEmpty(account)) {
            et_phone.setText(account)
        }

        window.decorView.post { getClipboardContent() }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_get_code -> getSmsCode()
            R.id.btn_login -> login()
            R.id.tv_other_login -> {
                start(mContext, EasyLoginActivity::class.java)
                finish()
            }
        }
    }

    private fun login() {
        if (ClickUtil.isFastClick()) {
            return
        }
        if (!checkbox.isChecked) {
            MyToast.show("请勾选用户协议")
            return
        }


        val phone = et_phone.text.toString().trim()
        if (TextUtils.isEmpty(phone)) {
            MyToast.show("请输入手机号")
            return
        }
        if (!RegexUtils.isMobileSimple(phone)) {
            MyToast.show("请输入有效手机号")
            return
        }
        val txtCode = et_code.text.toString().trim()
        if (TextUtils.isEmpty(txtCode)) {
            MyToast.show("请输入验证码")
            return
        }
        val map = mutableMapOf<String, String?>()
        map["account"] = phone
        map["smsCode"] = txtCode
        if (invite_id != null) {
            map["invite_id"] = invite_id
        }

        HttpManager.api?.login(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<LoginAuth>() {
                override fun onSuccess(code: Int, msg: String, data: LoginAuth?) {
                    if (data != null) {
                        PrefercencesManager.getInstance()?.setAuthorization(data.access_token)
                        PrefercencesManager.getInstance()?.setAccount(data.account)
                        PrefercencesManager.getInstance()?.setPwd(data.password)
                        PrefercencesManager.getInstance()?.setUserId(data.user_id)
                        if (!mainRuning()) {
                            start(mContext, MainActivity::class.java)
                        }
                        finish()
                    }
                }
            })

    }


    private fun getSmsCode() {
        if (ClickUtil.isFastClick()) {
            return
        }
        val phone = et_phone.text.toString().trim()
        if (TextUtils.isEmpty(phone)) {
            MyToast.show("请输入手机号")
            return
        }
        if (!RegexUtils.isMobileSimple(phone)) {
            MyToast.show("请输入有效手机号")
            return
        }

        HttpManager.api?.smsCode(phone)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<Any>() {
                override fun onSuccess(code: Int, msg: String, data: Any?) {
                    MyToast.show(msg)
                    countDown()
                }
            })
    }

    private fun countDown() {
        tv_get_code.isEnabled = false
        var millis = 60
        timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    millis--
                    tv_get_code.text = millis.toString()
                    if (millis == 0) {
                        tv_get_code.isEnabled = true
                        tv_get_code.text = "获取验证码"
                        timer?.cancel()
                    }
                }
            }
        }
        timer?.schedule(timerTask, 0, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }


    private fun getImgVerify() {
        val phone = et_phone.text.toString().trim()
        if (TextUtils.isEmpty(phone)) {
            MyToast.show("请输入手机号")
            return
        }
        if (!RegexUtils.isMobileSimple(phone)) {
            MyToast.show("请输入有效手机号")
            return
        }

        HttpManager.api?.imgVerify(phone)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : DefaultObserver<ResponseBody?>() {
                override fun onNext(responseBody: @NonNull ResponseBody?) {
                    val inputStream = responseBody?.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                }

                override fun onError(e: @NonNull Throwable?) {

                }

                override fun onComplete() {

                }
            })
    }


    fun mainRuning(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = am.getRunningTasks(10)
        if (!runningTasks.isNullOrEmpty()) {
            for (task in runningTasks) {
                val baseActivity = task.baseActivity
                if (baseActivity != null && baseActivity.equals("com.world.exchange.ui.MainActivity")) {
                    return true
                }
            }
        }
        return false
    }


    fun getClipboardContent() {
        try {
            val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val data = cm.primaryClip
            if (data != null) {
                val item = data.getItemAt(0)
                if (item != null && item.text != null) {
                    val content = item.text.toString()
                    //对剪贴板文字的操作
                    val parseMap: HashMap<String, String> = parse(content)
                    invite_id = parseMap["invite_id"]
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun parse(content: String): HashMap<String, String> {
        val map = HashMap<String, String>()
        try {
            if (!TextUtils.isEmpty(content) && content.contains("?")) {
                val substring = content.substring(content.lastIndexOf("?") + 1)
                if (!TextUtils.isEmpty(substring)) {
                    val split = substring.split("&".toRegex()).toTypedArray()
                    if (split != null && split.isNotEmpty()) {
                        for (str in split) {
                            if (!TextUtils.isEmpty(str) && str.contains("=")) {
                                val value = str.split("=".toRegex()).toTypedArray()
                                if (value.size > 1) {
                                    map[value[0]] = value[1]
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return map
    }


    override fun onBackPressed() {
        if (!mainRuning()) {
            start(mContext, MainActivity::class.java)
        }
        finish()
    }

}