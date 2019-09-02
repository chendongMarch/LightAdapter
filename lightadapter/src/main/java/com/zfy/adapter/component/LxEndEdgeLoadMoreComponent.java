package com.zfy.adapter.component;

import com.zfy.adapter.Lx;
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
}
