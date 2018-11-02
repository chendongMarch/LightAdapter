package com.zfy.adapter.model;

import com.zfy.adapter.common.LightValues;

/**
 * CreateAt : 2018/10/26
 * Describe :
 *
 * @author chendong
 */
public class ModelType {

    private int type; // 类型
    private int layout; // 布局资源
    private int spanSize = LightValues.NONE; // 跨越行数
    private boolean enableClick = true; // 是否允许点击事件
    private boolean enableLongPress = true; // 是否允许长按事件
    private boolean enableDbClick = false; // 是否允许双击事件，双击事件使用 gesture 实现，将会丧失一些效果

    public ModelType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public boolean isEnableClick() {
        return enableClick;
    }

    public void setEnableClick(boolean enableClick) {
        this.enableClick = enableClick;
    }

    public boolean isEnableDbClick() {
        return enableDbClick;
    }

    public void setEnableDbClick(boolean enableDbClick) {
        this.enableDbClick = enableDbClick;
    }

    public boolean isEnableLongPress() {
        return enableLongPress;
    }

    public void setEnableLongPress(boolean enableLongPress) {
        this.enableLongPress = enableLongPress;
    }
}
