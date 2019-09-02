package com.zfy.adapter.component;

import com.zfy.adapter.Lx;
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
}
