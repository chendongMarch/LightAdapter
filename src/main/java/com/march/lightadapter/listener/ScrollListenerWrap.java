package com.march.lightadapter.listener;

import android.support.v7.widget.RecyclerView;

/**
 * CreateAt : 2017/6/15
 * Describe :
 *
 * @author chendong
 */
class ScrollListenerWrap extends RecyclerView.OnScrollListener {

    public static final int UP   = 1;
    public static final int DOWN = -1;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }
}
