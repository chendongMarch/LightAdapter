package com.zfy.adapter.component;

import com.zfy.adapter.Lx;
import com.zfy.adapter.LxAdapter;
import com.zfy.adapter.LxList;
import com.zfy.adapter.listener.OnLoadMoreListener;

/**
 * CreateAt : 2019-09-01
 * Describe :
 *
 * @author chendong
 */
public class LxStartEdgeLoadMoreComponent extends LxLoadMoreComponent {

    public LxStartEdgeLoadMoreComponent(int startLoadMoreCount, OnLoadMoreListener loadMoreListener) {
        super(Lx.LOAD_MORE_START_EDGE, startLoadMoreCount, loadMoreListener);
    }

    public LxStartEdgeLoadMoreComponent(OnLoadMoreListener loadMoreListener) {
        super(Lx.LOAD_MORE_START_EDGE, DEFAULT_START_LOAD_COUNT, loadMoreListener);
    }


    @Override
    public void onAttachedToAdapter(LxAdapter lxAdapter) {
        super.onAttachedToAdapter(lxAdapter);
        LxList data = lxAdapter.getData();
        data.addEventHandler(Lx.EVENT_FINISH_START_EDGE_LOAD_MORE, (event, adapter, extra) -> {
            LxStartEdgeLoadMoreComponent startEdgeLoadMoreComponent = adapter.getComponent(LxStartEdgeLoadMoreComponent.class);
            if (startEdgeLoadMoreComponent != null) {
                startEdgeLoadMoreComponent.finishLoadMore();
            }
        });
        data.addEventHandler(Lx.EVENT_START_EDGE_LOAD_MORE_ENABLE, (event, adapter, extra) -> {
            if (!(extra instanceof Boolean)) {
                return;
            }
            LxStartEdgeLoadMoreComponent startEdgeLoadMoreComponent = adapter.getComponent(LxStartEdgeLoadMoreComponent.class);
            if (startEdgeLoadMoreComponent != null) {
                startEdgeLoadMoreComponent.setLoadMoreEnable((Boolean) extra);
            }
        });
    }
}
