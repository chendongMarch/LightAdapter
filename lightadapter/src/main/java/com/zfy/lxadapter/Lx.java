package com.zfy.lxadapter;

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

    /* -------------------------- LxSnapComponent -------------------------- */

    @IntDef({SnapMode.LINEAR, SnapMode.PAGER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SnapModeDef {
    }

    public static class SnapMode {
        public static final int LINEAR = 0;
        public static final int PAGER  = 1;
    }

    /* -------------------------- LxLoadMoreComponent -------------------------- */

    @IntDef({LoadMoreEdge.END, LoadMoreEdge.START})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadMoreEdgeDef {

    }

    public static class LoadMoreEdge {
        public static final int START = 0; // 加载更多，顶部
        public static final int END   = 1; // 加载更多，底部
    }


    @IntDef({LoadMoreOn.BIND, LoadMoreOn.SCROLL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadMoreOnDef {

    }

    public static class LoadMoreOn {
        public static final int SCROLL = 0; // 通过检测 scroll 获取加载更多
        public static final int BIND   = 1; // 通过检测 onBindViewHolder 获取加载更多

    }

    /* -------------------------- LxSpan -------------------------- */

    public static class SpanSize {

        public static int BASIC = -0x30;

        public static final int NONE    = --BASIC;
        public static final int ALL     = --BASIC; // span size 占满整行
        public static final int HALF    = --BASIC; // span size 占据一半
        public static final int THIRD   = --BASIC; // span size 占据 1/3
        public static final int QUARTER = --BASIC; // span size 占据 1/4
    }

    /* -------------------------- LxEvent -------------------------- */

    @IntDef({ViewEvent.CLICK, ViewEvent.LONG_PRESS, ViewEvent.DOUBLE_CLICK, ViewEvent.FOCUS_ATTACH, ViewEvent.FOCUS_DETACH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewEventDef {

    }

    public static class ViewEvent {

        public static final int CLICK        = 0; // 单击事件
        public static final int LONG_PRESS   = 1; // 长按事件
        public static final int DOUBLE_CLICK = 2; // 双击事件
        public static final int FOCUS_CHANGE = 3; // 焦点变化事件
        public static final int FOCUS_ATTACH = 4; // 获得焦点事件
        public static final int FOCUS_DETACH = 5; // 失去焦点事件
    }


    /* -------------------------- EventType -------------------------- */

    private static int VIEW_TYPE_EXT_BASE_4_GENERATE_BEFORE_CONTENT = 40000;
    private static int VIEW_TYPE_CONTENT_BASE_4_GENERATE_MIN        = 50000;
    private static int VIEW_TYPE_CONTENT_BASE_4_GENERATE_MAX        = 59999;
    private static int VIEW_TYPE_EXT_BASE_4_GENERATE_AFTER_CONTENT  = 60000;

    public static class ViewType {

        public static final int DEFAULT          = Lx.contentTypeOf(); // 默认 viewType
        public static final int SECTION          = Lx.contentTypeOf(); // 隔断 viewType
        public static final int EXPANDABLE_GROUP = Lx.contentTypeOf(); // 分组-组
        public static final int EXPANDABLE_CHILD = Lx.contentTypeOf(); // 分组-子
    }


    public static int contentTypeOf() {
        return ++VIEW_TYPE_CONTENT_BASE_4_GENERATE_MIN;
    }

    public static int extTypeBeforeContentOf() {
        return ++VIEW_TYPE_EXT_BASE_4_GENERATE_BEFORE_CONTENT;
    }

    public static int extTypeAfterContentOf() {
        return ++VIEW_TYPE_EXT_BASE_4_GENERATE_AFTER_CONTENT;
    }

    public static boolean isContentType(int viewType) {
        return viewType >= VIEW_TYPE_CONTENT_BASE_4_GENERATE_MIN && viewType < VIEW_TYPE_CONTENT_BASE_4_GENERATE_MAX;
    }


    /* -------------------------- Drag Swipe -------------------------- */

    @IntDef({
            DragState.NONE,
            SwipeState.NONE,
            DragState.ACTIVE,
            SwipeState.ACTIVE,
            DragState.RELEASE,
            SwipeState.RELEASE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DragSwipeState {

    }

    public static class DragState {
        public static final int NONE    = 0;
        public static final int ACTIVE  = 1;
        public static final int RELEASE = 2;
    }

    public static class SwipeState {
        public static final int NONE    = 3;
        public static final int ACTIVE  = 4;
        public static final int RELEASE = 5;
    }

    /* -------------------------- LxSelectorComponent -------------------------- */

    @IntDef({SelectMode.SINGLE, SelectMode.MULTI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectModeDef {

    }

    public static class SelectMode {
        public static final int SINGLE = 1;
        public static final int MULTI  = 2;

    }

    /* -------------------------- LxFixedComponent -------------------------- */

    @IntDef({FixedMode.DRAW, FixedMode.VIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FixedModeDef {

    }

    public static class FixedMode {
        public static final int DRAW = 0;
        public static final int VIEW = 1;
    }


    /* -------------------------- 事件 -------------------------- */

    public static class Event {
        public static final String FINISH_LOAD_MORE            = "EVENT_FINISH_LOAD_MORE"; // 结束加载更多，开启下一次
        public static final String FINISH_END_EDGE_LOAD_MORE   = "EVENT_FINISH_END_EDGE_LOAD_MORE";  // 结束底部加载更多，开启下一次
        public static final String FINISH_START_EDGE_LOAD_MORE = "EVENT_FINISH_START_EDGE_LOAD_MORE"; // 结束顶部加载更多，开启下一次

        public static final String LOAD_MORE_ENABLE            = "EVENT_LOAD_MORE_ENABLE"; // 设置加载更多开关
        public static final String END_EDGE_LOAD_MORE_ENABLE   = "EVENT_END_EDGE_LOAD_MORE_ENABLE"; // 设置底部加载更多开关
        public static final String START_EDGE_LOAD_MORE_ENABLE = "EVENT_START_EDGE_LOAD_MORE_ENABLE"; // 设置顶部加载更多开关
    }

    /* -------------------------- BindMode -------------------------- */

    public static class BindMode {

        public static final int NORMAL    = 1;
        public static final int CONDITION = 2;
        public static final int PAYLOADS  = 3;

    }

    /* -------------------------- 条件更新 -------------------------- */

    public static class Condition {
        public static final String CONDITION_SELECTOR     = "CONDITION_SELECTOR";
        public static final String CONDITION_FORCE_UPDATE = "CONDITION_FORCE_UPDATE";

        public static final String KEY   = "KEY_CONDITION_KEY";
        public static final String VALUE = "KEY_CONDITION_VALUE";

    }


    public static class Loop {

        public static final int FALSE_BREAK     = 0;
        public static final int FALSE_NOT_BREAK = 1;
        public static final int TRUE_BREAK      = 2;
        public static final int TRUE_NOT_BREAK  = 3;
    }
}
