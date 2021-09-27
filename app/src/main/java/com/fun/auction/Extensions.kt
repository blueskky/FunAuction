package com.`fun`.auction

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Paint
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.TextView


fun Context.getScreenInfo() {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    wm.defaultDisplay.getMetrics(dm)
    val width = dm.widthPixels // 屏幕宽度（像素）
    val height = dm.heightPixels // 屏幕高度（像素）
    val density = dm.density //屏幕密度（0.75 / 1.0 / 1.5）
    val densityDpi = dm.densityDpi //屏幕密度dpi（120 / 160 / 240）
    //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
    val screenWidth = (width / density).toInt() //屏幕宽度(dp)
    val screenHeight = (height / density).toInt() //屏幕高度(dp)

    Log.d("lhq", "width=" + width)
    Log.d("lhq", "height=" + height)
    Log.d("lhq", "density=" + density)
    Log.d("lhq", "densityDpi=" + densityDpi)
    Log.d("lhq", "screenWidth=" + screenWidth)
}


fun TextView.line() {
    paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG //中划线
}

fun Context.formatTime(tiem: Long): String {
    if (tiem == 0L) {
        return "刚刚"
    }
    val delay = System.currentTimeMillis() - tiem * 1000
    val d = delay / (24 * 3600 * 1000)
    val h = delay / (3600 * 1000)
    val m = delay / (60 * 1000)
    val s = delay / (1000)

    if (d > 0) {
        return "${d}天前"
    }
    if (h > 0) {
        return "${h}小时前"
    }
    if (m > 0) {
        return "${m}分钟前"
    }
    if (s > 0) {
        return "${s}秒钟前"
    }
    return "1秒钟前"
}


fun Context.getVersionCode(): Int {
    try {
        val packageInfo = this.packageManager?.getPackageInfo(packageName, 0);
        return packageInfo?.versionCode ?: 0
    } catch (e: Exception) {
        e.printStackTrace();
    }
    return 0
}


fun Context.getVersionName(): String {
    try {
        val packageInfo = this.packageManager?.getPackageInfo(packageName, 0);
        return packageInfo?.versionName ?: ""
    } catch (e: Exception) {
        e.printStackTrace();
    }
    return ""
}

