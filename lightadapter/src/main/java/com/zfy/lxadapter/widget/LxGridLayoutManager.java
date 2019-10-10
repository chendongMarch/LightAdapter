package com.zfy.lxadapter.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * CreateAt : 2019-10-08
 * Describe :
 *
 * @author chendong
 */
public class LxGridLayoutManager extends GridLayoutManager {


    private float speedRatio = 1f;

    public LxGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LxGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public LxGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int a = super.scrollHorizontallyBy(dx, recycler, state);
        if (a == (int) (speedRatio * dx)) {
            return dx;
        }
        return a;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int a = super.scrollVerticallyBy(dy, recycler, state);
        if (a == (int) (speedRatio * dy)) {
            return dy;
        }
        return a;
    }

    public void setSpeedRatio(float speedRatio) {
        this.speedRatio = speedRatio;
    }
}
