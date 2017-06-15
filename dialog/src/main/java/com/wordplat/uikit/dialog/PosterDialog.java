package com.wordplat.uikit.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * <p>海报 对话框："图片、取消"或"标题、内容、取消"</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class PosterDialog extends BaseDialog {

    private RelativeLayout Dialog_Content = null;
    private ImageView Dialog_Image = null;

    private Bitmap bitmap;

    public PosterDialog(Context context) {
        super(context);
    }

    protected int getLayoutResId() {
        return R.layout.dialog_poster;
    }

    public PosterDialog withImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dialog_Image = (ImageView) findViewById(R.id.Dialog_Image);
        Dialog_Content = (RelativeLayout) findViewById(R.id.Dialog_Content);

        if (bitmap != null) {
            Dialog_Content.setVisibility(View.GONE);
            Dialog_Image.setVisibility(View.VISIBLE);

            Dialog_Image.setImageBitmap(bitmap);
        }
    }

    public static PosterDialog from(Context context) {
        return new PosterDialog(context);
    }
}
