package com.zfy.adapter.annotations;

import com.zfy.adapter.common.SpanSize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CreateAt : 2018/11/12
 * Describe : 使用注解来构建 ModelType
 *
 * @author chendong
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface XItemType {

    int type() default com.zfy.adapter.common.ItemType.TYPE_CONTENT;

    int spanSize() default SpanSize.NONE; // 跨越

    int layoutId() default -1; // layoutId

    boolean enableClick() default true; // 是否允许点击事件

    boolean enableLongPress() default true; // 是否允许长按事件

    boolean enableDbClick() default false; // 是否允许双击事件

    boolean enableDrag() default false; // 是否允许拖动

    boolean enableSwipe() default false; // 是否允许滑动

    boolean enablePin() default false; // 钉住，支持悬停效果
}
