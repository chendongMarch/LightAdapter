package com.zfy.adapter.common;

import com.zfy.adapter.listener.ModelTypeConfigCallback;

/**
 * CreateAt : 2018/10/26
 * Describe :
 *
 * @author chendong
 */
public class LightValues {

    public static final int NONE = -0x11; // 没有值

    /**
     * @see com.zfy.adapter.model.ModelType
     * @see ModelTypeConfigCallback
     * 内部的类型
     */
    public static final int TYPE_HEADER = -0x21; // header type
    public static final int TYPE_FOOTER = -0x22; // footer type
    public static final int TYPE_CONTENT = -0x23; // 默认 type
    public static final int TYPE_LOADING = -0x24; // 加载中
    public static final int TYPE_EMPTY = -0x25; // 空白页
    public static final int TYPE_SECTION = -0x26; // 隔断

    /**
     * @see com.zfy.adapter.delegate.impl.SpanDelegate
     * Span Count
     */
    public static final int SPAN_SIZE_ALL = -0x31; // span size 占满整行
    public static final int SPAN_SIZE_HALF = -0x32; // span size 占据一半
    public static final int SPAN_SIZE_THIRD = -0x33;  // span size 占据 1/3

    /**
     * @see com.zfy.adapter.delegate.impl.SelectorDelegate
     * 选择器单选、多选
     */
    public static final int SINGLE = 0x41; // 单选
    public static final int MULTI = 0x42; // 多选

    /**
     * @see com.zfy.adapter.delegate.IDelegate#getAboveItemCount(int)
     * 在整个列表布局中的级别
     */
    public static final int FLOW_LEVEL_HEADER = 10; // header level
    public static final int FLOW_LEVEL_EMPTY = 20; // empty level
    public static final int FLOW_LEVEL_CONTENT = 50; // content level
    public static final int FLOW_LEVEL_FOOTER = 60; // footer level
    public static final int FLOW_LEVEL_LOADING = 70; // footer level






}
