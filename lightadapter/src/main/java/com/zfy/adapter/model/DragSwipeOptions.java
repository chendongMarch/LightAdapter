package com.zfy.adapter.model;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * CreateAt : 2018/11/7
 * Describe : 拖拽和侧滑的配置选项
 *
 * @author chendong
 */
public class DragSwipeOptions {

    public int dragFlags; // 拖动方向
    public int swipeFlags; // 滑动方向
    public boolean itemViewLongPressDragEnable = true; // 长按自动触发拖拽
    public boolean itemViewAutoSwipeEnable     = true; // 滑动自动触发滑动
    public float   moveThreshold               = .5f; // 超过 0.5 触发 onMoved
    public float   swipeThreshold              = .5f; // 超过 0.5 触发 onSwipe

    public DragSwipeOptions() {
        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
    }

    public DragSwipeOptions(int dragFlags, int swipeFlags) {
        this.dragFlags = dragFlags;
        this.swipeFlags = swipeFlags;
    }
}
