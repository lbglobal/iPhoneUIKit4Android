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
import android.view.View;
import android.view.animation.DecelerateInterpolator;
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

    private RelativeLayout titleView;
    private ImageButton backBut;
    private ProgressBar progressBar;
    private WebView webView;
    private boolean isAnimStart = false;
    private int currentProgress;
    private boolean isGoBack = false; // 是否是返回按下。是则不显示进度条

    protected abstract int getBackButResId();

    protected abstract int getBackgroundColor();

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
        TextView titleTextView = (TextView) findViewById(R.id.titleText);

        backBut.setImageResource(getBackButResId());
        titleView.setBackgroundColor(getBackgroundColor());

        String title = getIntent().getStringExtra(B);
        if(!TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
        } else {
            titleTextView.setText("广告");
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
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true; // 在当前webview内部打开url
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
            isGoBack = true;
            webView.goBack(); // 拦截
            return ;
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
