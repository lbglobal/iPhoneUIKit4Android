package com.wordplat.uikit.dialog;

import android.content.Context;

/**
 * <p>海报 对话框："图片、取消"或"标题、内容、取消"</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class PosterDialog extends BaseDialog {

    public PosterDialog(Context context) {
        super(context);
    }

    protected int getLayoutResId() {
        return R.layout.dialog_poster;
    }

    public static PosterDialog from(Context context) {
        return new PosterDialog(context);
    }
}
