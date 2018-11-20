package com.zfy.adapter.model;

import android.graphics.Rect;
import android.support.annotation.LayoutRes;

import com.zfy.adapter.animations.BindAnimator;
import com.zfy.adapter.common.SpanSize;

import java.util.TreeMap;

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
    public BindAnimator animator;

    public Rect spaceRect;

    public ModelType(int type) {
        this.type = type;
    }

    public ModelType(int type, int layoutId) {
        this.type = type;
        this.layoutId = layoutId;
    }

    public ModelType(int type, int layoutId, int spanSize) {
        this.type = type;
        this.layoutId = layoutId;
        this.spanSize = spanSize;
    }

    public ModelType update(ModelType modelType) {
        this.spanSize = modelType.spanSize;
        this.enableClick = modelType.enableClick;
        this.enableLongPress = modelType.enableLongPress;
        this.enableDbClick = modelType.enableDbClick;
        this.enableDrag = modelType.enableDrag;
        this.enableSwipe = modelType.enableSwipe;
        this.enablePin = modelType.enablePin;
        return this;
    }

    public ModelType type(int type) {
        this.type = type;
        return this;
    }

    public ModelType layoutId(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public ModelType setSpanSize(int spanSize) {
        this.spanSize = spanSize;
        return this;
    }

    public ModelType enableClick(boolean enableClick) {
        this.enableClick = enableClick;
        return this;
    }

    public ModelType enableDbClick(boolean enableDbClick) {
        this.enableDbClick = enableDbClick;
        return this;
    }

    public ModelType enableLongPress(boolean enableLongPress) {
        this.enableLongPress = enableLongPress;
        return this;
    }

    public ModelType enableDrag(boolean enableDrag) {
        this.enableDrag = enableDrag;
        return this;
    }

    public ModelType enableSwipe(boolean enableSwipe) {
        this.enableSwipe = enableSwipe;
        return this;
    }

    public ModelType enablePin(boolean enablePin) {
        this.enablePin = enablePin;
        return this;
    }

    public ModelType animator(BindAnimator animator) {
        this.animator = animator;
        return this;
    }

    public ModelType spaceRect(Rect rect) {
        this.spaceRect = rect;
        return this;
    }
}
