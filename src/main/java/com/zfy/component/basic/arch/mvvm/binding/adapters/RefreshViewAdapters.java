package com.zfy.component.basic.arch.mvvm.binding.adapters;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * CreateAt : 2018/9/10
 * Describe : SwipeRefreshLayout
 * bindRefresh
 *
 * @author chendong
 */
public class RefreshViewAdapters {

    @BindingAdapter(value = {"bindRefresh"})
    public static void onRefreshCommand(
            final SwipeRefreshLayout swipeRefreshLayout,
            final SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

}
