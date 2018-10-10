package com.zfy.component.basic.mvvm.binding.adapters;

import android.databinding.BindingAdapter;
import android.support.v4.widget.NestedScrollView;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

/**
 * CreateAt : 2018/9/10
 * Describe : ScrollView
 * bindScroll
 * bindScroll
 * @author chendong
 */
public final class ScrollViewAdapters {

    @BindingAdapter(value = {"bindScroll"})
    public static void onScrollChangeCommand(
            final NestedScrollView nestedScrollView,
            final NestedScrollView.OnScrollChangeListener onScrollChangeListener) {
        nestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
    }

    @BindingAdapter(value = {"bindScroll"})
    public static void onScrollChangeCommand(
            final ScrollView scrollView,
            final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener) {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (onScrollChangedListener != null) {
                onScrollChangedListener.onScrollChanged();
            }
        });
    }
}
