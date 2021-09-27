package com.fun.auction.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.fun.auction.BuildConfig;

import java.io.File;

/**
 * @author:lhq
 * @file: Utils.class
 * @time: 2019/06/20
 * @describe
 */
public class Utils {

    public static void installApk(Activity mContext, File apkfile) {

        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".FileProvider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            //兼容8.0
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 6666);
                }
            }
        } else {
            // 通过Intent安装APK文件
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        }
        if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            mContext.startActivity(intent);
        }
    }

}
