package com.wordplat.uikit.dialog;

import android.content.Context;

import com.wordplat.uikit.dialog.R;

/**
 * <p>消息 对话框：标题、内容、取消、查看</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class MessageDialog extends BaseDialog {

    public MessageDialog(Context context) {
        super(context);
    }

    protected int getLayoutResId() {
        return R.layout.dialog_message;
    }

    public static MessageDialog from(Context context) {
        return new MessageDialog(context);
    }
}
