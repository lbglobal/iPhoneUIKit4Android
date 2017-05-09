package com.wordplat.quickstart.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.wordplat.quickstart.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event(value = {R.id.item1, R.id.item2, R.id.item3,
            R.id.item4, R.id.item5}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.item1:
                startActivity(LoadingAnimActivity.createIntent(mActivity));
                break;

            case R.id.item2:
                Toast.makeText(mActivity, "项目二", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item3:
                Toast.makeText(mActivity, "项目三", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item4:
                Toast.makeText(mActivity, "项目四", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item5:
                Toast.makeText(mActivity, "项目五", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 点击两次退出
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
