package com.zfy.adapter.common;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public class SpanSize {

    /**
     * @see com.zfy.adapter.delegate.impl.SpanDelegate
     * Span Count
     */
    public static final int NONE             = -0x30;
    public static final int SPAN_SIZE_ALL    = -0x31; // span size 占满整行
    public static final int SPAN_SIZE_HALF   = -0x32; // span size 占据一半
    public static final int SPAN_SIZE_THIRD  = -0x33;  // span size 占据 1/3
    public static final int SPAN_SIZE_QUATER = -0x34;  // span size 占据 1/4
}
