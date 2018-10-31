package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zfy.adapter.AdapterCallback;
import com.zfy.adapter.delegate.IDelegate;

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
    private AdapterCallback mCallback; // 加载更多回调

    public LoadMoreDelegate() {
        mStartTryLoadMoreItemCount = 3;
        mCallback = adapter -> {
        };
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
                // 停止，到达底部，没有在加载
                if (isAttached() && newState == RecyclerView.SCROLL_STATE_IDLE && mReachBottom && !mLoadingMore) {
                    mLoadingMore = true;
                    mCallback.call(mAdapter);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAttached() && dy > 0) {
                    int lastVisiblePosition = getLastVisiblePosition(mView);
                    mReachBottom = lastVisiblePosition + 1 + mStartTryLoadMoreItemCount >= mAdapter.getItemCount();
                }
            }
        });
    }


    // 获取最后一条展示的位置
    private int getLastVisiblePosition(RecyclerView mRecyclerView) {
        int position;
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = manager.getItemCount() - 1;
        }
        return position;
    }

    // 获得最大的位置
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    /**
     * 结束加载，才能开启下次加载
     */
    public void finishLoadMore() {
        this.mLoadingMore = false;
    }

    /**
     * 设置加载更多监听
     *
     * @param callback
     */
    public void setLoadMoreListener(AdapterCallback callback) {
        mCallback = callback;
    }


    /**
     * 设置提前预加载的个数
     *
     * @param startTryLoadMoreItemCount 预加载的个数
     */
    public void setStartTryLoadMoreItemCount(int startTryLoadMoreItemCount) {
        mStartTryLoadMoreItemCount = startTryLoadMoreItemCount;
    }
}
