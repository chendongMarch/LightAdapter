package com.zfy.adapter.delegate.impl;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * CreateAt : 2018/10/30
 * Describe : 完成顶部加载更多功能
 *
 * @author chendong
 */
public class TopMoreDelegate extends BaseDelegate {

    @Override
    public int getKey() {
        return TOPMORE;
    }


    private boolean mLoadingMore;
    private int mPreLoadNum;
    private boolean mReachTop;
    private Runnable mRunnable;

    public TopMoreDelegate() {
        mPreLoadNum = 3;
        mRunnable = () -> {
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
                    mRunnable.run();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAttached() && dy < 0) {
                    int firstPos = getFirstVisiblePosition(mView);
                    mReachTop = firstPos <= mPreLoadNum;
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
    public void finishLoad() {
        this.mLoadingMore = false;
    }
}
