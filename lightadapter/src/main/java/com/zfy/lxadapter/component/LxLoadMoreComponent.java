package com.zfy.lxadapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.LxViewHolder;
import com.zfy.lxadapter.helper.LxUtil;
import com.zfy.lxadapter.listener.OnLoadMoreListener;

import java.util.List;

/**
 * CreateAt : 2019-09-01
 * Describe :
 *
 * @author chendong
 */
public class LxLoadMoreComponent extends LxComponent {

    public static final int DEFAULT_START_LOAD_COUNT = 3;

    @Lx.LoadMoreOn
    private int loadMoreOn = Lx.LOAD_MORE_ON_SCROLL;
    @Lx.LoadMoreEdge
    private int loadMoreEdge;

    private boolean loadingMore; // 是否在加载更多
    private int     startLoadMoreCount; // 预加载的个数
    private boolean reachEdge; // 是否到达边界
    private boolean loadMoreEnable; // 是否可以加载
    private int     viewOrientation = RecyclerView.VERTICAL;

    private OnLoadMoreListener listener;

    LxLoadMoreComponent(@Lx.LoadMoreEdge int loadMoreEdge, int startLoadMoreCount, OnLoadMoreListener loadMoreListener) {
        this.startLoadMoreCount = startLoadMoreCount;
        this.listener = loadMoreListener;
        this.loadMoreEnable = true;
        this.loadMoreEdge = loadMoreEdge;
    }

    @Override
    public void onBindViewHolder(LxAdapter adapter, @NonNull LxViewHolder holder, int position, @NonNull List<Object> payloads) {
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
        if (loadMoreEdge == Lx.LOAD_MORE_END_EDGE) {
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
                    reachEdge = false;
                    if (!loadMoreEnable) {
                        return;
                    }
                    if ((viewOrientation == RecyclerView.VERTICAL && dy > 0) || (viewOrientation == RecyclerView.HORIZONTAL && dx > 0)) {
                        int lastVisiblePosition = LxUtil.findLastVisibleItemPosition(recyclerView);
                        reachEdge = lastVisiblePosition + 1 + startLoadMoreCount >= adapter.getItemCount();
                    }
                }
            });
        } else if (loadMoreEdge == Lx.LOAD_MORE_START_EDGE) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!loadMoreEnable) {
                        return;
                    }
                    // 停止，到达顶部,没有在加载更多，
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
                    reachEdge = false;
                    if (!loadMoreEnable) {
                        return;
                    }
                    if ((viewOrientation == RecyclerView.VERTICAL && dy < 0) || (viewOrientation == RecyclerView.HORIZONTAL && dx < 0)) {
                        int firstPos = LxUtil.findFirstVisibleItemPosition(recyclerView);
                        reachEdge = firstPos <= startLoadMoreCount;
                    }
                }
            });
        }
    }

    public void setLoadMoreEnable(boolean enable) {
        loadMoreEnable = enable;
    }

    public void finishLoadMore() {
        loadingMore = false;
    }

    @Override
    public void onAttachedToAdapter(LxAdapter lxAdapter) {
        super.onAttachedToAdapter(lxAdapter);
        LxList data = lxAdapter.getData();
        // 结束加载监听
        data.addEventHandler(Lx.EVENT_FINISH_LOAD_MORE, (event, adapter, extra) -> {
            LxEndEdgeLoadMoreComponent endEdgeLoadMoreComponent = adapter.getComponent(LxEndEdgeLoadMoreComponent.class);
            if (endEdgeLoadMoreComponent != null) {
                endEdgeLoadMoreComponent.finishLoadMore();
            }
            LxStartEdgeLoadMoreComponent startEdgeLoadMoreComponent = adapter.getComponent(LxStartEdgeLoadMoreComponent.class);
            if (startEdgeLoadMoreComponent != null) {
                startEdgeLoadMoreComponent.finishLoadMore();
            }
        });
        // 加载开关
        data.addEventHandler(Lx.EVENT_LOAD_MORE_ENABLE, (event, adapter, extra) -> {
            if (!(extra instanceof Boolean)) {
                return;
            }
            LxEndEdgeLoadMoreComponent endEdgeLoadMoreComponent = adapter.getComponent(LxEndEdgeLoadMoreComponent.class);
            if (endEdgeLoadMoreComponent != null) {
                endEdgeLoadMoreComponent.setLoadMoreEnable((Boolean) extra);
            }
            LxStartEdgeLoadMoreComponent startEdgeLoadMoreComponent = adapter.getComponent(LxStartEdgeLoadMoreComponent.class);
            if (startEdgeLoadMoreComponent != null) {
                startEdgeLoadMoreComponent.setLoadMoreEnable((Boolean) extra);
            }
        });

    }
}
