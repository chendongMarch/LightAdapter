package com.zfy.adapter.common;

/**
 * CreateAt : 2018/10/26
 * Describe :
 *
 * @author chendong
 */
public class LightValues {

    public static final int NONE = -0x11; // 没有值


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
