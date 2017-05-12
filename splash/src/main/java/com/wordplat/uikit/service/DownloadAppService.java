package com.wordplat.uikit.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * <p>下载器。
 * 注意在 Android 6.0 系统以上，需要自己写 SDcard 写入权限的申请逻辑。
 * 在 Android 7.0 系统以上，需要在 AndroidManifest.xml 中定义 provider，否则下载完成后不能自动跳转安装 apk。</p>
 * <p>Date: 2017/4/27</p>
 *
 * @author afon
 */

public class DownloadAppService extends Service {
    private static final String TAG = "DownloadService";

    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K

    ///////////////////////////////////////////////////////////////////////////
    // the notification
    ///////////////////////////////////////////////////////////////////////////
    
    private static int NOTIFICATION_ID = 0;

    private NotificationManager notifyManager;
    private Builder builder;

    private static class NotifyBean {

        private int iconResId;
        private String appName;
        private String appUrl;
        private int notificationId;
        private File apkFile;
        private int progress;

        public int getIconResId() {
            return iconResId;
        }

        public void setIconResId(int iconResId) {
            this.iconResId = iconResId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }

        public int getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(int notificationId) {
            this.notificationId = notificationId;
        }

        public File getApkFile() {
            return apkFile;
        }

        public void setApkFile(File apkFile) {
            this.apkFile = apkFile;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // the download handler
    ///////////////////////////////////////////////////////////////////////////

    private static final int MSG_DOWNLOAD_START = 1;
    private static final int MSG_DOWNLOAD_UPDATE = 2;
    private static final int MSG_DOWNLOAD_END = 3;
    private static final int MSG_DOWNLOAD_FAILED = 4;

    private final Handler downloadHandler;

    private static class DownloadHandler extends Handler {
        private final WeakReference<DownloadAppService> reference;

        public DownloadHandler(DownloadAppService service) {
            reference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            NotifyBean notifyBean = (NotifyBean) msg.obj;

            DownloadAppService service = reference.get();
            if (service == null) {
                return;
            }

            switch (msg.what) {
                case MSG_DOWNLOAD_START:
                    break;
                
                case MSG_DOWNLOAD_UPDATE:
                    service.updateProgress(notifyBean);
                    break;
                
                case MSG_DOWNLOAD_END:
                    File apkFile = notifyBean.getApkFile();
                    service.installAPk(apkFile);
                    service.notifyManager.cancel(notifyBean.getNotificationId());
                    break;

                case MSG_DOWNLOAD_FAILED:
                    service.updateFailed(notifyBean);
                    break;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // the singleton executor service
    ///////////////////////////////////////////////////////////////////////////
    
    private enum ExecutorService {
        INSTANCE;

        private final java.util.concurrent.ExecutorService executorService;

        private ExecutorService() {
            executorService = Executors.newFixedThreadPool(5);
        }

        public void shutdown() {
            if (executorService != null) {
                executorService.shutdown();
            }
        }

        public void execute(Runnable runnable) {
            executorService.execute(runnable);
        }
    }

    public DownloadAppService() {
        downloadHandler = new DownloadHandler(DownloadAppService.this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 已经清除内存之后会回调此方法
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // 清除内存时，后台下载任务也没有了，此时需清除通知栏上的下载进度条
        ExecutorService.INSTANCE.shutdown();
        downloadHandler.removeCallbacksAndMessages(null);

        if (notifyManager != null) {
            notifyManager.cancelAll();
        }
    }

    /**
     * 准备清除内存时会回调此方法
     */
    @Override
    public void onTrimMemory(int level) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ExecutorService.INSTANCE.shutdown();
        downloadHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            onHandleIntent(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    void onHandleIntent(Intent intent) {
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);

        int iconResId = intent.getIntExtra("iconResId", 0);
        final String appName = intent.getStringExtra("appName");
        String appUrl = intent.getStringExtra("appUrl");
        int notificationId = intent.getIntExtra("notificationId", 0);

        final NotifyBean notifyBean = new NotifyBean();
        notifyBean.setIconResId(iconResId);
        notifyBean.setAppName(appName);
        notifyBean.setAppUrl(appUrl);
        notifyBean.setNotificationId(notificationId);

        builder.setContentTitle(appName).setContentText("准备下载...").setSmallIcon(iconResId);
        notifyManager.notify(notifyBean.getNotificationId(), builder.build());

        Log.i(TAG, "##d onHandleIntent: appName = " + appName + ", appUrl = " + appUrl + ", notificationId = " + notificationId);

        ExecutorService.INSTANCE.execute(new Runnable() {
            @Override
            public void run() {
                String appUrl = notifyBean.getAppUrl();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    URL url = new URL(appUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(false);
                    urlConnection.setConnectTimeout(10 * 1000);
                    urlConnection.setReadTimeout(10 * 1000);
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("Charset", "UTF-8");
                    urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

                    urlConnection.connect();
                    long bytetotal = urlConnection.getContentLength();
                    long bytesum = 0;
                    int byteread = 0;
                    in = urlConnection.getInputStream();

                    File dir = new File("/sdcard/Download");
                    if(!dir.exists()) {
                        dir.mkdirs();
                    }

                    String apkName = appUrl.substring(appUrl.lastIndexOf("/") + 1, appUrl.length());
                    File apkFile = new File(dir, apkName);

                    notifyBean.setApkFile(apkFile);

                    out = new FileOutputStream(apkFile);
                    byte[] buffer = new byte[BUFFER_SIZE];

                    int oldProgress = 0;

                    while ((byteread = in.read(buffer)) != -1) {
                        bytesum += byteread;
                        out.write(buffer, 0, byteread);

                        int progress = (int) (bytesum * 100L / bytetotal);
                        // 如果进度与之前进度相等，则不更新，如果更新太频繁，否则会造成界面卡顿
                        if (progress != oldProgress) {
                            notifyBean.setProgress(progress);
                            downloadHandler.obtainMessage(MSG_DOWNLOAD_UPDATE, notifyBean).sendToTarget();
                        }
                        oldProgress = progress;
                    }

                    // 下载完成
                    downloadHandler.obtainMessage(MSG_DOWNLOAD_END, notifyBean).sendToTarget();

                } catch (Exception e) {
                    // 下载错误
                    Log.e(TAG, "##d download apk file error");
                    e.printStackTrace();

                    downloadHandler.obtainMessage(MSG_DOWNLOAD_FAILED, notifyBean).sendToTarget();

                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ignored) {

                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                    }
                }
            }
        });
    }

    private void updateFailed(NotifyBean notifyBean) {
        builder.setContentTitle(notifyBean.getAppName())
                .setContentText("下载失败")
                .setProgress(100, 0, false)
                .setSmallIcon(notifyBean.getIconResId());
        notifyManager.notify(notifyBean.getNotificationId(), builder.build());
    }

    private void updateProgress(NotifyBean notifyBean) {
//        Log.e(TAG, "##d updateProgress: notificationId = " + notificationId + ", progress = " + progress);
        // "正在下载:" + progress + "%"
        builder.setContentTitle(notifyBean.getAppName())
                .setContentText("正在下载: " + notifyBean.getProgress() + "%")
                .setProgress(100, notifyBean.getProgress(), false)
                .setSmallIcon(notifyBean.getIconResId());
        // setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingintent);
        notifyManager.notify(notifyBean.getNotificationId(), builder.build());
    }

    private void installAPk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(getApplicationContext(),
                    getApplicationContext().getPackageName() + ".provider",
                    apkFile);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static Intent createIntent(Context context, int iconResId, String appUrl, String appName) {
        Intent intent = new Intent(context, DownloadAppService.class);
        intent.putExtra("iconResId", iconResId);
        intent.putExtra("appName", appName);
        intent.putExtra("appUrl", appUrl);
        intent.putExtra("notificationId", NOTIFICATION_ID++);
        return intent;
    }
}
