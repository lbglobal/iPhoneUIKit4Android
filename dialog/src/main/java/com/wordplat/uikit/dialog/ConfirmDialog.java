package com.wordplat.uikit.dialog;

import android.content.Context;

import com.wordplat.uikit.dialog.R;

/**
 * <p>确认操作 对话框：内容、取消、确定</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class ConfirmDialog extends BaseDialog {

    public ConfirmDialog(Context context) {
        super(context);
    }

    protected int getLayoutResId() {
        return R.layout.dialog_confirm;
    }

    public static ConfirmDialog from(Context context) {
        return new ConfirmDialog(context);
    }
}
