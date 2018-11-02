package com.zfy.adapter.common;

/**
 * CreateAt : 2018/10/26
 * Describe :
 *
 * @author chendong
 */
public class Values {


    public static final String HOLDER_HEADER = "HOLDER_HEADER"; // header holder
    public static final String HOLDER_FOOTER = "HOLDER_FOOTER"; // footer holder
    public static final String HOLDER_TYPE = "HOLDER_TYPE"; // type holder

    public static final int NONE = -0x11; // 没有值

    public static final int TYPE_HEADER = -0x21; // header type
    public static final int TYPE_FOOTER = -0x22; // footer type
    public static final int TYPE_CONTENT = -0x23; // 默认 type

    public static final int SPAN_SIZE_ALL = -0x31; // span size 占满整行
    public static final int SPAN_SIZE_HALF = -0x32; // span size 占据一半
    public static final int SPAN_SIZE_THIRD = -0x33;  // span size 占据 1/3
}
