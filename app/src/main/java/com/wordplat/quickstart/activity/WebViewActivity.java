package com.wordplat.quickstart.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wordplat.quickstart.R;
import com.wordplat.uikit.splash.AbstractWebViewActivity;

/**
 * <p>WebViewActivity</p>
 * <p>Date: 2017/4/7</p>
 *
 * @author afon
 */

public class WebViewActivity extends AbstractWebViewActivity {

    @Override
    protected int getBackButResId() {
        return R.drawable.btn_nav_back;
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.colorPrimaryDark);
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
