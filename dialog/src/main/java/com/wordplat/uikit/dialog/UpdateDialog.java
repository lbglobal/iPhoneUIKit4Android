package com.wordplat.uikit.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * <p>更新 对话框</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public class UpdateDialog extends BaseDialog {

    protected Button Dialog_Nolonger_But;

    protected View.OnClickListener myNoLongerClickListener;

    private boolean isForce = false;

    public UpdateDialog(Context context) {
        super(context);
    }

    public UpdateDialog withUpdateForce(boolean isForce) {
        this.isForce = isForce;

        return this;
    }

    public UpdateDialog onNoLongerClick(View.OnClickListener onClickListener) {
        myNoLongerClickListener = onClickListener;

        return this;
    }

    protected int getLayoutResId() {
        return isForce ? R.layout.dialog_update_force : R.layout.dialog_update_normal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isForce) {
            Dialog_Nolonger_But = (Button) findViewById(R.id.Dialog_Nolonger_But);

            Dialog_Nolonger_But.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myNoLongerClickListener != null) {
                        myNoLongerClickListener.onClick(v);
                    }

                    dismiss();
                }
            });
        }
    }

    public static UpdateDialog from(Context context) {
        return new UpdateDialog(context);
    }
}
