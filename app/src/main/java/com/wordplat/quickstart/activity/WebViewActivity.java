package com.wordplat.quickstart.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wordplat.quickstart.R;
import com.wordplat.uikit.service.DownloadAppService;
import com.wordplat.uikit.splash.AbstractWebViewActivity;

/**
 * <p>WebViewActivity</p>
 * <p>Date: 2017/4/7</p>
 *
 * @author afon
 */

public class WebViewActivity extends AbstractWebViewActivity {
    public static final String ALIPAY_URL = "https://mobilecodec.alipay.com/client_download.htm";

    @Override
    protected int getBackButResId() {
        return R.drawable.btn_nav_back;
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.colorPrimaryDark);
    }

    @Override
    protected String onUrlLoading(WebView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("intent://")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    // forbid launching activities without BROWSABLE
                    // category
                    intent.addCategory("android.intent.category.BROWSABLE");
                    // forbid explicit call
                    intent.setComponent(null);
                    // forbid intent with selector intent
                    intent.setSelector(null);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // start the activity by the intent
                        startActivityIfNeeded(intent, -1);

                        finish();

                        return "";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                clearHistory();

                return ALIPAY_URL;
            }

            if (url.endsWith(".apk")) {
                startService(DownloadAppService.createIntent(this, R.mipmap.ic_launcher, url, "支付宝"));

                return "";
            }
        }

        return url;
    }

    @Override
    protected void onDownload(String url, String mimetype) {
        if (!TextUtils.isEmpty(url)) {
            if (url.endsWith(".apk")) {
                DownloadAppService.createIntent(this, R.mipmap.ic_launcher, url, "支付宝");
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatusBarColor(getBackgroundColor());
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }
}
