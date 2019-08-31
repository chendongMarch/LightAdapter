package com.zfy.adapter.x.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.x.Lx;
import com.zfy.adapter.x.LxAdapter;
import com.zfy.adapter.x.LxVh;
import com.zfy.adapter.x.function.LxUtil;
import com.zfy.adapter.x.listener.OnLoadMoreListener;

import java.util.List;

/**
 * CreateAt : 2019-09-01
 * Describe :
 *
 * @author chendong
 */
public class LxLoadMoreComponent extends LxComponent {

    public static final int DEFAULT_START_LOAD_COUNT = 3;

    private int loadMoreOn   = Lx.LOAD_MORE_ON_SCROLL;
    private int loadMoreEdge;

    private boolean loadingMore; // 是否在加载更多
    private int     startLoadMoreCount; // 预加载的个数
    private boolean reachEdge; // 是否到达边界
    private boolean loadMoreEnable; // 是否可以加载
    private int     viewOrientation = RecyclerView.VERTICAL;

    private OnLoadMoreListener listener;

    public LxLoadMoreComponent(int loadMoreEdge, int startLoadMoreCount, OnLoadMoreListener loadMoreListener) {
        this.startLoadMoreCount = startLoadMoreCount;
        this.listener = loadMoreListener;
        this.loadMoreEnable = true;
        this.loadMoreEdge = loadMoreEdge;
    }

    @Override
    public void onBindViewHolder(LxAdapter adapter, @NonNull LxVh holder, int position, @NonNull List<Object> payloads) {
        if (loadMoreOn == Lx.LOAD_MORE_ON_SCROLL) {
            return;
        }
        if (loadingMore || !loadMoreEnable) {
            return;
        }
        if (adapter.getData().size() - position <= startLoadMoreCount) {
            loadingMore = true;
            if (listener != null) {
                listener.load(LxLoadMoreComponent.this);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        viewOrientation = LxUtil.getRecyclerViewOrientation(recyclerView);
        if (loadMoreOn == Lx.LOAD_MORE_ON_BIND) {
            return;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!loadMoreEnable) {
                    return;
                }
                // 停止，到达底部，没有在加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && reachEdge && !loadingMore) {
                    loadingMore = true;
                    if (listener != null) {
                        listener.load(LxLoadMoreComponent.this);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!loadMoreEnable) {
                    return;
                }
                if ((viewOrientation == RecyclerView.VERTICAL && dy > 0) || (viewOrientation == RecyclerView.HORIZONTAL && dx > 0)) {
                    int lastVisiblePosition = LxUtil.getLastVisiblePosition(recyclerView);
                    reachEdge = lastVisiblePosition + 1 + startLoadMoreCount >= adapter.getItemCount();
                }
            }
        });
    }

    public void setLoadMoreEnable(boolean enable) {
        loadMoreEnable = enable;
    }

    public void finishLoadMore() {
        loadingMore = false;
    }
}
