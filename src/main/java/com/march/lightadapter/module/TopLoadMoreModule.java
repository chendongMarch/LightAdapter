package com.march.lightadapter.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.march.lightadapter.listener.OnLoadMoreListener;

/**
 * Created by march on 16/6/8.
 * 顶部加载更多模块的实现
 */
public class TopLoadMoreModule extends AbstractModule {

    private boolean mIsLoadingMore;
    private int preLoadNum = 0;
    private boolean isTopping;

    public TopLoadMoreModule(int preLoadNum) {
        this.preLoadNum = preLoadNum;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView mRecyclerView) {
        super.onAttachedToRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mAttachAdapter == null) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mIsLoadingMore) {
                        if (isTopping) {
                            mIsLoadingMore = true;
                            mAttachAdapter.onTopLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mAttachAdapter == null) {
                    return;
                }
                if (dy < 0) {
                    int firstPos = getFirstVisiblePosition(mAttachRecyclerView);
                    isTopping = firstPos <= preLoadNum;
                }
            }
        });
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return pos
     */
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

    /**
     * 获得最大的位置
     *
     * @param positions 位置
     * @return pos
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    public void finishLoad() {
        this.mIsLoadingMore = false;
    }
}
