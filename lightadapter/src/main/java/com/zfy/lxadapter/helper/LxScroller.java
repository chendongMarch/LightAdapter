package com.zfy.lxadapter.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Field;

/**
 * CreateAt : 2019-10-06
 * Describe : 滑动辅助
 *
 * @author chendong
 */
public class LxScroller {

    public static final int START  = LinearSmoothScroller.SNAP_TO_START;
    public static final int END    = LinearSmoothScroller.SNAP_TO_END;
    public static final int CENTER = 10;

    /**
     * 设置滑动阻力
     *
     * @param recyclerView view
     * @param ratio        占据现在的比例
     */
    public static void setMaxFlingVelocity(RecyclerView recyclerView, float ratio) {
        try {
            Field field = recyclerView.getClass().getDeclaredField("mMaxFlingVelocity");
            field.setAccessible(true);
            field.set(recyclerView, (int) (recyclerView.getMaxFlingVelocity() * ratio));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 滑动到指定位置，并且置顶
     *
     * @param view     rv
     * @param position 位置
     */
    public static void scrollToPositionOnStart(RecyclerView view, int position) {
        if (position < 0) {
            return;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) view.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPositionWithOffset(position, 0);
        }
    }


    /**
     * 滑动到指定位置
     *
     * @param where    位置
     * @param view     rv
     * @param position 位置
     */
    public static void scrollToPositionOnWhere(int where, RecyclerView view, int position) {
        if (position < 0) {
            return;
        }
        if (where == CENTER) {
            scrollToPositionOnCenter(view, position);
            return;
        }
        LinearSmoothScroller scroller = new LinearSmoothScroller(view.getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return where;
            }

            @Override
            protected int getHorizontalSnapPreference() {
                return where;
            }
        };
        scroller.setTargetPosition(position);
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.startSmoothScroll(scroller);
        }
    }


    /**
     * 将指定位置滑动到中间
     *
     * @param view     rv
     * @param position 位置
     */
    public static void scrollToPositionOnCenter(RecyclerView view, int position) {
        LinearSmoothScroller scroller = new LinearSmoothScroller(view.getContext()) {
            @Override
            public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
            }
        };
        scroller.setTargetPosition(position);
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.startSmoothScroll(scroller);
        }
    }

}
