package com.zfy.adapter.model;

/**
 * CreateAt : 2018/11/7
 * Describe : 拖拽和侧滑状态变化
 *
 * @author chendong
 */
public class DragSwipeState {

    public static final int ACTIVE_NONE   = 0;
    public static final int ACTIVE_DRAG   = 1;
    public static final int ACTIVE_SWIPE  = 2;
    public static final int RELEASE_DRAG  = 3;
    public static final int RELEASE_SWIPE = 4;

    public int state;

    public DragSwipeState() {
    }

}
