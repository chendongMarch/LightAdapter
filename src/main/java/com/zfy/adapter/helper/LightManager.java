package com.zfy.adapter.helper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

/**
 * CreateAt : 2018/4/22
 * Describe : 简单的创建 LayoutManager 的方式
 *
 * @author chendong
 */
public class LightManager {

    public static LinearLayoutManager vLinear(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    public static LinearLayoutManager hLinear(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    public static GridLayoutManager vGrid(Context context, int spanCount) {
        return new GridLayoutManager(context, spanCount, LinearLayoutManager.VERTICAL, false);
    }

    public static GridLayoutManager hGrid(Context context, int spanCount) {
        return new GridLayoutManager(context, spanCount, LinearLayoutManager.HORIZONTAL, false);
    }
}
