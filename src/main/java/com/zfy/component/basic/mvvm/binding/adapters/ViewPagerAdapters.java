package com.zfy.component.basic.mvvm.binding.adapters;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;


/**
 * CreateAt : 2018/9/10
 * Describe : ViewPager
 * bindPageChanged
 *
 * @author chendong
 */
public class ViewPagerAdapters {

    @BindingAdapter(value = {"bindPageChanged"})
    public static void bindPageChanged(
            final ViewPager viewPager,
            final ViewPager.OnPageChangeListener onPageChangeListener) {
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

}
