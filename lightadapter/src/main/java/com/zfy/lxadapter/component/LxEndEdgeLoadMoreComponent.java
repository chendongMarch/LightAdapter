package com.zfy.lxadapter.component;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.listener.OnLoadMoreListener;

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
        data.addAdapterEventDispatcher(Lx.EVENT_FINISH_END_EDGE_LOAD_MORE, (event, adapter, extra) -> {
            LxEndEdgeLoadMoreComponent endEdgeLoadMoreComponent = lxAdapter.getComponent(LxEndEdgeLoadMoreComponent.class);
            if (endEdgeLoadMoreComponent != null) {
                endEdgeLoadMoreComponent.finishLoadMore();
            }
        });
        data.addAdapterEventDispatcher(Lx.EVENT_END_EDGE_LOAD_MORE_ENABLE, (event, adapter, extra) -> {
            if (extra instanceof Boolean) {
                LxEndEdgeLoadMoreComponent endEdgeLoadMoreComponent = lxAdapter.getComponent(LxEndEdgeLoadMoreComponent.class);
                if (endEdgeLoadMoreComponent != null) {
                    endEdgeLoadMoreComponent.setLoadMoreEnable((Boolean) extra);
                }
            }
        });
    }
}
