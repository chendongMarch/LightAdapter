package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.delegate.refs.TopMoreRef;
import com.zfy.adapter.listener.AdapterCallback;

/**
 * CreateAt : 2018/10/30
 * Describe : 完成顶部加载更多功能
 *
 * @author chendong
 */
public class TopMoreDelegate extends BaseDelegate implements TopMoreRef {

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
    public int getKey() {
        return TOP_MORE;
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

    @Override
    public void setTopMoreEnable(boolean enable) {
        mTopMoreEnable = enable;
    }

    @Override
    public void finishTopMore() {
        this.mLoadingMore = false;
    }


    @Override
    public void setTopMoreListener(int count, AdapterCallback callback) {
        mCallback = callback;
        mStartTryTopMoreItemCount = count;
    }

    @Override
    public void setTopMoreListener(AdapterCallback callback) {
        mCallback = callback;
    }

}
