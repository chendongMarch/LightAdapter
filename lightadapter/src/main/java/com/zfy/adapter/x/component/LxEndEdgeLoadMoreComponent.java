package com.zfy.adapter.x.component;

import com.zfy.adapter.x.Lx;
import com.zfy.adapter.x.listener.OnLoadMoreListener;

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
}
