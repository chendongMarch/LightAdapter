package com.zfy.adapter.x;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class Lx {

    public static final int SPAN_NONE        = -0x30;
    public static final int SPAN_SIZE_ALL    = -0x31; // span size 占满整行
    public static final int SPAN_SIZE_HALF   = -0x32; // span size 占据一半
    public static final int SPAN_SIZE_THIRD  = -0x33; // span size 占据 1/3
    public static final int SPAN_SIZE_QUATER = -0x34; // span size 占据 1/4


    public static final int EVENT_CLICK        = 0;
    public static final int EVENT_LONG_PRESS   = 1;
    public static final int EVENT_DOUBLE_CLICK = 2;

    public static final int VIEW_TYPE_DEFAULT = 101;


    public static int VIEW_TYPE_BASE = 201;

    public static int incrementViewType() {
        return VIEW_TYPE_BASE++;
    }
}
