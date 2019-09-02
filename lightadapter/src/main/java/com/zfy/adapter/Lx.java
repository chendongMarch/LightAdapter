package com.zfy.adapter;

import android.support.annotation.IntDef;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class Lx {

    @IntDef({SNAP_MODE_LINEAR, SNAP_MODE_PAGER})
    public @interface SnapMode {

    }

    public static final int SNAP_MODE_LINEAR = 0;
    public static final int SNAP_MODE_PAGER  = 1;

    @IntDef({LOAD_MORE_START_EDGE, LOAD_MORE_END_EDGE})
    public @interface LoadMoreEdge {

    }

    public static final int LOAD_MORE_START_EDGE = 0; // 加载更多，顶部
    public static final int LOAD_MORE_END_EDGE   = 1; // 加载更多，底部

    @IntDef({LOAD_MORE_ON_BIND, LOAD_MORE_ON_SCROLL})
    public @interface LoadMoreOn {

    }
    public static final int LOAD_MORE_ON_SCROLL = 0; // 通过检测 scroll 获取加载更多
    public static final int LOAD_MORE_ON_BIND   = 1; // 通过检测 onBindViewHolder 获取加载更多

    public static final int SPAN_NONE         = -0x30;
    public static final int SPAN_SIZE_ALL     = -0x31; // span size 占满整行
    public static final int SPAN_SIZE_HALF    = -0x32; // span size 占据一半
    public static final int SPAN_SIZE_THIRD   = -0x33; // span size 占据 1/3
    public static final int SPAN_SIZE_QUARTER = -0x34; // span size 占据 1/4

    @IntDef({EVENT_CLICK, EVENT_LONG_PRESS, EVENT_DOUBLE_CLICK})
    public @interface EventType {

    }

    public static final int EVENT_CLICK        = 0; // 单击事件
    public static final int EVENT_LONG_PRESS   = 1; // 长按事件
    public static final int EVENT_DOUBLE_CLICK = 2; // 双击事件


    public static final int VIEW_TYPE_DEFAULT = 0; // 默认 viewType
    public static       int VIEW_TYPE_BASE    = 1; // 基础 viewType
    public static final int VIEW_TYPE_HEADER  = Lx.incrementViewType(); // 内置 header
    public static final int VIEW_TYPE_FOOTER  = Lx.incrementViewType(); // 内置 footer
    public static final int VIEW_TYPE_EMPTY   = Lx.incrementViewType(); // 内置空载
    public static final int VIEW_TYPE_LOADING = Lx.incrementViewType(); // 内置 loading
    public static final int VIEW_TYPE_FAKE    = Lx.incrementViewType(); // 内置假数据

    @IntDef({DRAG_SWIPE_STATE_NONE, DRAG_STATE_ACTIVE, SWIPE_STATE_ACTIVE, DRAG_STATE_RELEASE, SWIPE_STATE_RELEASE})
    public @interface DragSwipeState {

    }

    public static final int DRAG_SWIPE_STATE_NONE = 0;
    public static final int DRAG_STATE_ACTIVE     = 1;
    public static final int SWIPE_STATE_ACTIVE    = 2;
    public static final int DRAG_STATE_RELEASE    = 3;
    public static final int SWIPE_STATE_RELEASE   = 4;

    public static int incrementViewType() {
        return VIEW_TYPE_BASE++;
    }

    public static final int DEFAULT_BLOCK_ID = 0;
}
