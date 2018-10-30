package com.zfy.adapter.delegate;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * CreateAt : 2018/10/30
 * Describe :
 *
 * @author chendong
 */
public class LoadMoreDelegate extends BaseDelegate {

    private boolean mLoadingMore;
    private int mPreLoadNum;
    private boolean mReachBottom;
    private Runnable mRunnable;

    public LoadMoreDelegate() {
        mPreLoadNum = 3;
        mRunnable = () -> {
        };
    }

    @Override
    public int getKey() {
        return IDelegate.LOADMORE;
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
                    mRunnable.run();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAttached() && dy > 0) {
                    int lastVisiblePosition = getLastVisiblePosition(mView);
                    mReachBottom = lastVisiblePosition + 1 + mPreLoadNum >= mAdapter.getItemCount();
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
    public void finishLoad() {
        this.mLoadingMore = false;
    }
}
