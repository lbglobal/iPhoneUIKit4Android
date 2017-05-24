package com.wordplat.uikit.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wordplat.uikit.splash.R;

/**
 * <p>AbstractWebViewActivity</p>
 * <p>Date: 2017/5/12</p>
 *
 * @author afon
 */

public abstract class AbstractWebViewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AbstractWebViewActivity";

    protected WebView webView;
    private RelativeLayout titleView;
    private ImageButton backBut;
    private ProgressBar progressBar;
    private TextView titleTextView;

    private boolean isAnimStart = false;
    private int currentProgress;
    private boolean isGoBack = false; // 是否是返回按下。是则不显示进度条
    private boolean needClearHistory = false; // 是否需要清除历史记录

    protected abstract int getBackButResId();

    protected abstract int getBackgroundColor();

    protected abstract String onUrlLoading(WebView view, String url);

    protected abstract void onDownload(String url, String mimetype);

    protected void clearHistory() {
        needClearHistory = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_abstract_webview);
        initUI();

        setUpWebView();
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webView);

        titleView = (RelativeLayout) findViewById(R.id.titleView);
        backBut = (ImageButton) findViewById(R.id.backBut);
        titleTextView = (TextView) findViewById(R.id.titleText);

        backBut.setImageResource(getBackButResId());
        titleView.setBackgroundColor(getBackgroundColor());

        String title = getIntent().getStringExtra(B);
        if(!TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
        }

        backBut.setOnClickListener(this);
    }

    private void setUpWebView() {
        // 拦截url
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(!isGoBack) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setAlpha(1.0f);
                }
                isGoBack = false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String originUrl) {
                String url = onUrlLoading(view, originUrl);
                if (!TextUtils.isEmpty(url)) {
                    // 在当前webview内部打开url
                    webView.loadUrl(url);

                    WebView.HitTestResult hit = webView.getHitTestResult();
                    int hitType = hit.getType();

                    if (hitType != WebView.HitTestResult.UNKNOWN_TYPE) {
                        // 这里执行自定义的操作
                        return true;
                    } else{
                        // 重定向时 hitType 为0 ,执行默认的操作
                        return false;
                    }
                }

                return true;
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                if (needClearHistory) {
                    needClearHistory = false;
                    webView.clearHistory();
                }
            }
        });

        // 获取网页加载进度
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                currentProgress = progressBar.getProgress();
                if (newProgress >= 100 && !isAnimStart) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    progressBar.setProgress(newProgress);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(progressBar.getProgress());
                } else {
                    // 开启属性动画让进度条平滑递增
                    startProgressAnimation(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleTextView.setText(title);
            }
        });

        // 下载文件
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                onDownload(url, mimetype);
            }
        });

        // 支持js
        webView.getSettings().setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra(A);

        if(!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    /**
     * progressBar递增动画
     */
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    /**
     * progressBar消失动画
     */
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(progressBar, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator()); // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction(); // 0.0f ~ 1.0f
                int offset = 100 - progress;
                progressBar.setProgress((int) (progress + offset * fraction));
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                progressBar.setProgress(0);
                progressBar.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBut) {
            onBackPressed();
        }
    }

    /**
     * 监听back键
     * 在WebView中回退导航
     */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            Log.i(TAG, "##d onBackPressed: goBack");
            isGoBack = true;
            webView.goBack(); // 拦截
            return ;
        } else {
            Log.i(TAG, "##d onBackPressed: normal");
        }
        super.onBackPressed(); // 放行

        if (backCallback != null) {
            backCallback.onBackPressed();
        }
    }

    private static final String A = "a";
    private static final String B = "b";

    public static <E> Intent createIntent(Context context, Class<E> clz, String url) {
        Intent intent = new Intent(context, clz);
        intent.putExtra(A, url);
        return intent;
    }

    public static <E> Intent createIntent(Context context, Class<E> clz, String url, String title) {
        Intent intent = new Intent(context, clz);
        intent.putExtra(A, url);
        intent.putExtra(B, title);
        return intent;
    }

    private static BackCallback backCallback;

    public static void setBackCallback(BackCallback backCallback) {
        AbstractWebViewActivity.backCallback = backCallback;
    }

    public interface BackCallback {

        void onBackPressed();
    }
}
