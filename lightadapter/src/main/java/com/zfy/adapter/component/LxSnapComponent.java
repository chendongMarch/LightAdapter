package com.zfy.adapter.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.zfy.adapter.Lx;
import com.zfy.adapter.LxAdapter;

/**
 * CreateAt : 2019-09-02
 * Describe :
 *
 * @author chendong
 */
public class LxSnapComponent extends LxComponent {

    private int snapMode;

    public LxSnapComponent(@Lx.SnapMode int snapMode) {
        this.snapMode = snapMode;
    }

    @Override
    public void onAttachedToRecyclerView(LxAdapter adapter, @NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(adapter, recyclerView);
        SnapHelper snapHelper;
        if (snapMode == Lx.SNAP_MODE_LINEAR) {
            snapHelper = new LinearSnapHelper();
        } else {
            snapHelper = new PagerSnapHelper();
        }
        snapHelper.attachToRecyclerView(recyclerView);
    }
}
