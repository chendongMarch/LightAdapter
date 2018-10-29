package com.zfy.adapter.model;

/**
 * CreateAt : 2017/6/14
 * Describe : 类型配置
 *
 * @author chendong
 */
public class TypeConfig {

    private int type;
    private int resId;

    public TypeConfig(int type, int resId) {
        this.type = type;
        this.resId = resId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
