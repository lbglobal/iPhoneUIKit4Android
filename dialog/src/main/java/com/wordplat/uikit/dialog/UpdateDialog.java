package com.wordplat.uikit.dialog;

import android.content.Context;

import com.wordplat.uikit.dialog.R;

/**
 * <p>更新 对话框</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class UpdateDialog extends BaseDialog {

    public UpdateDialog(Context context) {
        super(context);
    }

    protected int getLayoutResId() {
        return R.layout.dialog_update_normal;
    }

    public static UpdateDialog from(Context context) {
        return new UpdateDialog(context);
    }
}
