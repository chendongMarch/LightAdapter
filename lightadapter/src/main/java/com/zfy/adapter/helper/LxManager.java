package com.zfy.adapter.helper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


/**
 * CreateAt : 2019-09-17
 * Describe :
 * 简化创建 LayoutManager
 *
 * @author chendong
 */
public class LxManager {

    // linear

    public static RecyclerView.LayoutManager linear(Context context, boolean horizontal, boolean reverse) {
        return new LinearLayoutManager(context,
                horizontal ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL,
                reverse);
    }

    public static RecyclerView.LayoutManager linear(Context context, boolean horizontal) {
        return linear(context, horizontal, false);
    }

    public static RecyclerView.LayoutManager linear(Context context) {
        return linear(context, false, false);
    }


    // grid


    public static RecyclerView.LayoutManager grid(Context context, int spanCount, boolean horizontal, boolean reverse) {
        return new GridLayoutManager(context, spanCount,
                horizontal ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL,
                reverse);
    }

    public static RecyclerView.LayoutManager grid(Context context, int spanCount, boolean horizontal) {
        return grid(context, spanCount, horizontal, false);
    }

    public static RecyclerView.LayoutManager grid(Context context, int spanCount) {
        return grid(context, spanCount, false, false);
    }

    // flow

    public static RecyclerView.LayoutManager flow(int spanCount, boolean horizontal) {
        return new StaggeredGridLayoutManager(spanCount, horizontal ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL);
    }

    public static RecyclerView.LayoutManager flow(int spanCount) {
        return flow(spanCount, false);
    }

}
