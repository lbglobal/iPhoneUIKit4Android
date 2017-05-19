package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.wordplat.quickstart.R;
import com.wordplat.uikit.dialog.ConfirmDialog;
import com.wordplat.uikit.dialog.IKnowDialog;
import com.wordplat.uikit.dialog.MessageDialog;
import com.wordplat.uikit.dialog.PosterDialog;
import com.wordplat.uikit.dialog.SimpleAlertDialog;
import com.wordplat.uikit.dialog.TitleDialog;
import com.wordplat.uikit.dialog.UpdateDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * <p>DialogExampleActivity</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_dialog_example)
public class DialogExampleActivity extends BaseActivity {

    @ViewInject(R.id.titleView) private SuperTextView titleView = null;
    @ViewInject(R.id.backBut) private TextView backBut = null;

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
    }

    @Event(value = {R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item7,
            R.id.item5, R.id.item6, R.id.item8}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.item1:
                ConfirmDialog.from(mActivity)
                        .withText("确认进行此操作吗？")
                        .onActionClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 ConfirmDialog Action_But", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;

            case R.id.item2:
                IKnowDialog.from(mActivity)
                        .withText("世界是我们的，我们是大家的。")
                        .onActionClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 IKnowDialog Action_But", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;

            case R.id.item3:
                MessageDialog.from(mActivity)
                        .withTitle("今日要闻")
                        .withText("特刊评论员李明专程为您报道")
                        .onActionClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 MessageDialog Action_But", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;

            case R.id.item4:
                SimpleAlertDialog.from(mActivity)
                        .withTitle("退出登录")
                        .withText("您的账号将退出登录")
                        .onActionClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 SimpleAlertDialog Action_But", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;

            case R.id.item5:
                TitleDialog.from(mActivity)
                        .withTitle("风险警示承诺书")
                        .withText("请仔细阅读本风险警示书")
                        .onActionClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 TitleDialog Action_But", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;

            case R.id.item6:
                UpdateDialog.from(mActivity)
                        .withUpdateForce(false)
                        .onNoLongerClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 不再提示", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onActionClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 立即更新", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onCancelClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 稍后更新", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .withTitle("发现新版本")
                        .withText("优化内存占用\n新增我的足迹功能")
                        .show();
                break;

            case R.id.item7:
                UpdateDialog.from(mActivity)
                        .withUpdateForce(true)
                        .onActionClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 立即更新", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onCancelClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mActivity, "点击了 退出程序", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .withTitle("发现新版本")
                        .withText("优化内存占用\n新增我的足迹功能\n本版本需强制更新")
                        .show();
                break;

            case R.id.item8:
                PosterDialog.from(mActivity)
                        .show();
                break;
        }
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, DialogExampleActivity.class);
        return intent;
    }
}
