package com.wordplat.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.wordplat.uikit.dialog.R;

/**
 * <p>BaseDialog</p>
 * <p>Date: 2017/5/11</p>
 *
 * @author afon
 */

public abstract class BaseDialog extends Dialog {
    private final static String TAG = "BaseDialog";

    protected Context context;

    protected TextView Dialog_Text;
    protected TextView Dialog_Title;
    protected Button Dialog_Action_But;
    protected Button Dialog_Cancel_But;

    private View.OnClickListener onClickListener;
    protected View.OnClickListener myActionClickListener;
    protected View.OnClickListener myCancelClickListener;

    protected String dialogText; // 对话框主要内容 文本
    protected String dialogTitle; // 对话框主要标题 文本
    protected String actionText; // 对话框动作按钮 文本
    protected String cancelText; // 对话框取消按钮 文本

    public BaseDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
    }

    protected abstract int getLayoutResId();

    protected void setUpDialog() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);

        setCancelable(true);
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
        Dialog_Text = (TextView) findViewById(R.id.Dialog_Text);
        Dialog_Action_But = (Button) findViewById(R.id.Dialog_Action_But);
        Dialog_Cancel_But = (Button) findViewById(R.id.Dialog_Cancel_But);

        if(Dialog_Text != null && !TextUtils.isEmpty(dialogText)) {
            Dialog_Text.setText(dialogText);
        }
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

    public BaseDialog withText(String text) {
        dialogText = text;
        return this;
    }

    public BaseDialog withTitle(String text) {
        dialogTitle = text;
        return this;
    }

    public BaseDialog withAction(String text) {
        actionText = text;
        return this;
    }

    public BaseDialog withCancel(String text) {
        cancelText = text;
        return this;
    }

    public BaseDialog onActionClick(View.OnClickListener onClickListener) {
        myActionClickListener = onClickListener;
        return this;
    }

    public BaseDialog onCancelClick(View.OnClickListener onClickListener) {
        myCancelClickListener = onClickListener;
        return this;
    }
}