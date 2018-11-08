package com.zfy.adapter.common;

/**
 * CreateAt : 2018/10/29
 * Describe :
 *
 * @author chendong
 */
public class AdapterException extends IllegalStateException {

    public static final String LAYOUT_MANAGER_NOT_SET = "RecyclerView LayoutManager Required !";
    public static final String NOT_ATTACH = "RecyclerView And Adapter should attach first !";


    public AdapterException(String msg) {
        super(msg);
    }
}
