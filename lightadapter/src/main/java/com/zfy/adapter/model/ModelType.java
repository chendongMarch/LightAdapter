package com.zfy.adapter.model;

import android.graphics.Rect;
import android.support.annotation.LayoutRes;

import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.SpanSize;

/**
 * CreateAt : 2018/10/26
 * Describe : 针对每种 type 的配置对象
 *
 * @author chendong
 */
public class ModelType {

    public            int type; // 数据类型
    @LayoutRes public int layoutId; // 布局资源
    public int     spanSize        = SpanSize.NONE; // 跨越行数
    public boolean enableClick     = true; // 是否允许点击事件
    public boolean enableLongPress = true; // 是否允许长按事件
    public boolean enableDbClick   = false; // 是否允许双击事件
    public boolean enableDrag      = false; // 是否允许拖动
    public boolean enableSwipe     = false; // 是否允许滑动
    public boolean enablePin       = false; // 钉住，支持悬停效果

    public Rect spaceRect;

    public ModelType(int type) {
        this.type = type;
    }

    public boolean isBuildInType() {
        return type == ItemType.TYPE_HEADER
                || type == ItemType.TYPE_FOOTER
                || type == ItemType.TYPE_CONTENT
                || type == ItemType.TYPE_LOADING
                || type == ItemType.TYPE_EMPTY;
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
