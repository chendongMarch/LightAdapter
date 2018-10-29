package com.zfy.adapter.delegate;

/**
 * CreateAt : 2018/10/29
 * Describe :
 *
 * @author chendong
 */
public class AdapterException extends IllegalStateException {

    public static final String LAYOUT_MANAGER_NOT_SET = "RecyclerView LayoutManager Required !";


    public AdapterException(String msg) {
        super(msg);
    }
}
