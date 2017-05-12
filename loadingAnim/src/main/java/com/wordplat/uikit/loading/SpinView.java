/*
 *    Copyright 2015 Kaopiz Software Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.wordplat.uikit.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wordplat.uikit.loading.R;

public class SpinView extends ImageView implements Indeterminate {

    private float mRotateDegrees;
    private int mFrameTime;
    private boolean mNeedToUpdateView;
    private Runnable mUpdateViewRunnable;

    private Context context;

    public SpinView(Context context) {
        this(context, null);
    }

    public SpinView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initUI(context, attrs, defStyleAttr);
    }

    private void initUI(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpinView, defStyleAttr, 0);
        int loadingResId = a.getResourceId(R.styleable.SpinView_loadingImage, R.drawable.loading_spinner);

        setImageResource(loadingResId);
        mFrameTime = 1000 / 12;
        mUpdateViewRunnable = new Runnable() {
            @Override
            public void run() {
                mRotateDegrees += 30;
                mRotateDegrees = mRotateDegrees < 360 ? mRotateDegrees : mRotateDegrees - 360;
                invalidate();
                if (mNeedToUpdateView) {
                    postDelayed(this, mFrameTime);
                }
            }
        };
    }

    @Override
    public void setAnimationSpeed(float scale) {
        mFrameTime = (int) (1000 / 12 / scale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mRotateDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedToUpdateView = true;
        post(mUpdateViewRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        mNeedToUpdateView = false;
        super.onDetachedFromWindow();
    }
}
