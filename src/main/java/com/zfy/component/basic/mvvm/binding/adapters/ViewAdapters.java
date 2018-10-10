package com.zfy.component.basic.mvvm.binding.adapters;

import android.databinding.BindingAdapter;
import android.view.View;


/**
 * CreateAt : 2018/9/10
 * Describe : ViewGroup
 * bindClick
 * bindFocus
 * bindFocusChanged
 * bindTouch
 *
 * @author chendong
 */
public final class ViewAdapters {

    @BindingAdapter({"bindClick"})
    public static void bindClick(
            final View view,
            final View.OnClickListener onClickListener) {
        view.setOnClickListener(onClickListener);
    }

    @BindingAdapter({"bindFocus"})
    public static void bindFocus(
            final View view,
            final Boolean requestFocus) {
        if (requestFocus) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }

    @BindingAdapter({"bindFocusChanged"})
    public static void bindFocusChanged(
            final View view,
            final View.OnFocusChangeListener onFocusChangeListener) {
        view.setOnFocusChangeListener(onFocusChangeListener);
    }

    @BindingAdapter({"bindTouch"})
    public static void bindTouch(
            final View view,
            final View.OnTouchListener onTouchListener) {
        view.setOnTouchListener(onTouchListener);
    }
}

