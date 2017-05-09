package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wordplat.quickstart.R;
import com.wordplat.uikit.loading.LoadingDialog;

import org.xutils.view.annotation.ContentView;

/**
 * <p>LoadingAnimActivity</p>
 * <p>Date: 2017/5/9</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_loadinganim)
public class LoadingAnimActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadingDialog loadingDialog = LoadingDialog.create(mActivity).setCancellable(true);

        loadingDialog.show();
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, LoadingAnimActivity.class);
        return intent;
    }
}
