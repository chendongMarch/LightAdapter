package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.listener.AdapterCallback;
import com.zfy.adapter.model.LoadingState;

/**
 * CreateAt : 2018/10/30
 * Describe : 底部加载更多功能
 *
 * @author chendong
 */
public class LoadMoreDelegate extends BaseDelegate {

    private boolean mLoadingMore; // 是否在加载更多
    private int mStartTryLoadMoreItemCount; // 预加载的个数
    private boolean mReachBottom; // 是否到达底部
    private boolean mLoadMoreEnable;
    private AdapterCallback mCallback; // 加载更多回调

    public LoadMoreDelegate() {
        mStartTryLoadMoreItemCount = 3;
        mCallback = adapter -> {
        };
        mLoadMoreEnable = true;
    }

    @Override
    public int getKey() {
        return IDelegate.LOAD_MORE;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mLoadMoreEnable) {
                    return;
                }
                // 停止，到达底部，没有在加载
                if (isAttached() && newState == RecyclerView.SCROLL_STATE_IDLE && mReachBottom && !mLoadingMore) {
                    mLoadingMore = true;
                    mCallback.call(mAdapter);
                    mAdapter.loadingView().setLoadingState(LoadingState.LOADING);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mLoadMoreEnable) {
                    return;
                }
                if (isAttached() && dy > 0) {
                    int lastVisiblePosition = LightUtils.getLastVisiblePosition(mView);
                    mReachBottom = lastVisiblePosition + 1 + mStartTryLoadMoreItemCount >= mAdapter.getItemCount();
                }
            }
        });
    }

    /**
     * 加载更多是否可用
     *
     * @param enable
     */
    public void setLoadMoreEnable(boolean enable) {
        mLoadMoreEnable = enable;
    }


    /**
     * 结束加载，才能开启下次加载
     */
    public void finishLoadMore() {
        this.mLoadingMore = false;
        mAdapter.loadingView().setLoadingState(LoadingState.FINISH);
    }

    /**
     * 设置加载更多监听
     * @param count 预加载的个数
     * @param callback 回调
     */
    public void setLoadMoreListener(int count, AdapterCallback callback) {
        mCallback = callback;
        mStartTryLoadMoreItemCount = count;
    }

    /**
     * 设置加载更多监听
     *
     * @param callback 回调
     */
    public void setLoadMoreListener(AdapterCallback callback) {
        mCallback = callback;
    }

}
