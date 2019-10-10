package com.zfy.lxadapter.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * CreateAt : 2019-10-08
 * Describe :
 *
 * @author chendong
 */
public class LxLinearLayoutManager extends LinearLayoutManager {

    public LxLinearLayoutManager(Context context) {
        super(context);
    }

    public LxLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LxLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float speedRatio = 1f;

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


    public static LxLinearLayoutManager get(Context context, boolean horizontal, boolean reverse) {
        return new LxLinearLayoutManager(context,
                horizontal ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL,
                reverse);
    }

    public static LxLinearLayoutManager get(Context context, boolean horizontal) {
        return get(context, horizontal, false);
    }

    public static LxLinearLayoutManager get(Context context) {
        return get(context, false, false);
    }

}
