package com.fun.auction.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.Status;
import com.fun.auction.R;
import com.fun.auction.util.Utils;

import java.io.File;

public class DownloadDialog extends Dialog {


    private NumberProgressBar progressBar;
    private int downloadId;
    private TextView tvStatus;
    private Activity context;
    private TextView tvInstall;

    private String url;
    private String name;
    private boolean canCancel;
    private TextView tvCancel;

    public DownloadDialog(Activity context) {
        super(context, R.style.dialog_center);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_load_layout);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        progressBar = (NumberProgressBar) findViewById(R.id.progress_bar);
        tvStatus = findViewById(R.id.tv_status);
        tvInstall = findViewById(R.id.tv_install);
        tvCancel = findViewById(R.id.tv_cancel);

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getContext(), config);

        download();

        if (canCancel) {
            tvCancel.setText("取消");
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PRDownloader.cancel(downloadId);
                    dismiss();
                }
            });
        }
    }

    private void download() {
        File filesDir = getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (Status.PAUSED == PRDownloader.getStatus(downloadId)) {
            PRDownloader.resume(downloadId);
            return;
        }
        if (filesDir == null) {
            return;
        }

        downloadId = PRDownloader.download(url, filesDir.getPath(), name)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        tvStatus.setText("正在下载");
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        tvStatus.setText("下载暂停");
                    }
                })
                .setOnCancelListener(new com.downloader.OnCancelListener() {
                    @Override
                    public void onCancel() {
                        tvStatus.setText("下载取消");

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        int progressPercent = (int) (progress.currentBytes * 100 / progress.totalBytes);
                        progressBar.setProgress(progressPercent);

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        tvStatus.setText("下载完成");
                        File file = new File(filesDir, name);
                        Utils.installApk(context, file);

                        tvInstall.setText("安装");
                        tvInstall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File file = new File(filesDir, name);
                                Utils.installApk(context, file);
                            }
                        });
                    }

                    @Override
                    public void onError(Error error) {
                        tvStatus.setText("下载出错");

                        tvInstall.setText("重试");
                        tvInstall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                download();
                            }
                        });
                    }
                });
    }

    public void setDownload(String url, String name, boolean canCancel) {
        this.url = url;
        this.name = name;
        this.canCancel = canCancel;
    }
}
