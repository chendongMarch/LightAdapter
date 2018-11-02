package com.zfy.adapter.common;

/**
 * CreateAt : 2018/10/26
 * Describe :
 *
 * @author chendong
 */
public class LightValues {

    public static final int NONE = -0x11; // 没有值

    public static final int TYPE_HEADER = -0x21; // header type
    public static final int TYPE_FOOTER = -0x22; // footer type
    public static final int TYPE_CONTENT = -0x23; // 默认 type

    public static final int SPAN_SIZE_ALL = -0x31; // span size 占满整行
    public static final int SPAN_SIZE_HALF = -0x32; // span size 占据一半
    public static final int SPAN_SIZE_THIRD = -0x33;  // span size 占据 1/3

    public static final int SINGLE = 0x41; // 单选
    public static final int MULTI = 0x42; // 多选
}
