package com.wordplat.uikit.dialog;

import android.content.Context;

import com.wordplat.uikit.dialog.R;

/**
 * <p>警告 对话框：标题、内容、取消、确定</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class SimpleAlertDialog extends BaseDialog {

    public SimpleAlertDialog(Context context) {
        super(context);
    }

    protected int getLayoutResId() {
        return R.layout.dialog_alert;
    }

    public static SimpleAlertDialog from(Context context) {
        return new SimpleAlertDialog(context);
    }
}
