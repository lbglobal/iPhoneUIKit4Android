package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.wordplat.quickstart.R;
import com.wordplat.uikit.loading.NetworkEmptyLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * <p>NetworkEmptyLayoutExampleActivity</p>
 * <p>Date: 2017/5/12</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_network_emptylayout_example)
public class NetworkEmptyLayoutExampleActivity extends BaseActivity {

    @ViewInject(R.id.titleView) private SuperTextView titleView = null;
    @ViewInject(R.id.backBut) private TextView backBut = null;

    @ViewInject(R.id.emptyLayout) private NetworkEmptyLayout emptyLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {
        titleView.setCenterString("Dialog example");
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        emptyLayout.setEmptyRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.showLoading();
                emptyLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyLayout.showEmpty();
                    }
                }, 3000);
            }
        });

        emptyLayout.showLoading();
        emptyLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLayout.showError();
            }
        }, 3000);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, NetworkEmptyLayoutExampleActivity.class);
        return intent;
    }
}
