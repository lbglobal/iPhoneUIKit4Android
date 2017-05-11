package com.wordplat.uikit.dialog;

import android.content.Context;

import com.wordplat.uikit.dialog.R;

/**
 * <p>我知道了 对话框：内容、我知道了</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class IKnowDialog extends BaseDialog {

    public IKnowDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_i_know;
    }

    public static IKnowDialog from(Context context) {
        return new IKnowDialog(context);
    }
}
