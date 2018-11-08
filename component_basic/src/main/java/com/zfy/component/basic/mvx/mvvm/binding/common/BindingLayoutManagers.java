package com.zfy.component.basic.mvx.mvvm.binding.common;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import me.tatarka.bindingcollectionadapter2.LayoutManagers;


/**
 * CreateAt : 2018/9/13
 * Describe :
 *
 * @author chendong
 */
public class BindingLayoutManagers implements LayoutManagers.LayoutManagerFactory {

    public static final int VERTICAL   = RecyclerView.VERTICAL;
    public static final int HORIZONTAL = RecyclerView.HORIZONTAL;

    public static final int LINEAR         = 0;
    public static final int GRID           = 1;
    public static final int STAGGERED_GRID = 2;

    private int     orientation = VERTICAL;
    private int     spanCount   = 1;
    private boolean reverse     = false;
    private int     type        = LINEAR;
    private GridLayoutManager.SpanSizeLookup spanSizeLookup;

    private BindingLayoutManagers() {

    }

    public static BindingLayoutManagers make() {
        return new BindingLayoutManagers();
    }

    public static BindingLayoutManagers make(int type) {
        BindingLayoutManagers factory = new BindingLayoutManagers();
        factory.type = type;
        return factory;
    }

    @Override
    public RecyclerView.LayoutManager create(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        RecyclerView.LayoutManager layoutManager = null;
        switch (type) {
            case LINEAR:
                layoutManager = new LinearLayoutManager(context, orientation, reverse);
                break;
            case GRID:
                GridLayoutManager manager = new GridLayoutManager(context, spanCount, orientation, reverse);
                if (spanSizeLookup != null) {
                    manager.setSpanSizeLookup(spanSizeLookup);
                }
                layoutManager = manager;
                break;
            case STAGGERED_GRID:
                layoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
                break;
        }
        return layoutManager;
    }

    public BindingLayoutManagers orientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    public BindingLayoutManagers spanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public BindingLayoutManagers reverse() {
        this.reverse = true;
        return this;
    }

    public BindingLayoutManagers spanSizeLookup(GridLayoutManager.SpanSizeLookup lookup) {
        this.spanSizeLookup = lookup;
        return this;
    }
}

