package com.`fun`.auction.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.`fun`.auction.Constants
import com.`fun`.auction.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.ByteArrayOutputStream


class ShareHelper(context: Context) {

    var createWXAPI: IWXAPI? = null

    init {
        createWXAPI = WXAPIFactory.createWXAPI(context, null)
        createWXAPI?.registerApp(Constants.WX_APP_ID)
    }

    fun share(context: Context, title: String, desc: String, imgUrl: String, url: String) {
        //初始化一个WXWebpageObject，填写url

        Glide.with(context).asBitmap().load(imgUrl).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val bitmap2Bytes = bitmap2Bytes(resource, 30)

                val webpage = WXWebpageObject()
                webpage.webpageUrl = url


                //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
                val msg = WXMediaMessage(webpage)
                msg.title = title
                msg.description = desc
                msg.thumbData = bitmap2Bytes


                //构造一个Req
                val req = SendMessageToWX.Req()
                req.transaction = "share"
                req.message = msg
                req.scene = SendMessageToWX.Req.WXSceneSession
                /*req.userOpenId = getOpenId()*/

                //调用api接口，发送数据到微信
                createWXAPI?.sendReq(req)

            }
        })
    }


    fun bitmap2Bytes(bitmap: Bitmap, maxkb: Int): ByteArray? {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        var options = 100
        while (output.toByteArray().size / 1024 > maxkb) {
            if (options == 2) {
                break
            }
            var k = if (options <= 10) 2 else 10
            options -= k
            output.reset() //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output) //这里压缩options%，把压缩后的数据存放到output中
        }
        Log.d("lhq", "size=" + output.toByteArray().size + " options=" + options)
        return output.toByteArray()
    }

}