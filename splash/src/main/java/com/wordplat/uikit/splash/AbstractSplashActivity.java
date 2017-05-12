package com.wordplat.uikit.splash;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * <p>启动页</p>
 * <p>Date: 2017/3/17</p>
 *
 * @author afon
 */

public abstract class AbstractSplashActivity extends Activity {

    protected int remainSeconds = 3; // 广告剩余展示时间，单位：秒
    protected int maxSeconds = 3; // 最大获取广告时间，超过这个时间将结束加载，进入下一 Activity，单位：秒

    private static final int MSG_TIMER_START = 1;
    private static final int MSG_TIMER_PERIOD = 2;
    private static final int MSG_TIMER_END = 3;

    protected Activity mActivity;

    private Handler timerHandler;
    private Handler timeOutHandler;

    private ImageView adImage;
    private Button continueBut;

    private boolean showContinueBut;

    protected abstract int getLayoutResId();

    protected abstract int getAdImageResId();

    protected abstract int getContinueButResId();

    protected abstract void onTimeEnd();

    protected void startTimer(boolean showContinueBut) {
        this.showContinueBut = showContinueBut;
        timerHandler.sendEmptyMessage(MSG_TIMER_START);
    }

    protected void endTimer() {
        timerHandler.removeMessages(MSG_TIMER_PERIOD);
        timeOutHandler.removeMessages(MSG_TIMER_END);
    }

    protected void onAdImageStartLoad(ImageView adImage) {
    }

    protected void onTimeRefresh(Button continueBut, int remainSeconds) {
        if (showContinueBut) {
            if (!continueBut.isShown()) {
                continueBut.setVisibility(View.VISIBLE);
            }
            continueBut.setText(String.format("%dS跳过", remainSeconds));
        }
    }

    protected void onAdImageClick(ImageView adImage) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final View decorView = getWindow().getDecorView();
            final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(getLayoutResId());

        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        endTimer();
    }

    private void initUI() {
        timerHandler = new TimerHandler(this);
        timeOutHandler = new TimeOutHandler(this);

        adImage = (ImageView) findViewById(getAdImageResId());
        continueBut = (Button) findViewById(getContinueButResId());

        onAdImageStartLoad(adImage);
        timeOutHandler.sendEmptyMessage(MSG_TIMER_START);

        adImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adImage.getMeasuredHeight() > 0) {
                    onAdImageClick(adImage);
                }
            }
        });
        continueBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeMessages(MSG_TIMER_PERIOD);
                timerHandler.sendEmptyMessage(MSG_TIMER_END);
            }
        });
    }

    private static class TimerHandler extends Handler {

        private final WeakReference<AbstractSplashActivity> reference;

        public TimerHandler(AbstractSplashActivity ativity) {
            reference = new WeakReference<>(ativity);
        }

        @Override
        public void handleMessage(Message msg) {
            AbstractSplashActivity activity = reference.get();
            if (activity == null) {
                return;
            }

            switch (msg.what) {
                case MSG_TIMER_START:
                    activity.timeOutHandler.removeMessages(MSG_TIMER_END);

                    activity.onTimeRefresh(activity.continueBut, activity.remainSeconds);

                    sendEmptyMessageDelayed(MSG_TIMER_PERIOD, 1000);
                    break;

                case MSG_TIMER_PERIOD:
                    activity.remainSeconds--;
                    if (activity.remainSeconds > 0) {
                        activity.onTimeRefresh(activity.continueBut, activity.remainSeconds);
                        sendEmptyMessageDelayed(MSG_TIMER_PERIOD, 1000);
                    } else {
                        sendEmptyMessage(MSG_TIMER_END);
                    }
                    break;

                case MSG_TIMER_END:
                    activity.onTimeEnd();
                    break;
            }
        }
    }

    private static class TimeOutHandler extends Handler {

        private final WeakReference<AbstractSplashActivity> reference;

        public TimeOutHandler(AbstractSplashActivity ativity) {
            reference = new WeakReference<>(ativity);
        }

        @Override
        public void handleMessage(Message msg) {
            AbstractSplashActivity activity = reference.get();
            if (activity == null) {
                return;
            }

            switch (msg.what) {
                case MSG_TIMER_START:
                    sendEmptyMessageDelayed(MSG_TIMER_END, activity.maxSeconds * 1000);
                    break;

                case MSG_TIMER_END:
                    activity.onTimeEnd();
                    break;
            }
        }
    }
}
