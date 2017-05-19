package com.wordplat.uikit.picker.wheel.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.wordplat.uikit.picker.R;

import static android.view.Gravity.CENTER;

/**
 * <p>BaseBottomDialog</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public abstract class BaseBottomDialog extends Dialog {
    private final static String TAG = "BaseBottomDialog";

    protected Context context;

    protected TextView Dialog_Title;
    protected Button Dialog_Action_But;
    protected Button Dialog_Cancel_But;

    private View.OnClickListener onClickListener;
    protected View.OnClickListener myActionClickListener;
    protected View.OnClickListener myCancelClickListener;

    protected String dialogTitle; // 对话框主要标题 文本
    protected String actionText; // 对话框动作按钮 文本
    protected String cancelText; // 对话框取消按钮 文本

    private boolean isCancellable = true;
    private boolean isCancelOutside = true;

    public BaseBottomDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
    }

    protected abstract int getLayoutResId();

    protected void setUpDialog() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = window.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        window.setGravity(CENTER);
        window.setWindowAnimations(R.style.BottomDialogAnim);

        setCancelable(isCancellable);
        setCanceledOnTouchOutside(isCancelOutside);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(getLayoutResId());
            setUpDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Dialog_Title = (TextView)findViewById(R.id.Dialog_Title);
        Dialog_Action_But = (Button) findViewById(R.id.Dialog_Action_But);
        Dialog_Cancel_But = (Button) findViewById(R.id.Dialog_Cancel_But);

        if(Dialog_Title != null && !TextUtils.isEmpty(dialogTitle)){
            Dialog_Title.setText(dialogTitle);
        }
        if(Dialog_Action_But != null && !TextUtils.isEmpty(actionText)) {
            Dialog_Action_But.setText(actionText);
        }
        if(Dialog_Cancel_But != null && !TextUtils.isEmpty(cancelText)) {
            Dialog_Cancel_But.setText(cancelText);
        }

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = v.getId();

                if (id == R.id.Dialog_Action_But) {
                    if (myActionClickListener != null) {
                        myActionClickListener.onClick(v);
                    }
                }

                if (id == R.id.Dialog_Cancel_But) {
                    if (myCancelClickListener != null) {
                        myCancelClickListener.onClick(v);
                    }
                }

                dismiss();
            }
        };

        if(Dialog_Action_But != null) {
            Dialog_Action_But.setOnClickListener(onClickListener);
        }

        if(Dialog_Cancel_But != null) {
            Dialog_Cancel_But.setOnClickListener(onClickListener);
        }
    }

    public BaseBottomDialog withTitle(String text) {
        dialogTitle = text;
        return this;
    }

    public BaseBottomDialog withAction(String text) {
        actionText = text;
        return this;
    }

    public BaseBottomDialog withCancel(String text) {
        cancelText = text;
        return this;
    }

    public BaseBottomDialog withCancellable(boolean isCancellable) {
        this.isCancellable = isCancellable;
        return this;
    }

    public BaseBottomDialog withCancelOutside(boolean isCancelOutside) {
        this.isCancelOutside = isCancelOutside;
        return this;
    }

    public BaseBottomDialog onActionClick(View.OnClickListener onClickListener) {
        myActionClickListener = onClickListener;
        return this;
    }

    public BaseBottomDialog onCancelClick(View.OnClickListener onClickListener) {
        myCancelClickListener = onClickListener;
        return this;
    }
}