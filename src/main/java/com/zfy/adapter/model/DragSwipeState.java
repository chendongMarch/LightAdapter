package com.zfy.adapter.model;

/**
 * CreateAt : 2018/11/7
 * Describe : 拖拽和侧滑状态变化
 *
 * @author chendong
 */
public class DragSwipeState {

    public static final int ACTIVE_DRAG = 0;
    public static final int ACTIVE_SWIPE = 1;
    public static final int RELEASE_DRAG = 2;
    public static final int RELEASE_SWIPE = 3;

    public int state;

    public DragSwipeState() {
    }

}
