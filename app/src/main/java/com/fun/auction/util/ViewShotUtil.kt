package com.`fun`.auction.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.content.FileProvider
import com.`fun`.auction.BuildConfig
import java.io.File
import java.io.FileOutputStream

class ViewShotUtil {

    companion object {

        fun viewShot(view: View): Bitmap? {
            if (view != null && view.width > 0 && view.height > 0) {
                val createBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.RGB_565)
                view.draw(Canvas(createBitmap))
                return createBitmap
            }
            return null
        }


        fun saveBitmap(bitmap: Bitmap, path: String): Boolean {
            var out: FileOutputStream? = null
            try {
                val file = File(path)
                if (!file.exists()) {
                    file.createNewFile()
                }
                out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                out?.close()
            }
            return false
        }


        fun insertMedia(context: Context, path: String?) {
            try {
                val file = File(path)
                if (!file.exists()) {
                    return
                }

                //其次把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);


                //其次把文件插入到系统图库
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+ ".fileprovider", File(path))
                    // 给目标应用一个临时授权
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    uri = Uri.fromFile(File(path))
                }
                intent.data = uri
                context.sendBroadcast(intent)


                // 通知图库更新
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null) { path, uri ->
                        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        mediaScanIntent.data = uri
                        context.sendBroadcast(mediaScanIntent)
                    }
                } else {
                    val relationDir = file.parent
                    val file1 = File(relationDir)
                    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.absoluteFile)))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}