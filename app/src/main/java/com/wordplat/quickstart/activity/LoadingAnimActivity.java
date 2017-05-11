package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.wordplat.quickstart.R;
import com.wordplat.uikit.loading.LoadingDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * <p>LoadingAnimActivity</p>
 * <p>Date: 2017/5/9</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_loadinganim)
public class LoadingAnimActivity extends BaseActivity {

    @ViewInject(R.id.titleView) private SuperTextView titleView = null;
    @ViewInject(R.id.backBut) private TextView backBut = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {
        titleView.setCenterString("Loading anim");
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        LoadingDialog loadingDialog = LoadingDialog.create(mActivity)
                .setCancellable(true);
        loadingDialog.show();
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, LoadingAnimActivity.class);
        return intent;
    }
}
