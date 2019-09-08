package com.zfy.adapter;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class Lx {

    @IntDef({SNAP_MODE_LINEAR, SNAP_MODE_PAGER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SnapMode {
    }

    public static final int SNAP_MODE_LINEAR = 0;
    public static final int SNAP_MODE_PAGER  = 1;

    @IntDef({LOAD_MORE_START_EDGE, LOAD_MORE_END_EDGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadMoreEdge {

    }

    public static final int LOAD_MORE_START_EDGE = 0; // 加载更多，顶部
    public static final int LOAD_MORE_END_EDGE   = 1; // 加载更多，底部

    @IntDef({LOAD_MORE_ON_BIND, LOAD_MORE_ON_SCROLL})
    @Retention(RetentionPolicy.SOURCE)
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
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {

    }

    public static final int EVENT_CLICK        = 0; // 单击事件
    public static final int EVENT_LONG_PRESS   = 1; // 长按事件
    public static final int EVENT_DOUBLE_CLICK = 2; // 双击事件

    public static final int VIEW_TYPE_MAX                     = 4096;
    public static       int VIEW_TYPE_BASE                    = VIEW_TYPE_MAX / 2; // 基础 viewType
    public static       int VIEW_TYPE_CONTENT_BASE_4_GENERATE = VIEW_TYPE_BASE + 1; // 内容类型基础 viewType
    public static       int VIEW_TYPE_EXT_BASE_4_GENERATE     = VIEW_TYPE_BASE - 1; // 扩展类型基础 viewType

    public static final int VIEW_TYPE_DEFAULT = Lx.contentTypeOf(); // 默认 viewType
    public static final int VIEW_TYPE_SECTION = Lx.contentTypeOf(); // 隔断 viewType
    public static final int VIEW_TYPE_HEADER  = Lx.extTypeOf(); // 内置 header
    public static final int VIEW_TYPE_FOOTER  = Lx.extTypeOf(); // 内置 footer
    public static final int VIEW_TYPE_EMPTY   = Lx.extTypeOf(); // 内置空载
    public static final int VIEW_TYPE_LOADING = Lx.extTypeOf(); // 内置 loading
    public static final int VIEW_TYPE_FAKE    = Lx.extTypeOf(); // 内置假数据

    @IntDef({DRAG_SWIPE_STATE_NONE, DRAG_STATE_ACTIVE, SWIPE_STATE_ACTIVE, DRAG_STATE_RELEASE, SWIPE_STATE_RELEASE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DragSwipeState {

    }

    public static final int DRAG_SWIPE_STATE_NONE = 0;
    public static final int DRAG_STATE_ACTIVE     = 1;
    public static final int SWIPE_STATE_ACTIVE    = 2;
    public static final int DRAG_STATE_RELEASE    = 3;
    public static final int SWIPE_STATE_RELEASE   = 4;


    @IntDef({SELECT_SINGLE, SELECT_MULTI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectMode {

    }

    public static final int SELECT_SINGLE = 1;
    public static final int SELECT_MULTI  = 2;

    @IntDef({FIXED_USE_DRAW, FIXED_USE_VIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FixedMode {

    }

    public static final int FIXED_USE_DRAW = 0;
    public static final int FIXED_USE_VIEW = 1;

    public static int contentTypeOf() {
        return VIEW_TYPE_CONTENT_BASE_4_GENERATE++;
    }

    public static int extTypeOf() {
        return VIEW_TYPE_EXT_BASE_4_GENERATE--;
    }

    public static boolean isContentType(int viewType) {
        return viewType > VIEW_TYPE_BASE;
    }
    public static final int DEFAULT_BLOCK_ID = 0;

    public static final String EVENT_FINISH_END_EDGE_LOAD_MORE   = "EVENT_FINISH_END_EDGE_LOAD_MORE";
    public static final String EVENT_FINISH_START_EDGE_LOAD_MORE = "EVENT_FINISH_START_EDGE_LOAD_MORE";
}
