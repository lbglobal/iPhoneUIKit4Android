package com.wordplat.quickstart.activity;

import android.os.Bundle;

import com.wordplat.quickstart.R;
import com.wordplat.uikit.splash.AbstractSplashActivity;

/**
 * <p>SplashActivity</p>
 * <p>Date: 2017/5/12</p>
 *
 * @author afon
 */

public class SplashActivity extends AbstractSplashActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getAdImageResId() {
        return R.id.adImage;
    }

    @Override
    protected int getContinueButResId() {
        return R.id.continueBut;
    }

    @Override
    protected void onTimeEnd() {
        startActivity(MainActivity.createIntent(mActivity));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remainSeconds = 1;
        startTimer(false);
    }
}
