package com.zfy.adapter.common;

/**
 * CreateAt : 2018/10/29
 * Describe :
 *
 * @author chendong
 */
public class AdapterException extends IllegalStateException {

    public static final String LAYOUT_MANAGER_NOT_SET = "RecyclerView LayoutManager Required !";
    public static final String NOT_ATTACH = "RecyclerView And Adapter should attachTo first !";
    public static final String USE_BUILD_IN_TYPE = "您使用了内建类型，请检查类型不要和内建类型重复 !";


    public AdapterException(String msg) {
        super(msg);
    }
}
