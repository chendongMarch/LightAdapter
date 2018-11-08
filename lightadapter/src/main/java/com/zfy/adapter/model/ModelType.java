package com.zfy.adapter.model;

import com.zfy.adapter.common.LightValues;

/**
 * CreateAt : 2018/10/26
 * Describe : 针对每种 type 的配置对象
 *
 * @author chendong
 */
public class ModelType {

    public int type; // 类型
    public int layoutId; // 布局资源
    public int spanSize = LightValues.NONE; // 跨越行数
    public boolean enableClick = true; // 是否允许点击事件
    public boolean enableLongPress = true; // 是否允许长按事件
    public boolean enableDbClick = false; // 是否允许双击事件，双击事件使用 gesture 实现，将会丧失一些效果
    public boolean enableDrag = false; // 是否允许拖动
    public boolean enableSwipe = false; // 是否允许滑动
    public boolean supportPin; // 钉住，会悬挂在顶部，后面的会有推的效果

    public ModelType(int type) {
        this.type = type;
    }

    public boolean isBuildInType() {
        return type == LightValues.TYPE_HEADER
                || type == LightValues.TYPE_FOOTER
                || type == LightValues.TYPE_CONTENT
                || type == LightValues.TYPE_LOADING
                || type == LightValues.TYPE_EMPTY;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
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
