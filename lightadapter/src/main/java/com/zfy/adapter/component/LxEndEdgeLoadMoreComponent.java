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
public class LxEndEdgeLoadMoreComponent extends LxLoadMoreComponent {

    public LxEndEdgeLoadMoreComponent(int startLoadMoreCount, OnLoadMoreListener loadMoreListener) {
        super(Lx.LOAD_MORE_END_EDGE, startLoadMoreCount, loadMoreListener);
    }

    public LxEndEdgeLoadMoreComponent(OnLoadMoreListener loadMoreListener) {
        super(Lx.LOAD_MORE_END_EDGE, DEFAULT_START_LOAD_COUNT, loadMoreListener);
    }

    @Override
    public void onAttachedToAdapter(LxAdapter lxAdapter) {
        super.onAttachedToAdapter(lxAdapter);
        LxList data = lxAdapter.getData();
        data.addEventHandler(Lx.EVENT_FINISH_END_EDGE_LOAD_MORE, (event, adapter, extra) -> {
            LxEndEdgeLoadMoreComponent endEdgeLoadMoreComponent = lxAdapter.getComponent(LxEndEdgeLoadMoreComponent.class);
            if (endEdgeLoadMoreComponent != null) {
                endEdgeLoadMoreComponent.finishLoadMore();
            }
        });
        data.addEventHandler(Lx.EVENT_END_EDGE_LOAD_MORE_ENABLE, (event, adapter, extra) -> {
            if (extra instanceof Boolean) {
                LxEndEdgeLoadMoreComponent endEdgeLoadMoreComponent = lxAdapter.getComponent(LxEndEdgeLoadMoreComponent.class);
                if (endEdgeLoadMoreComponent != null) {
                    endEdgeLoadMoreComponent.setLoadMoreEnable((Boolean) extra);
                }
            }
        });
    }
}
