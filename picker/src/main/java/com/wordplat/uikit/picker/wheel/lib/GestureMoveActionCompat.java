package com.wordplat.uikit.picker.wheel.lib;

import android.view.MotionEvent;

/**
 * 横向移动、垂直移动 识别，解决滑动冲突用的
 *
 * Created by afon on 2017/1/22.
 */

public class GestureMoveActionCompat {

    private OnGestureMoveListener mGestureMoveListener;

    /**
     * 本次 ACTION_DOWN 事件的坐标 x
     */
    private float mLastMotionX;

    /**
     * 本次 ACTION_DOWN 事件的坐标 y
     */
    private float mLastMotionY;

    /**
     * 当前滑动的方向。0 无滑动（视为点击）；1 垂直滑动；2 横向滑动
     */
    private int mInterceptStatus = 0;

    /**
     * 是否响应点击事件
     *
     * 因为有手指抖动的影响，有时候会产生少量的 ACTION_MOVE 事件，造成程序识别错误。
     * 如果需要减少识别错误的几率，使用 {@link GestureMoveDetectorCompat} 这个类。
     */
    private boolean mEnableClick = true;

    public GestureMoveActionCompat(OnGestureMoveListener onGestureMoveListener) {
        mGestureMoveListener = onGestureMoveListener;
    }

    public void enableClick(boolean enableClick) {
        mEnableClick = enableClick;
    }

    /**
     * @param e 事件 e
     * @param x 本次事件的坐标 x。可以是 e.getRawX() 或是 e.getX()，具体看情况
     * @param y 本次事件的坐标 y。可以是 e.getRawY() 或是 e.getY()，具体看情况
     * @return 事件是否被横向移动消费
     */
    public boolean onTouchEvent(MotionEvent e, float x, float y) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                mLastMotionX = x;
                mInterceptStatus = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaY = y - mLastMotionY;
                float deltaX = x - mLastMotionX;

                /**
                 * 如果之前是垂直滑动，即使现在是横向滑动，仍然认为它是垂直滑动的
                 * 如果之前是横向滑动，即使现在是垂直滑动，仍然认为它是横向滑动的
                 * 防止在一个方向上来回滑动时，发生垂直滑动和横向滑动的频繁切换，造成识别错误
                 */
                if (mInterceptStatus != 1 && Math.abs(deltaX) > Math.abs(deltaY)) {
                    mInterceptStatus = 2;

                    if (mGestureMoveListener != null) {
                        mGestureMoveListener.onHorizontalMove(e, x, y);
                    }
                } else if (mInterceptStatus != 2) {
                    mInterceptStatus = 1;

                    if (mGestureMoveListener != null) {
                        mGestureMoveListener.onVerticalMove(e, x, y);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mInterceptStatus == 0) {
                    if (mEnableClick && mGestureMoveListener != null) {
                        mGestureMoveListener.onClick(e, x, y);
                    }
                }
                break;
        }
        return mInterceptStatus == 2;
    }

    public interface OnGestureMoveListener {

        /**
         * 横向移动
         */
        void onHorizontalMove(MotionEvent e, float x, float y);

        /**
         * 垂直移动
         */
        void onVerticalMove(MotionEvent e, float x, float y);

        /**
         * 点击事件
         */
        void onClick(MotionEvent e, float x, float y);
    }
}
