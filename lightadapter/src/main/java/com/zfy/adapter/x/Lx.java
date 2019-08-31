package com.zfy.adapter.x;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class Lx {

    public static final int EVENT_CLICK        = 0;
    public static final int EVENT_LONG_PRESS   = 1;
    public static final int EVENT_DOUBLE_CLICK = 2;

    public static final int VIEW_TYPE_DEFAULT = 101;


    public static int VIEW_TYPE_BASE = 201;

    public static int incrementViewType() {
        return VIEW_TYPE_BASE++;
    }
}
