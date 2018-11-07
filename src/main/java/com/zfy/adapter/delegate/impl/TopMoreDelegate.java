package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.common.LightUtils;
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
    private boolean mTopMoreEnable;

    public TopMoreDelegate() {
        mStartTryTopMoreItemCount = 3;
        mCallback = adapter -> {
        };
        mTopMoreEnable = true;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView mRecyclerView) {
        super.onAttachedToRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mTopMoreEnable) {
                    return;
                }
                // 停止，到达顶部,没有在加载更多，
                if (isAttached() && newState == RecyclerView.SCROLL_STATE_IDLE && mReachTop && !mLoadingMore) {
                    mLoadingMore = true;
                    mCallback.call(mAdapter);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mTopMoreEnable) {
                    return;
                }
                if (isAttached() && dy < 0) {
                    int firstPos = LightUtils.getFirstVisiblePosition(mView);
                    mReachTop = firstPos <= mStartTryTopMoreItemCount;
                }
            }
        });
    }

    /**
     * 加载更多是否可用
     *
     * @param enable
     */
    public void setTopMoreEnable(boolean enable) {
        mTopMoreEnable = enable;
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
