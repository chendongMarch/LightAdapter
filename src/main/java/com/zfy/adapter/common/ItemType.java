package com.zfy.adapter.common;

import com.zfy.adapter.listener.ModelTypeConfigCallback;

/**
 * CreateAt : 2018/11/10
 * Describe :
 *
 * @author chendong
 */
public class ItemType {

    /**
     * @see com.zfy.adapter.model.ModelType
     * @see ModelTypeConfigCallback
     * 内部的类型
     */
    public static final int TYPE_NONE    = -0x20; // header type
    public static final int TYPE_HEADER  = -0x21; // header type
    public static final int TYPE_FOOTER  = -0x22; // footer type
    public static final int TYPE_CONTENT = -0x23; // 默认 type
    public static final int TYPE_LOADING = -0x24; // 加载中
    public static final int TYPE_EMPTY   = -0x25; // 空白页
    public static final int TYPE_SECTION = -0x26; // 隔断
    public static final int TYPE_FAKE    = -0x27; // 假数据

}
