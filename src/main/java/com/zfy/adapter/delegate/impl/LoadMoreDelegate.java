package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.delegate.refs.LoadMoreRef;
import com.zfy.adapter.listener.AdapterCallback;
import com.zfy.adapter.model.LoadingState;
import com.zfy.adapter.model.Extra;

/**
 * CreateAt : 2018/10/30
 * Describe : 底部加载更多功能
 *
 * @author chendong
 */
public class LoadMoreDelegate extends BaseDelegate implements LoadMoreRef {

    public static final int STRATEGY_SCROLL = 0; // 通过检测 scroll 获取加载更多
    public static final int STRATEGY_BIND   = 1; // 通过检测 onBindViewHolder 获取加载更多

    private int mStrategy = STRATEGY_SCROLL;
    private boolean         mLoadingMore; // 是否在加载更多
    private int             mStartTryLoadMoreItemCount; // 预加载的个数
    private boolean         mReachBottom; // 是否到达底部
    private boolean         mLoadMoreEnable; // 是否可以加载
    private boolean         mLoadMoreEnableFlagInternal = true; // 是否可以加载
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
    public boolean onBindViewHolder(LightHolder holder, int layoutIndex) {
        if (mStrategy == STRATEGY_SCROLL) {
            return super.onBindViewHolder(holder, layoutIndex);
        }
        if (!mLoadMoreEnable || !mLoadMoreEnableFlagInternal) {
            return super.onBindViewHolder(holder, layoutIndex);
        }
        Extra position = mAdapter.obtainExtraByLayoutIndex(layoutIndex);
        if (isAttached() && !mLoadingMore && mAdapter.getDatas().size() - position.modelIndex <= mStartTryLoadMoreItemCount) {
            mLoadingMore = true;
            mCallback.call(mAdapter);
            mAdapter.loadingView().setLoadingState(LoadingState.LOADING);
        }
        return super.onBindViewHolder(holder, layoutIndex);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mStrategy == STRATEGY_BIND) {
                    return;
                }
                if (!mLoadMoreEnable || !mLoadMoreEnableFlagInternal) {
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
                if (mStrategy == STRATEGY_BIND) {
                    return;
                }
                if (!mLoadMoreEnable || !mLoadMoreEnableFlagInternal) {
                    return;
                }
                if (isAttached() && dy > 0) {
                    int lastVisiblePosition = LightUtils.getLastVisiblePosition(mView);
                    mReachBottom = lastVisiblePosition + 1 + mStartTryLoadMoreItemCount >= mAdapter.getItemCount();
                }
            }
        });
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        mLoadMoreEnable = enable;
    }


    @Override
    public void finishLoadMore() {
        this.mLoadingMore = false;
        mAdapter.loadingView().setLoadingState(LoadingState.FINISH);
    }

    @Override
    public void setLoadMoreListener(int count, AdapterCallback callback) {
        mCallback = callback;
        mStartTryLoadMoreItemCount = count;
    }

    @Override
    public void setLoadMoreListener(AdapterCallback callback) {
        mCallback = callback;
    }

    public void setLoadMoreEnableFlagInternal(boolean loadMoreEnableFlagInternal) {
        mLoadMoreEnableFlagInternal = loadMoreEnableFlagInternal;
    }
}
