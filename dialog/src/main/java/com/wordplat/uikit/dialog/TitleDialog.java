package com.wordplat.uikit.dialog;

import android.content.Context;

import com.wordplat.uikit.dialog.R;

/**
 * <p>风险警示 对话框：标题、内容、知道了</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class TitleDialog extends BaseDialog {

    public TitleDialog(Context context) {
        super(context);
    }

    protected int getLayoutResId() {
        return R.layout.dialog_title;
    }

    public static TitleDialog from(Context context) {
        return new TitleDialog(context);
    }
}
