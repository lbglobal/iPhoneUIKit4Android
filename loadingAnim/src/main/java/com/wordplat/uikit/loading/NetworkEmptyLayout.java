package com.wordplat.uikit.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * <p>网络加载中、网络异常、空布局。
 * copy from https://github.com/qyxxjd/MultipleStatusView
 * </p>
 * <p>Date: 2017/5/12</p>
 *
 * @author afon
 */

public class NetworkEmptyLayout extends RelativeLayout {
    public static final int STATUS_CONTENT    = 0x00;
    public static final int STATUS_LOADING    = 0x01;
    public static final int STATUS_EMPTY      = 0x02;
    public static final int STATUS_ERROR      = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;
    private View mContentView;
    private View mEmptyRetryView;
    private View mErrorRetryView;
    private View mNoNetworkRetryView;
    private int  mEmptyViewResId;
    private int  mErrorViewResId;
    private int  mLoadingViewResId;
    private int  mNoNetworkViewResId;
    private int  mViewStatus;

    private LayoutInflater mInflater;
    private OnClickListener mOnRetryClickListener;
    private final ViewGroup.LayoutParams mLayoutParams =
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public NetworkEmptyLayout(Context context) {
        this(context, null);
    }

    public NetworkEmptyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkEmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NetworkEmptyLayout, defStyleAttr, 0);

        mEmptyViewResId = a.getResourceId(R.styleable.NetworkEmptyLayout_emptyView, R.layout.layout_empty);
        mErrorViewResId = a.getResourceId(R.styleable.NetworkEmptyLayout_errorView, R.layout.layout_error);
        mLoadingViewResId = a.getResourceId(R.styleable.NetworkEmptyLayout_loadingView, R.layout.layout_loading);
        mNoNetworkViewResId = a.getResourceId(R.styleable.NetworkEmptyLayout_noNetworkView, R.layout.layout_no_network);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInflater = LayoutInflater.from(getContext());

        if (getChildCount() == 1) {

            mContentView = getChildAt(0);
            showContent();
        }
    }

    /**
     * 获取当前状态
     */
    @SuppressWarnings("unused")
    public int getViewStatus() {
        return mViewStatus;
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    /**
     * 显示空视图
     */
    public final void showEmpty() {
        mViewStatus = STATUS_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewResId, null);
            mEmptyRetryView = mEmptyView.findViewById(R.id.nRetryBut);
            if (mEmptyRetryView == null) {
                mEmptyRetryView = mEmptyView.findViewById(R.id.nEmptyRetryView);
            }
            if (null != mOnRetryClickListener && null != mEmptyRetryView) {
                mEmptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mEmptyView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    /**
     * 显示错误视图
     */
    public final void showError() {
        mViewStatus = STATUS_ERROR;
        if (null == mErrorView) {
            mErrorView = mInflater.inflate(mErrorViewResId, null);
            mErrorRetryView = mErrorView.findViewById(R.id.nRetryBut);
            if (mErrorRetryView == null) {
                mErrorRetryView = mErrorView.findViewById(R.id.nErrorRetryView);
            }
            if (null != mOnRetryClickListener && null != mErrorRetryView) {
                mErrorRetryView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mErrorView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    /**
     * 显示加载中视图
     */
    public final void showLoading() {
        mViewStatus = STATUS_LOADING;
        if (null == mLoadingView) {
            mLoadingView = mInflater.inflate(mLoadingViewResId, null);
            addView(mLoadingView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    /**
     * 显示无网络视图
     */
    public final void showNoNetwork() {
        mViewStatus = STATUS_NO_NETWORK;
        if (null == mNoNetworkView) {
            mNoNetworkView = mInflater.inflate(mNoNetworkViewResId, null);
            mNoNetworkRetryView = mNoNetworkView.findViewById(R.id.nRetryBut);
            if (mNoNetworkRetryView == null) {
                mNoNetworkRetryView = mNoNetworkView.findViewById(R.id.nNoNetworkRetryView);
            }
            if (null != mOnRetryClickListener && null != mNoNetworkRetryView) {
                mNoNetworkRetryView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mNoNetworkView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    /**
     * 显示内容视图
     */
    public final void showContent() {
        mViewStatus = STATUS_CONTENT;
        showViewByStatus(mViewStatus);
    }

    private void showViewByStatus(int viewStatus) {
        if (null != mLoadingView) {
            mLoadingView.setVisibility(viewStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(viewStatus == STATUS_EMPTY ? View.VISIBLE : View.GONE);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(viewStatus == STATUS_ERROR ? View.VISIBLE : View.GONE);
        }
        if (null != mNoNetworkView) {
            mNoNetworkView.setVisibility(viewStatus == STATUS_NO_NETWORK ? View.VISIBLE : View.GONE);
        }
        if (null != mContentView) {
            mContentView.setVisibility(viewStatus == STATUS_CONTENT ? View.VISIBLE : View.GONE);
        }
    }

}