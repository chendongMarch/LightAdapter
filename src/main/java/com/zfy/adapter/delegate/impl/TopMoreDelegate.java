package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zfy.adapter.listener.AdapterCallback;

/**
 * CreateAt : 2018/10/30
 * Describe : 完成顶部加载更多功能
 *
 * @author chendong
 */
public class TopMoreDelegate extends BaseDelegate {

    @Override
    public int getKey() {
        return TOP_MORE;
    }


    private boolean mLoadingMore; // 是否在加载更多
    private int mStartTryTopMoreItemCount; // 预加载个数
    private boolean mReachTop; // 是否到达顶部
    private AdapterCallback mCallback; // 加载回调

    public TopMoreDelegate() {
        mStartTryTopMoreItemCount = 3;
        mCallback = adapter -> {
        };
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView mRecyclerView) {
        super.onAttachedToRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 停止，到达顶部,没有在加载更多，
                if (isAttached() && newState == RecyclerView.SCROLL_STATE_IDLE && mReachTop && !mLoadingMore) {
                    mLoadingMore = true;
                    mCallback.call(mAdapter);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAttached() && dy < 0) {
                    int firstPos = getFirstVisiblePosition(mView);
                    mReachTop = firstPos <= mStartTryTopMoreItemCount;
                }
            }
        });
    }


    // 获取最后一条展示的位置
    private int getFirstVisiblePosition(RecyclerView mRecyclerView) {
        int position;
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = 0;
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
     * 结束加载才能开启下次加载
     */
    public void finishTopMore() {
        this.mLoadingMore = false;
    }


    /**
     * @param count 预加载个数
     * @param callback 回调
     */
    public void setTopMoreListener(int count, AdapterCallback callback) {
        mCallback = callback;
        mStartTryTopMoreItemCount = count;
    }

    /**
     * @param callback 回调
     */
    public void setTopMoreListener(AdapterCallback callback) {
        mCallback = callback;
    }

    /**
     * @param count 预加载个数
     */
    public void setStartTryTopMoreItemCount(int count) {
        mStartTryTopMoreItemCount = count;
    }
}
